package it.polimi.ingsw.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class Talker implements Runnable{

    private final Gson gson = new GsonBuilder().create();
    private final BlockingQueue<JsonElement> messageSource;
    private final PrintWriter out;
    private final PingTimer ping;

    private Thread thread = null;

    public Talker(BlockingQueue<JsonElement> messageSource, OutputStream outputStream, PingTimer ping) {
        this.messageSource = messageSource;
        this.out = new PrintWriter(outputStream, true);
        this.ping = ping;
    }

    @Override
    public void run() {
        this.thread = Thread.currentThread();

        while (!this.thread.isInterrupted()){

            //retrieve the message to send if it is present
            JsonElement messageJ;
            try {
                messageJ = messageSource.take();
            } catch (InterruptedException e) {
                System.out.println("Talker: Error while waiting for data to be sent");
                return;
            }

            //if there is a message to send, send it and reset the ping timer for sender
            String message = this.gson.toJson(messageJ);
            this.out.println(message);
            this.ping.resetSendTimer();
        }
        System.out.println("Talker Stopped");
    }

    public void stopTalker(){
        if (this.thread == null){
            System.out.println("Cannot stop Talker if is it not running");
        }
        this.thread.interrupt();
    }
}
