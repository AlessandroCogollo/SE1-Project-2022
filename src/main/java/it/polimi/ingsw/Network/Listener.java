package it.polimi.ingsw.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class Listener implements Runnable{

    private final Gson gson = new GsonBuilder().create();
    private final BlockingQueue<JsonElement> messageDestination;
    private final BufferedReader in;
    private final PingTimer ping;


    private Thread thread = null;

    public Listener(BlockingQueue<JsonElement> messageSource, InputStream inputStream, PingTimer ping) {
        this.messageDestination = messageSource;
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.ping = ping;
    }

    @Override
    public void run() {
        //listener thread
        this.thread = Thread.currentThread();

        while (!this.thread.isInterrupted()){
            //retrieve message from server
            String message = null;
            try {
                while (message == null && !this.thread.isInterrupted()) {
                    message = in.readLine();
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Listener: socket close");
                return;
            }

            if (message == null || message.isBlank() || message.isEmpty())
                continue;

            //reset the ping timer of receiver
            this.ping.resetReceiveTimer();

            //convert the message
            JsonElement messageJ = this.gson.fromJson(message, JsonElement.class);

            //check if the message is not only a ping message
            Message m;
            try {
                m = this.gson.fromJson(messageJ, Message.class);
            } catch (JsonSyntaxException e){
                System.out.println("Listener: received a non json message, discarded");
                continue;
            }

            if (m == null) continue;

            if (m.getError() != Errors.PING) {

                //if is not a ping message put it in the out queue
                try {
                    this.messageDestination.put(messageJ);
                } catch (InterruptedException e) {
                    System.out.println("Listener: Interrupted when putting data in the queue");
                    return;
                }
            }
        }
        System.out.println("Listener: Stopped");
    }

    public void stopListener(){
        if (this.thread == null){
            System.out.println("Cannot stop Listener if is it not running");
        }
        this.thread.interrupt();
    }
}
