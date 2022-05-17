package it.polimi.ingsw.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class Listener implements Runnable{

    private final Gson gson = new GsonBuilder().create();
    private final BlockingQueue<JsonElement> messageDest;
    private final BufferedReader in;
    private final PingTimer ping;


    private volatile boolean go = false;

    public Listener(BlockingQueue<JsonElement> messageSource, InputStream inputStream, PingTimer ping) {
        this.messageDest = messageSource;
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.ping = ping;
    }

    public boolean isRunning() {
        return go;
    }

    @Override
    public void run() {
        //listener thread
        this.go = true;
        while (go){
            //retrieve message from server
            String message = null;
            try {
                while (message == null)
                    message = in.readLine();
            } catch (IOException e) {
                if (go) //if catches an exception and the thread still need to go print error
                    e.printStackTrace();
            }

            if (message != null){

                //if not null

                //reset the ping timer of receiver
                this.ping.resetReceiveTimer();

                //convert the message
                JsonElement messageJ = this.gson.fromJson(message, JsonElement.class);

                //check if the message is not only a ping message
                Message m = this.gson.fromJson(messageJ, Message.class);
                if (m.getError() != Errors.PING) {

                    //if is not a ping message put it in the out queue
                    try {
                        this.messageDest.put(messageJ);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void stopListener(){
        this.go = false;
    }
}
