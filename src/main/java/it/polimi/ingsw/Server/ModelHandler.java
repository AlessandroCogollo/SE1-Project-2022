package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Server.Model.Game;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ModelHandler implements Runnable{

    private final Game model;
    private final QueueOrganizer queues;
    private final int[] ids;

    private volatile boolean hasToRun; //VOLATILE GUARANTEES UPDATED VALUE VISIBLE TO ALL

    public ModelHandler(int[] ids, int gameMode, QueueOrganizer q) {
        this.model = Game.getGameModel(ids, gameMode);
        this.queues = q;
        this.ids = ids;
        this.hasToRun = false;
    }

    public boolean getHasToRun() {
        return hasToRun;
    }

    public void setHasToRun(boolean hasToRun) {
        this.hasToRun = hasToRun;
    }

    @Override
    public void run() {
        //set to run
        this.hasToRun = true;

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

    private void updateClients(int errorCode, int playerId) {
        String message;
        Errors er = Errors.getErrorsByCode(errorCode);

        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new GsonBuilder().create();

        if (Errors.NO_ERROR.equals(er)) {
            //if no error the model is updated
            ModelMessage m = ModelMessageBuilder.getModelMessageBuilder().buildModelMessage(er);

            message = gson.toJson(m);
        }
        else {
            //if error return the message
            //todo retrieve username from lobby
            Message m = new Message(er, "The player " + playerId + " commit an error: " + er.getDescription());

            message = gson.toJson(m);
        }
        for (Integer id : ids){
            sendMessageToPlayer(message, id);
        }
    }

    private void sendMessageToPlayer (String message, int playerId){
        //using executor for not blocking the model queue
        new Thread(new Runnable() {
            private String message;
            private BlockingQueue<String> queue;

            public Runnable init(String myParam, BlockingQueue<String> queue) {
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

    //stop the model within 100 millisecond and take at least one other message
    public void stopModel () {
        this.hasToRun = false;
    }
}
