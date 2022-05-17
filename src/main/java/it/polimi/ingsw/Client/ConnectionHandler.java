package it.polimi.ingsw.Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.Ping;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A complete implementation that mask completely the network level and gives you two easy queue, one to the server and one from the server. <br>
 * It is implemented with a TCP Socket and use a peak of 6 thread to run (1 for the main thread that is always in wait, 2 for input and output from the server, and finally 3 for the ping where a thread creates 2 java.util.Timer).<br>
 * This class takes care also of the ping from and to this client, it send always a ping within the default timeout passed to the constructor, and calls the TimerTask function passed when the server doesn't send anything for the default timeout passed * 2. <br>
 */
public class ConnectionHandler implements Runnable{

    private final BlockingQueue<JsonElement> out = new LinkedBlockingQueue<>();
    private final Talker talker;
    private final BlockingQueue<JsonElement> in = new LinkedBlockingQueue<>();
    private final Listener listener;

    private final PingTimer pingTimer;
    private final Socket socket;

    private volatile boolean hasToRun;
    private final Object lock = new Object();

    /**
     * Create the ConnectionHandler
     * @param serverHost the ip of the server
     * @param serverPort the port of the server
     * @param graphic a graphic interface for log some possible error during the setup of the class
     * @param defaultTimeout the default timeout for the ping (exactly fro the sender, x2 for the receiver )
     * @param errorTask the task to do if the server go down and don't answer the ping message, if null it will print that the timeout is occurred in the default system out and in the graphic environment, then it will close the ConnectionHandler
     */
    public ConnectionHandler(String serverHost, int serverPort, Graphic graphic, Duration defaultTimeout, @Nullable Callable errorTask) {

        //first create the socket
        try{
            this.socket = new Socket(serverHost, serverPort);
        } catch (UnknownHostException e) {
            graphic.displayMessage("Ip or address are incorrect");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //initialize input and output from server
        InputStream fromSocket;
        OutputStream toSocket;
        try {
            fromSocket = socket.getInputStream();
            toSocket = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //create function for pint timer
        Callable sendPing = new Callable() {
            private PrintWriter printer;
            private String ping;

            public Callable init(String ping, OutputStream outputStream){
                this.printer = new PrintWriter(outputStream);
                this.ping = ping;
                return this;
            }
            @Override
            public Object call() {
                this.printer.println(ping);
                return null;
            }
        }.init(new Gson().toJson(new Ping()), toSocket);

        Callable errorCall;
        if (errorTask != null)
            errorCall = errorTask;
        else{
            errorCall = new Callable() {
                private Graphic g;
                private ConnectionHandler c;
                public Callable init(Graphic g, ConnectionHandler c){
                    this.g = g;
                    this.c = c;
                    return this;
                }
                @Override
                public Object call() {
                    g.displayMessage("Server Disconnected");
                    System.out.println("Server Disconnected");
                    c.stopConnectionHandler();
                    return null;
                }
            }.init(graphic, this);
        }

        this.pingTimer = new PingTimer(defaultTimeout, new TimerTaskCloneable(sendPing), new TimerTaskCloneable(errorCall));
        this.talker = new Talker(this.out, toSocket, pingTimer);
        this.listener = new Listener(this.in, fromSocket, pingTimer);

        graphic.displayMessage("Connected to server at " + serverHost + " at port " + serverPort);
    }

    /**
     * Getter for the queue to server, any message put in this queue is send to the server one by one in order of arrival in this queue
     * @return the queue that is used to send the message to the server
     */
    public BlockingQueue<JsonElement> getQueueToServer() {
        return out;
    }

    /**
     * Getter for the queue from server, any message that has received from server is put in this queue and the first message returned is the older message not retrieved in this queue
     * @return the queue where are stored the message received from server
     */
    public BlockingQueue<JsonElement> getQueueFromServer() {
        return in;
    }

    /**
     * Return if the ConnectionHandler is running or not
     * @return true if this class thread is running, of false otherwise
     */
    public boolean isRunning() {
        return hasToRun;
    }

    /**
     * Start all the thread needed for handling the connection
     */
    @Override
    public void run() {

        //set to run
        this.hasToRun = true;

        new Thread(this.pingTimer).start();
        new Thread(this.listener).start();
        new Thread(this.talker).start();


        synchronized (this.lock) {
            while (hasToRun) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Stop the ConnectionHandler and all his inner class including all thread created by him, close also the socket
     */
    public void stopConnectionHandler (){
        synchronized(this.lock) {
            this.hasToRun = false;
            this.lock.notifyAll();
        }

        this.pingTimer.stopPing();
        if (this.listener.isRunning())
            this.listener.stopListener();
        if (this.talker.isRunning())
            this.talker.stopTalker();

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    class Talker implements Runnable{

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

        public boolean isRunning() {
            return go;
        }

        @Override
        public void run() {
            this.go = true;
            while (go){

                //retrieve the message to send if it is present
                JsonElement messageJ = null;
                try {
                    messageJ = messageSource.poll(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (messageJ != null) {

                    //if there is a message to send, send it and reset the ping timer for sender
                    String message = this.gson.toJson(messageJ);
                    this.out.println(message);
                    this.ping.resetSendTimer();
                }
            }
        }

        public void stopTalker(){
            this.go = false;
        }
    }

    class Listener implements Runnable{

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

    class PingTimer implements Runnable{

        private Timer sendTimer = new Timer("sendTimer");
        private final TimerTaskCloneable sendPingTask;
        private final Duration sendTimeout;
        private Timer receiveTimer = new Timer("receiveTimer");
        private final TimerTaskCloneable errorTask;
        private final Duration receiveTimeout;

        public PingTimer(Duration defaultTimeout, TimerTaskCloneable sendPingTask, TimerTaskCloneable errorTask){
            this(defaultTimeout, defaultTimeout.multipliedBy(2), sendPingTask, errorTask);
        }

        public PingTimer(Duration sendTimeout, Duration receiveTimeout, TimerTaskCloneable sendPingTask, TimerTaskCloneable errorTask) {
            this.sendTimeout = sendTimeout;
            this.receiveTimeout = receiveTimeout;
            this.sendPingTask = sendPingTask;
            this.errorTask = errorTask;
        }

        /**
         * Only start the two timer and stop his thread
          */
        @Override
        public void run() {
            this.sendTimer.schedule(this.sendPingTask, 0, this.sendTimeout.toMillis());
            this.receiveTimer.schedule(this.errorTask, this.receiveTimeout.toMillis(), this.receiveTimeout.toMillis());
        }

        /**
         * Reset the send timer stopping it and rescheduling it whit the same arguments
         */
        public void resetSendTimer (){
            stopSendTimer();
            this.sendTimer = new Timer("sendTimer");
            this.sendTimer.schedule(this.sendPingTask.clone(), this.sendTimeout.toMillis(), this.sendTimeout.toMillis());
        }

        /**
         * Reset the receive timer stopping it and rescheduling it whit the same arguments
         */
        public void resetReceiveTimer (){
            stopReceiveTimer();
            this.receiveTimer = new Timer("receiveTimer");
            this.receiveTimer.schedule(this.errorTask.clone(), this.receiveTimeout.toMillis(), this.receiveTimeout.toMillis());
        }

        public void stopSendTimer(){
            this.sendTimer.cancel();
            this.sendTimer.purge();
        }

        public void  stopReceiveTimer (){
            this.receiveTimer.cancel();
            this.receiveTimer.purge();
        }

        public void stopPing (){
            stopReceiveTimer();
            stopSendTimer();
        }
    }

}
