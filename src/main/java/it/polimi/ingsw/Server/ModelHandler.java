package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Server.Model.Game;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The creator and manager of the model server side
 */
public class ModelHandler implements Runnable{

    private final Server server;
    private final Game model;
    private final QueueOrganizer queues;
    private final int[] ids;
    private final Gson gson;
    private volatile boolean hasToRun; //Volatile guarantees updated value always visible

    /**
     * Constructor of the class
     * @param ids Ids of the players
     * @param gameMode gamemode to thart the right type of game
     * @param server Server main Thread
     * @param q the Queue Organizer
     */
    public ModelHandler(int[] ids, int gameMode, Server server, QueueOrganizer q) {
        this.server = server;
        this.model = Game.getGameModel(ids, gameMode);
        this.queues = q;
        this.ids = ids;

        this.hasToRun = false;

        //this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.gson = new GsonBuilder().create();
    }

    /**
     * Retrieve if the model is working or not
     * @return true if the model is running, false otherwise
     */
    public boolean getHasToRun() {
        return hasToRun;
    }

    /**
     * stop the model within 100 millisecond and take at least one other message and send one other message to the players
     */
    public void stopModel () {
        this.hasToRun = false;
    }

    /**
     * Thread of the Model
     *
     * First it send to all the player the first model message.
     * Then it waits for some move from the players and execute them, resending to player the result.
     *
     * Finally start again to wait for some moves from players until another thread set to stop, or the game finish.
     */
    @Override
    public void run() {

        //set to run
        this.hasToRun = true;

        //first send to player the first model message
        ModelMessage m = ModelMessageBuilder.getModelMessageBuilder().buildModelMessage(Errors.NO_ERROR);
        JsonElement message = this.gson.toJsonTree(m);
        for (Integer id : ids){
            sendMessageToPlayer(message, id);
        }


        //then wait for player move

        while (hasToRun){

            ClientMessageDecorator move = null;
            try {
                move = queues.getModelQueue().poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (move != null) {

                int playerId = move.playerId();
                ClientMessage moveReceived = move.message();

                int error = moveReceived.executeMove(model, playerId);

                updateClients(error, playerId);
            }
        }
    }

    /**
     * Prepare the message to the player, The Model Message if the move was valid and the model is updated, otherwise an error message
     * @param errorCode the error from the model (0 == No Error)
     * @param playerId the player id that has done the move
     */
    private void updateClients(int errorCode, int playerId) {
        JsonElement message;
        Errors er = Errors.getErrorsByCode(errorCode);



        if (Errors.NO_ERROR.equals(er)) {
            //if no error the model is updated
            ModelMessage m = ModelMessageBuilder.getModelMessageBuilder().buildModelMessage(er);

            //if the game is over communicates it to the main server thread
            if (m.gameIsOver()){
                server.setCode(Errors.GAME_OVER);
            }

            message = gson.toJsonTree(m);
        }
        else {
            //if error return the message
            //todo retrieve username from lobby
            Message m = new Message(er, "The player " + playerId + " commit an error: " + er.getDescription());

            message = gson.toJsonTree(m);
        }
        for (Integer id : ids){
            sendMessageToPlayer(message, id);
        }
    }

    /**
     * Send the message prepared by @{link#updateClients(int, int) updateClients} to the player
     * @param message message prepared
     * @param playerId the player id to send the message
     */
    private void sendMessageToPlayer (JsonElement message, int playerId){
        //using executor for not blocking the model queue
        new Thread(new Runnable() {
            private JsonElement message;
            private BlockingQueue<JsonElement> queue;

            public Runnable init(JsonElement myParam, BlockingQueue<JsonElement> queue) {
                this.message = myParam;
                this.queue = queue;
                return this;
            }

            @Override
            public void run() {
                try {
                    queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.init(message, queues.getPlayerQueue(playerId))).start();
    }
}
