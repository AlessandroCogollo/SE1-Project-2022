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

    private ExecutorService executors = null;

    private final Object lock = new Object();
    private boolean hasToGo = true;

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
        //todo
        return null;
    }

    /**
     * Method for stop all thread of this class
     */
    public void stopClientHandler (){
        synchronized (this.lock){
            this.hasToGo = false;
            this.lock.notifyAll();
        }
    }

    public void setQueues (BlockingQueue<ClientMessageDecorator> toModel, BlockingQueue<JsonElement> toClient){
        synchronized (this.lock){
            this.toModel = toModel;
            this.toClient = toClient;
            this.lock.notifyAll();
        }
    }

    @Override
    public void run() {

        //don't used executors because the Connection handler class has his own method to be turned off
        new Thread(this.net).start();

        System.out.println("Client Handler Started id: " + this.id);

        this.executors = Executors.newSingleThreadExecutor();
        this.executors.execute(this::setupConnection);


        synchronized (this.lock){
            while ((this.toClient == null || this.toModel == null) && (this.hasToGo)){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("lock wait interrupted while waiting for queues in CLinet Handler " + this.id);
                    e.printStackTrace();
                }
            }
        }

        this.executors.shutdown();

        this.executors = null;



        if (this.hasToGo) {

            System.out.println("Client Handler " + this.id + " game started.");

            this.executors = Executors.newFixedThreadPool(2);

            this.executors.execute(this::senderModelToClient);
            this.executors.execute(this::senderClientToModel);
        }

        synchronized (this.lock){
            while (this.hasToGo){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("lock wait interrupted while game is running CLinet Handler " + this.id);
                    e.printStackTrace();
                }
            }
        }
        shutdown();
    }

    private void senderModelToClient (){
        while (true){
            JsonElement messageToClient;
            try {
                messageToClient = this.toClient.take();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for some message from the model ClientHandler " + this.id);
                e.printStackTrace();
                continue;
            }

            sendJsonToClient(messageToClient);
        }
    }

    private void senderClientToModel () {
        while (true){
            JsonElement messageFromClient = readJsonFromClient();

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
                //the put only wait if the queues have a max number of message, in our case no
                e.printStackTrace();
            }
        }
    }

    private void setupConnection () {
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

        System.out.println("ClientHandler: " + this.id + ". " + "This client is ready");

        l.SetOk(this.id, this.username, this.wizard);

        System.out.println("Client Handler " + this.id + " waiting for the start of the game");
    }

    private void shutdown (){
        System.out.println("Client Handler " + this.id + " shutting down.");

        this.net.stopConnectionHandler();

        if (this.executors != null && !this.executors.isTerminated())
            this.executors.shutdown();
    }





    private JsonElement readJsonFromClient(){
        JsonElement J = null;
        while (J == null) {
            try {
                J = this.net.getQueueFromServer().poll(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("readJson method in Client handler interrupted");
            }
        }
        return J;
    }

    private void sendJsonToClient(JsonElement m){
        try {
            this.net.getQueueToServer().put(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("send method in Client handler interrupted");
        }
    }

    private void sendMessageToClient(Message m){
        sendJsonToClient(this.gson.toJsonTree(m));
    }

    private void ReceiveFirstMessage() {
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

    private void ReceiveFirstPlayerData(){
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

    private void ReceiveData(){
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

    private void ReceiveConfirm(){
        Message m = null;
        while (m == null){
            JsonElement j = readJsonFromClient();
            Message temp = this.gson.fromJson(j, Message.class);
            if (temp.getError().equals(Errors.CLIENT_READY))
                m = temp;
        }
    }
}
