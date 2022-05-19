package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;

import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.*;

import it.polimi.ingsw.Network.ConnectionHandler;

/**
 * Class that creates the Abstract view of the client
 */
public class ClientHandler implements Runnable{

    private final int id;
    private final Lobby l;
    private final ConnectionHandler net;
    private final Gson gson = new Gson();

    private final ExecutorService main = Executors.newSingleThreadExecutor();
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
        this.net = new ConnectionHandler(client, Duration.ofSeconds(60), this::clientDown);
    }

    /**
     * Used only by the timer in connection handler if the client go down
     * @return always null for override call method of Callable
     */
    public Object clientDown (){
        this.l.clientDown(this.id);
        return null;
    }

    /**
     * Method for stop all thread of this class, can only be used after the run command
     */
    public void shutdown (){
        System.out.println("Client Handler " + this.id + " shutting down.");

        this.net.stopConnectionHandler();

        //this is for stop the main class if it's still going
        this.main.shutdownNow();

        if (this.executors != null && !this.executors.isTerminated())
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
     * Main thread of ClientHandler, first start a thread for handling the setup of the connection.
     * Then wait until the game has to start, and finally start two thread for exchange message between client and model
     */
    @Override
    public void run() {

        //don't used executors because the Connection handler class has his own method to be turned off
        new Thread(this.net).start();

        this.main.execute(this::main);
    }

    //thread method

    private void main (){
        System.out.println("Client Handler Started id: " + this.id);

        this.executors = Executors.newSingleThreadExecutor();
        this.executors.execute(this::setupConnection);


        synchronized (this.lock){
            while ((this.toClient == null || this.toModel == null)){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("lock wait interrupted while waiting for queues in ClientHandler " + this.id + " line: " + new Throwable().getStackTrace()[0].getLineNumber());
                    return;
                }
            }
        }

        this.executors.shutdownNow();

        this.executors = null;

        System.out.println("Client Handler " + this.id + " game started.");

        this.executors = Executors.newFixedThreadPool(2);

        this.executors.execute(this::senderModelToClient);
        this.executors.execute(this::senderClientToModel);

        //now only the two executors continue to exchange message
    }

    private void senderModelToClient (){
        while (true){
            JsonElement messageToClient;
            try {
                messageToClient = this.toClient.take();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for some message from the model ClientHandler " + this.id);
                return;
            }

            sendJsonToClient(messageToClient);
        }
    }

    private void senderClientToModel () {
        while (true){
            JsonElement messageFromClient;
            try {
                messageFromClient = readJsonFromClient();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting some message from the client ClientHandler " + this.id);
                return;
            }

            ClientMessage temp = this.gson.fromJson(messageFromClient, ClientMessage.class);
            int moveId = temp.getMoveId();

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
                System.err.println("Interrupted while waiting to put some message in the queue to the model ClientHandler " + this.id);
                return;
            }
        }
    }

    private void setupConnection () {

        try {

            ReceiveFirstMessage();

            System.out.println("ClientHandler: " + this.id + ". " + "Received first message from client");

            SendFirstMessage();

            System.out.println("ClientHandler: " + this.id + ". " + "Sent if client is first client");

            if (this.id == 0)
                ReceiveFirstPlayerData();
            else
                ReceiveData();

            System.out.println("ClientHandler: " + this.id + ". " + "Received data from client client");

            SendId();

            System.out.println("ClientHandler: " + this.id + ". " + "Sent Id");

            ReceiveConfirm();
        } catch (InterruptedException e){
            System.err.println("Interrupted while setup the connection with the player ClientHandler " + this.id);
            return;
        }

        System.out.println("ClientHandler: " + this.id + ". " + "This client is ready");

        l.SetOk(this.id, this.username, this.wizard);

        System.out.println("Client Handler " + this.id + " waiting for the start of the game");
    }







    private JsonElement readJsonFromClient() throws InterruptedException {
        return this.net.getQueueFromServer().take();
    }

    private void sendJsonToClient(JsonElement m){
        try {
            this.net.getQueueToServer().put(m);
        } catch (InterruptedException e) {
            System.err.println("Send method interrupted ClientHandler " + this.id + " line: " + new Throwable().getStackTrace()[0].getLineNumber());
        }
    }

    private void sendMessageToClient(Message m){
        sendJsonToClient(this.gson.toJsonTree(m));
    }

    private void ReceiveFirstMessage() throws InterruptedException {
        Message m = null;
        while (m == null){
            JsonElement j = readJsonFromClient();
            Message temp = this.gson.fromJson(j, Message.class);
            if (temp.getError().equals(Errors.FIRST_MESSAGE_CLIENT))
                m = temp;
        }
    }

    private void SendFirstMessage() {
        sendMessageToClient(new NewPlayerMessage("Welcome!", this.id == 0));
    }

    private void ReceiveFirstPlayerData() throws InterruptedException {
        boolean ok = false;
        int playerN = -1;
        int gameMode = -1;
        FirstPlayerMessage m = null;
        while (!ok) {
            JsonElement Jm = readJsonFromClient();
            Message temp = this.gson.fromJson(Jm, Message.class);
            while (!temp.getError().equals(Errors.FIRST_CLIENT)) {
                Jm = readJsonFromClient();
                temp = this.gson.fromJson(Jm, Message.class);
            }
            m = this.gson.fromJson(Jm, FirstPlayerMessage.class);
            playerN = m.getNumOfPlayer();
            gameMode = m.getGameMode();
            if (l.getUsernames().containsValue(m.getUsername())) {
                sendMessageToClient(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            } else if (l.getWizards().containsValue(m.getWizard())) {
                sendMessageToClient(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            } else if (playerN < 2 || playerN > 4){
                sendMessageToClient(new Message(Errors.NUM_OF_PLAYER_ERROR, "Please select a valid number (2-4): "));
            } else if (gameMode < 0 || gameMode > 1){
                sendMessageToClient(new Message(Errors.WRONG_GAME_MODE, "Please select a valid game mode (0-1): "));
            } else {
                ok = true;
            }
        }
        this.l.setParameters(playerN, gameMode);
        this.username = m.getUsername();
        this.wizard = m.getWizard();
    }

    private void ReceiveData() throws InterruptedException {
        boolean ok = false;
        NotFirstPlayerMessage m = null;
        while (!ok) {
            JsonElement Jm = readJsonFromClient();
            Message temp = this.gson.fromJson(Jm, Message.class);
            while (!temp.getError().equals(Errors.NOT_FIRST_CLIENT)) {
                Jm = readJsonFromClient();
                temp = this.gson.fromJson(Jm, Message.class);
            }
            m = this.gson.fromJson(Jm, NotFirstPlayerMessage.class);
            if (l.getUsernames().containsValue(m.getUsername())) {
                sendMessageToClient(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            } else if (l.getWizards().containsValue(m.getWizard())) {
                sendMessageToClient(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            } else {
                ok = true;
            }
        }
        this.username = m.getUsername();
        this.wizard = m.getWizard();
    }

    private void SendId(){
        sendMessageToClient(new IdMessage("Your id is: ", this.id));
    }

    private void ReceiveConfirm() throws InterruptedException {
        Message m = null;
        while (m == null){
            JsonElement j = readJsonFromClient();
            Message temp = this.gson.fromJson(j, Message.class);
            if (temp.getError().equals(Errors.CLIENT_READY))
                m = temp;
        }
    }
}
