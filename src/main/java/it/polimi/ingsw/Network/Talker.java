package it.polimi.ingsw.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Talker implements Runnable{

    private final Gson gson = new GsonBuilder().create();
    private final BlockingQueue<JsonElement> messageSource;
    private final PrintWriter out;
    private final PingTimer ping;

    private volatile boolean go = false;

    public Talker(BlockingQueue<JsonElement> messageSource, OutputStream outputStream, PingTimer ping) {
        this.messageSource = messageSource;
        this.out = new PrintWriter(outputStream, true);
        this.ping = ping;
    }

    @Override
    public void run() {
        this.go = true;
        while (this.go){

            //retrieve the message to send if it is present
            JsonElement messageJ = null;
            try {
                messageJ = messageSource.poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println("Listener: Error while retrieving the message to send");
                this.go = false;
            }

            if (messageJ == null)
                continue;

            //if there is a message to send, send it and reset the ping timer for sender
            String message = this.gson.toJson(messageJ);
            this.out.println(message);
            this.ping.resetSendTimer();
        }
        System.out.println("Talker Stopped");
    }

    public void stopTalker(){
        this.go = false;
    }
}
