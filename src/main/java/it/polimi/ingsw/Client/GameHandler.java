package it.polimi.ingsw.Client;

import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Message.ModelMessage;
import it.polimi.ingsw.Network.ConnectionHandler;


public class GameHandler implements Runnable{

    private final int myId;
    private final ConnectionHandler net;
    private final Graphic g;

    private ModelMessage model;

    public GameHandler(int myId, ConnectionHandler net, Graphic g) {
        this.myId = myId;
        this.net = net;
        this.g = g;
    }

    @Override
    public void run() {
        //the setup of the connection is done, now the client wait for the start of the game

        //todo the stop and the handling of error
        /*while (true){
            this.model = waitForModel();

            JsonElement move = elaborateModel(this.model, g);

            sendMove(move);
        }*/
    }
    //TODO

    private void sendMove(JsonElement move) {
    }

    private static JsonElement elaborateModel(ModelMessage model, Graphic g) {
        return null;
    }

    private ModelMessage waitForModel() {
        return null;
    }
}
