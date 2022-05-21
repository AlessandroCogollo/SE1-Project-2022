package it.polimi.ingsw.Client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Cli;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Message.ClientMessage;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.ModelMessage;
import it.polimi.ingsw.Network.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameHandler implements Runnable{

    private final int myId;
    private final ConnectionHandler net;
    private final Graphic g;
    private final Client client;
    private final ExecutorService main = Executors.newSingleThreadExecutor();
    private final Gson gson = new Gson();

    private ModelMessage model;

    public GameHandler(int myId, ConnectionHandler net, Graphic g, Client client) {
        this.myId = myId;
        this.net = net;
        this.g = g;
        this.client = client;
    }

    public void stopGameHandler (){
        this.main.shutdownNow();
    }

    @Override
    public void run() {
        //the setup of the connection is done, now the client wait for the start of the game

        this.main.execute(this::main);
    }

    private void main(){
        while (true){
            try {
                waitForModel();
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for some message from server");
                return;
            }

            JsonElement move;
            try {
                move = elaborateModel();
            } catch (IOException | InterruptedException e) {
                System.err.println(e.getMessage());
                return;
            }

            try {
                sendMove(move);
            } catch (InterruptedException e) {
                System.err.println("Error while waiting to send message to server");
                return;
            }
        }
    }

    private void sendMove(JsonElement move) throws InterruptedException {
        if (move == null)
            return;

        this.net.getQueueToServer().put(move);
    }

    private JsonElement elaborateModel() throws IOException, InterruptedException {
        if (this.model == null)
            return null;

        if (this.model.gameIsOver()){
            gameOver();
            return null;
        }

        if (this.model.getCurrentPlayerId() != this.myId)
            return null;

        this.g.displayMessage("It's your turn");
        return askMove();
    }

    private void gameOver() {
        //todo
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
            switch (aP) {
                case MoveStudent -> cM = this.g.askStudentMovement(this.model, this.myId);
                case MoveMotherNature -> cM = this.g.askMNMovement(this.model, this.myId);
                case ChooseCloud -> cM = this.g.askCloud(this.model, this.myId);
            }
        }
        return this.gson.toJsonTree(cM);
    }

    private void waitForModel() throws InterruptedException {
        Message temp = null;
        JsonElement mJ = null;

        while (temp == null || !Errors.NO_ERROR.equals(temp.getError())) {
            mJ = this.net.getQueueFromServer().take();

            temp = this.gson.fromJson(mJ, Message.class);

            if (!Errors.NO_ERROR.equals(temp.getError())){
                this.g.displayMessage(temp.getMessage());
            }
        }
        //todo idea say that the prec player has done his move
        this.model = this.gson.fromJson(mJ, ModelMessage.class);
    }
}
