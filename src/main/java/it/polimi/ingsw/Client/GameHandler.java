package it.polimi.ingsw.Client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Message.ClientMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;


public class GameHandler implements Runnable{

    private final int myId;
    private final BlockingQueue<JsonElement> in;
    private final BlockingQueue<JsonElement> out;
    private final Graphic g;
    private final Client client;
    private final Gson gson = new Gson();

    private Thread thread = null;

    private ModelMessage model;
    private boolean firstModel = false;

    public GameHandler(int myId, BlockingQueue<JsonElement> in, BlockingQueue<JsonElement> out, Graphic g, Client client) {
        this.myId = myId;
        this.in = in;
        this.out = out;
        this.g = g;
        this.client = client;
    }

    public void stopGameHandler (){
        if (this.thread == null){
            System.out.println("Cannot stop GameHandler if is it not running");
            return;
        }
        this.thread.interrupt();
    }

    @Override
    public void run() {

        this.thread = Thread.currentThread();

        //the setup of the connection is done, now the client wait for the start of the game
        while (!this.thread.isInterrupted()){
            try {
                waitForModel();
            } catch (InterruptedException e) {
                System.out.println("GameHandler: Interrupted while waiting for some message from server");
                return;
            }

            JsonElement move;
            try {
                move = elaborateModel();
            } catch (IOException | InterruptedException e) {
                System.out.println("GameHandler: Interrupted while waiting for move from player");
                return;
            }

            try {
                sendMove(move);
            } catch (InterruptedException e) {
                System.out.println("GameHandler: Interrupted while waiting to send message to server");
                return;
            }
        }
    }

    private void sendMove(JsonElement move) throws InterruptedException {
        if (move == null)
            return;

        this.out.put(move);
    }

    private JsonElement elaborateModel() throws IOException, InterruptedException {
        if (this.model == null)
            return null;

        if (!firstModel){
            this.g.displayMessage("Game started");
            this.g.displayMessage("First Model received");
            firstModel = true;
        }

        if (this.model.gameIsOver()){
            gameOver();
            return null;
        }

        if (this.model.getCurrentPlayerId() != this.myId){
            this.g.displayMessage("Model Received");
            this.g.displayModel(this.model);
            return null;
        }


        this.g.displayMessage("It's your turn");
        return askMove();
    }

    private void gameOver() {
        //todo win message
        this.client.setCode(Errors.GAME_OVER);
    }

    private JsonElement askMove() throws IOException, InterruptedException {
        Phase p = Phase.valueOf(this.model.getActualPhase());
        ActionPhase aP = ActionPhase.valueOf(this.model.getActualActionPhase());

        ClientMessage cM = null;
        if (Phase.Planning.equals(p)){
            cM =  this.g.askAssistant(this.model, this.myId);
        }
        else {
            if (this.model.getGameMode() == 1){
                cM = this.g.askCharacter(this.model, this.myId);
            }
            if (cM == null){
                switch (aP) {
                    case MoveStudent -> {
                        this.g.displayMessage("You have to move other " + this.model.getStudentsToMove());
                        cM = this.g.askStudentMovement(this.model, this.myId);
                    }
                    case MoveMotherNature -> cM = this.g.askMNMovement(this.model, this.myId);
                    case ChooseCloud -> cM = this.g.askCloud(this.model, this.myId);
                }
            }
        }
        return this.gson.toJsonTree(cM);
    }

    private void waitForModel() throws InterruptedException {
        Message temp = null;
        JsonElement mJ = null;

        while (temp == null || !Errors.NO_ERROR.equals(temp.getError())) {
            mJ = this.in.take();

            temp = this.gson.fromJson(mJ, Message.class);

            if (!Errors.NO_ERROR.equals(temp.getError())){

                this.g.displayMessage(temp.getMessage());

                if (this.model != null && this.model.getCurrentPlayerId() == this.myId)
                    return;
            }
        }
        //todo idea say that the precedent player has done his move
        this.model = this.gson.fromJson(mJ, ModelMessage.class);
    }
}
