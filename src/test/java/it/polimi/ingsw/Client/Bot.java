package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GraphicInterface.TestingGraphicHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A bot that could be used for testing, you need to start the server first at the port used here (if the bot is the first player always set the game for 4 players and game mode 1)
 */
public class Bot {

    static final int number = 3; // start number of bot (if <= 1 start a single bot)
    static final int port = 5088;

    public static void main(String[] args){
        int x = Math.max(number, 1);
        ExecutorService ex = Executors.newFixedThreadPool(x);
        for (int i = 0; i < x; i++) {
            ex.execute(Bot::startSingle);
        }
        ex.shutdown(); //let the bot to finish
    }


    static void startSingle(){
        TestingGraphicHandler tgh = new TestingGraphicHandler("Gui");
        tgh.startGraphic();
        Client c = new Client(tgh, "127.0.0.1", port);
        c.start();
    }
}
