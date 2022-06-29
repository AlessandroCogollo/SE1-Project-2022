package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Network.ConnectionHandler;

import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that creates the Abstract view of the client.
 * It use the NetworkHandler for send the message to the player.
 * Implements the setup of communication described in the deliverables folder, server side.
 *
 * When the game is stared creates two thread that only pass the message from the model to the client and from the client to the model.
 */
public class ClientHandler implements Runnable{

    private final int id;
    private final Lobby l;
    private final ConnectionHandler net;
    private final Gson gson = new Gson();

    private Thread thread = null;
    private ExecutorService executors = null;

    private final Object lock = new Object();

    private String username = null;
    private Wizard wizard = null;

    private BlockingQueue<ClientMessageDecorator> toModel = null;
    private BlockingQueue<JsonElement> toClient = null;

    /**
     * Constructor of ClientHandler
     * @param client the socket to communicate with client
     * @param id id of this client handler and of the client
     * @param lobby lobby for send info about the client to the main server
     */
    public ClientHandler(Socket client, int id, Lobby lobby){
        this.id = id;
        this.l = lobby;
        this.net = new ConnectionHandler(client, Duration.ofSeconds(15), this::clientDown);
    }

    /**
     * Used only by the timer in connection handler if the client go down
     * @return always null for override call method of Callable
     */
    public Object clientDown (){
        System.out.println("ClientHandler: " + this.id + " this player has been disconnected.");
        this.l.clientDown(this.id);
        return null;
    }

    /**
     * Method for stop all thread of this class, can only be used after the run command
     */
    public void shutdown (){

        if (this.thread == null){
            System.out.println("Cannot stop the ClientHandler if it is not running");
            return;
        }

        this.net.stopConnectionHandler();

        //this is for stop the main class if it's still going
        this.thread.interrupt(); //this thread not use any io method so it finish gratefully

        if (this.executors != null)
            this.executors.shutdownNow();
    }

    /**
     * Method used by lobby for set the queues and start the game
     * @param toModel queues of ClientMessageDecorator used for send message from client to model
     * @param toClient queues of JsonElement used for send message from model to client
     */
    public void setQueues (BlockingQueue<ClientMessageDecorator> toModel, BlockingQueue<JsonElement> toClient){
        synchronized (this.lock){
            this.toModel = toModel;
            this.toClient = toClient;
            this.lock.notifyAll();
        }
    }

    /**
     * Method used by the lobby for ensures that all message are sent to the player
     * @return if all message are sent to the client and the queue out is empty
     */
    public boolean allMessageSent(){
        return this.net.getOutQueue().isEmpty();
    }

    /**
     * Main thread of ClientHandler, first start a thread for handling the setup of the connection.
     * Then wait until the game has to start, and finally start two thread for exchange message between client and model
     */
    @Override
    public void run() {

        this.thread = Thread.currentThread();

        //don't used executors because the Connection handler class has his own method to be turned off
        new Thread(this.net, "ConnectionHandler " + this.id).start();

        if (this.thread.isInterrupted()){
            System.out.println("Client Handler " + this.id + ": my main thread interrupted");
            return;
        }

        System.out.println("Client Handler Started id: " + this.id);

        setupConnection();

        if (this.thread.isInterrupted()){
            System.out.println("Client Handler " + this.id + ": my main thread interrupted");
            return;
        }

        System.out.println("Client Handler " + this.id + " waiting for the start of the game");

        synchronized (this.lock){
            while ((this.toClient == null || this.toModel == null)){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("ClientHandler " + this.id + ": interrupt when waiting for the start of the game");
                    return;
                }
            }
        }

        if (this.thread.isInterrupted()){
            System.out.println("Client Handler " + this.id + ": my main thread interrupted");
            return;
        }

        System.out.println("Client Handler " + this.id + " game started.");

        this.executors = Executors.newFixedThreadPool(2);

        this.executors.execute(this::senderModelToClient);
        this.executors.execute(this::senderClientToModel);

        //now only the two executors continue to exchange message
    }

    //thread method

    private void senderModelToClient (){
        while (!Thread.currentThread().isInterrupted()){
            JsonElement messageToClient;
            try {
                messageToClient = this.toClient.take();
            } catch (InterruptedException e) {
                System.out.println("ClientHandler " + this.id + ": Interrupted while waiting for some message from the model");
                return;
            }

            try {
                sendMessage(messageToClient);
            } catch (InterruptedException e) {
                System.out.println("ClientHandler " + this.id + ": Interrupted while sending some message to client");
                return;
            }
        }
    }

    private void senderClientToModel () {
        while (!Thread.currentThread().isInterrupted()){
            JsonElement messageFromClient;
            try {
                messageFromClient = readMessageFromClient();
            } catch (InterruptedException e) {
                System.out.println("ClientHandler " + this.id + ": Interrupted while waiting for some message from the client");
                return;
            }


            int moveId = messageFromClient.getAsJsonObject().get("moveId").getAsInt();

            if (moveId < 1 || moveId > 5){
                System.err.println("Move Id not exists, ClientHandler " + this.id);
                continue;
            }

            ClientMessage right = null;
            switch (moveId){
                case 1 -> right = this.gson.fromJson(messageFromClient, PlayAssistantMessage.class);
                case 2 -> right = this.gson.fromJson(messageFromClient, MoveStudentMessage.class);
                case 3 -> right = this.gson.fromJson(messageFromClient, MoveMotherNatureMessage.class);
                case 4 -> right = this.gson.fromJson(messageFromClient, ChooseCloudMessage.class);
                case 5 -> right = this.gson.fromJson(messageFromClient, PlayCharacterMessage.class);
            }

            ClientMessageDecorator m = new ClientMessageDecorator(right, this.id);
            try {
                this.toModel.put(m);
            } catch (InterruptedException e) {
                System.out.println("ClientHandler " + this.id + ": Interrupted while sending some message to the model");
                return;
            }
        }
    }

    private void setupConnection () {
        Collection<Errors> temp = new ArrayList<>();
        try {

            temp.add(Errors.FIRST_MESSAGE_CLIENT);
            waitForResponse(temp);

            System.out.println("ClientHandler: " + this.id + " Received first message from client");

            sendMessage(new NewPlayerMessage("Welcome!", this.id == 0));

            System.out.println("ClientHandler: " + this.id + " Sent if client is first client");

            temp.clear();

            if (this.id == 0)
                ReceiveFirstPlayerData();
            else
                ReceiveData();

            System.out.println("ClientHandler: " + this.id + " Received data from client");

            sendMessage(new IdMessage("Your id is: ", this.id));

            System.out.println("ClientHandler: " + this.id + " Sent Id");

            temp.clear();

            temp.add(Errors.CLIENT_READY);
            waitForResponse(temp);

        } catch (InterruptedException e){
            System.out.println("ClientHandler " + this.id + ": Interrupted while setup the connection with the player at line:" + e.getStackTrace()[0].getLineNumber());
            this.thread.interrupt(); // reset the interrupt flag
            return;
        }

        System.out.println("ClientHandler: " + this.id + " This client is ready");

        l.SetOk(this.id, this.username, this.wizard);
    }







    private JsonElement readMessageFromClient() throws InterruptedException {
        return this.net.getInQueue().take();
    }

    /**
     * Used by the Lobby for send the message ia a player disconnected when the setup isn't finished
     * @param m JsonElement that will be converted to a string and sent to the Client
     */
    public void sendMessage(JsonElement m) throws InterruptedException {
        this.net.getOutQueue().put(m);
    }

    private void sendMessage (Message m) throws InterruptedException {
        sendMessage(this.gson.toJsonTree(m));
    }

    private JsonElement waitForResponse (Collection<Errors> corrects) throws InterruptedException {
        JsonElement message = null;
        Errors codeReceived = null;
        while (!corrects.contains(codeReceived)){
            message = readMessageFromClient();
            Message temp = this.gson.fromJson(message, Message.class);
            codeReceived = temp.getError();
        }
        return message;
    }

    private void ReceiveFirstPlayerData() throws InterruptedException {

        Collection<Errors> temp = new ArrayList<>();

        temp.add(Errors.FIRST_CLIENT);

        boolean ok = false;
        FirstPlayerMessage data = null;

        while (!ok){
            JsonElement jM = waitForResponse(temp);
            data = this.gson.fromJson(jM, FirstPlayerMessage.class);

            //check correct value
            int tempN = data.getNumOfPlayer();
            int tempG = data.getGameMode();
            if (l.getUsernames().containsValue(data.getUsername())) {
                sendMessage(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            } else if (!validUsername(data.getUsername())){
                sendMessage(new Message(Errors.USERNAME_NOT_AVAILABLE, "Username can't contain special characters"));
            } else if (l.getWizards().containsValue(data.getWizard())) {
                sendMessage(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            } else if (tempN < 2 || tempN > 4){
                sendMessage(new Message(Errors.NUM_OF_PLAYER_ERROR, "Please select a valid number (2-4): "));
            } else if (tempG < 0 || tempG > 1){
                sendMessage(new Message(Errors.WRONG_GAME_MODE, "Please select a valid game mode (0-1): "));
            } else {
                ok = true;
            }
        }
        this.l.setParameters(data.getNumOfPlayer(), data.getGameMode());
        this.username = data.getUsername();
        this.wizard = data.getWizard();
    }

    private boolean validUsername(String username) {
        if (username == null || username.isBlank() || username.isEmpty())
            return false;

        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(username);

        return !hasSpecial.find();
    }

    private void ReceiveData() throws InterruptedException {
        Collection<Errors> temp = new ArrayList<>();

        temp.add(Errors.NOT_FIRST_CLIENT);

        boolean ok = false;
        NotFirstPlayerMessage data = null;

        while (!ok){
            JsonElement jM = waitForResponse(temp);
            data = this.gson.fromJson(jM, NotFirstPlayerMessage.class);

            //check correct value
            if (l.getUsernames().containsValue(data.getUsername())) {
                sendMessage(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            } else if (!validUsername(data.getUsername())){
                sendMessage(new Message(Errors.USERNAME_NOT_AVAILABLE, "Username can't contain special characters"));
            } else if (l.getWizards().containsValue(data.getWizard())) {
                sendMessage(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            } else {
                ok = true;
            }
        }

        this.username = data.getUsername();
        this.wizard = data.getWizard();
    }
}
