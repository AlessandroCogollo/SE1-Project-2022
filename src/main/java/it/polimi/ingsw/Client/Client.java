package it.polimi.ingsw.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.*;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Network.ConnectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main Client class
 */
public class Client{

    private final GraphicHandler graphic;
    private final ConnectionHandler connection;
    private final Gson gson = new GsonBuilder().create();
    private final BlockingQueue<JsonElement> filteredIn = new LinkedBlockingQueue<>();

    private Thread setup = null;
    private Thread filter = null;

    private GameHandler game = null;

    private final Object lock = new Object();
    private Errors code = Errors.NOTHING_TODO;

    /**
     * Simpler Constructor with only the ip and the port of the server, it will ask the player if he wants a cli or a gui interface and the ping timer is set to 60 sec
     * @param serverIp ip of server
     * @param serverPort port of server
     */
    public Client (String serverIp, int serverPort){
        this(askGraphic(), serverIp, serverPort);
    }

    /**
     * Start a client that connects to server with coordination given and use the given Interface to display information, the ping timer is set to 60 sec
     * @param graphic Interface for display information to player
     * @param serverIp ip of server
     * @param serverPort port of server
     */
    public Client (GraphicHandler graphic, String serverIp, int serverPort){
        this( graphic, serverIp, serverPort, Duration.ofSeconds(15));
    }

    /**
     * Start a client that connects to server with coordination given and use the given Interface to display information, the ping timer is set to the given timeout
     * @param graphic Interface for display information to player
     * @param serverHost ip of server
     * @param serverPort port of server
     * @param defaultTimeout timeout for ping
     */
    public Client(GraphicHandler graphic, String serverHost, int serverPort, Duration defaultTimeout) {
        this.graphic = graphic;
        ConnectionHandler temp = null;
        try {
            temp = new ConnectionHandler(serverHost, serverPort, defaultTimeout, this::serverDown);
        } catch (IOException e) {
            System.err.println("Error connecting to " + serverHost + " at " + serverPort);
            e.printStackTrace();
            System.exit(-1);
        }
        this.connection = temp;
    }

    /**
     * Ask the player what type of interface he prefers, CLi or Gui
     * @return the chosen interface
     */
    public static GraphicHandler askGraphic() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        while (!GraphicHandler.isValidString(s)){
            try {
                s = input.readLine();
            } catch (IOException ignored) {}
        }
        return new GraphicHandler(s);
    }

    /**
     * Start the main thread of client
     */
    public void start() {

        Thread.currentThread().setName("Main thread Client");

        this.graphic.setGraphicStopCallback(this::graphicStopped);

        new Thread(this.connection, "Connection Handler").start();

        new Thread(this::messageFilter, "Client Filter").start();

        new Thread(this::setupConnectionAndStartGame, "ConnectionSetup").start();


        //do nothing until some other thread tells him what to do with a code

        synchronized(this.lock) {
            while(doSomething()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Client main Thread interrupted");
                    break;
                }
            }
        }
        System.out.println("Client is shutting down");
        //came here only when the client has to shutdown
        shutdownAll();
    }

    /**
     * Set the code to tell the main thread of the client what to do
     * @param er error code corresponding to an action that will be done by the client main thread
     */
    public void setCode (Errors er){
        synchronized(this.lock) {
            this.code = er;
            this.lock.notifyAll();
        }
    }

    private void graphicStopped() {
        this.setCode(Errors.GRAPHIC_STOPPED);
    }
    private void shutdownAll() {
        if (this.setup != null)
            this.setup.interrupt();
        this.filter.interrupt();
        this.connection.stopConnectionHandler();
        if (this.game != null)
            this.game.stopGameHandler();
        this.graphic.stopInput();
    }

    private Object serverDown() {
        setCode(Errors.SERVER_DOWN);
        return null;
    }

    private boolean doSomething() {
        boolean go = true;

        //return false only when the Server need to shutdown otherwise return always true
        switch (this.code) {
            case SETUP_FINISHED -> {
                System.out.println("Ready to play, waiting for the start of the game");
                new Thread(this.game, "GameHandler").start();
            }
            case SERVER_DOWN -> {
                System.out.println("The server go down, shutting down");
                go = false;
            }
            case GAME_OVER -> {
                System.out.println("The game is finished, shutting down");
                go = false;
            }
            case GRAPHIC_STOPPED -> {
                System.out.println("Graphic has stopped, shutting down");
                go = false;
            }
            case PLAYER_DISCONNECTED, CANNOT_ACCEPT -> go = false;
        }

        //reset code
        this.code = Errors.NOTHING_TODO;
        return go;
    }

    private void messageFilter() {
        this.filter = Thread.currentThread();
        BlockingQueue<JsonElement> netIn = this.connection.getInQueue();
        while (!this.filter.isInterrupted()){

            JsonElement jM;
            try {
                jM = netIn.take();
            } catch (InterruptedException e) {
                System.out.println("Client Filter: interrupted while waiting for some message");
                return;
            }

            Message temp = this.gson.fromJson(jM, Message.class);

            if (Errors.PLAYER_DISCONNECTED.equals(temp.getError())) {
                System.out.println(temp.getMessage());
                setCode(Errors.PLAYER_DISCONNECTED);
                return;
            }

            if (Errors.CANNOT_ACCEPT.equals(temp.getError())){
                System.out.println(temp.getMessage());
                setCode(Errors.CANNOT_ACCEPT);
                return;
            }

            try {
                this.filteredIn.put(jM);
            } catch (InterruptedException e) {
                System.out.println("Client Filter: interrupted while waiting for put message in filtered queue");
                return;
            }
        }
    }


    //method for set the initial connection

    private void setupConnectionAndStartGame() {

        this.setup = Thread.currentThread();

        int id;
        Collection <Errors> temp = new ArrayList<>();
        //this operation are done without the intervention of user and must be done in this order, so is not needed for have multiple thread (except for the timeout)

        //start the handbrake with server
        try {

            sendMessage(Errors.FIRST_MESSAGE_CLIENT, Errors.FIRST_MESSAGE_CLIENT.getDescription());

            System.out.println("Sent first message to server");

            temp.add(Errors.FIRST_MESSAGE_SERVER);
            //wait for the first answer from server
            JsonElement answer = waitForResponse(temp);
            temp.clear();

            System.out.println("Received first message from server");

            NewPlayerMessage npM = gson.fromJson(answer, NewPlayerMessage.class);

            System.out.println(npM.isYouAreFirst() ? "You are the first client" : "You are not the first client");

            this.graphic.setFirst(npM.isYouAreFirst()); //give the information to the graphic

            /*this.graphic.displayMessage(npM.isYouAreFirst() ? "You are the first client" : "You are not the first client"); //todo
            System.out.println("Displayed!");*/

            //ask for information about game only if needed and send this information to server waiting for answer that are correct
            answer = sendInfo(npM.isYouAreFirst(), temp);

            System.out.println("Sent correct info to server");

            IdMessage idM = gson.fromJson(answer, IdMessage.class);

            //finish the setup
            sendMessage(Errors.CLIENT_READY, Errors.CLIENT_READY.getDescription());

            System.out.println("Sent Client ready");

            //get the player id
            id = idM.getPlayerId();

            temp.clear();
            temp.add(Errors.LOBBY_DATA);


            System.out.println("Waiting for gameData");

            JsonElement gameData = waitForResponse(temp);

            LobbyInfoMessage data = gson.fromJson(gameData, LobbyInfoMessage.class);

            this.graphic.setGameData(data, id);

            System.out.println("GameData received and set");

        } catch (InterruptedException e) {
            System.out.println("Interrupted during setup of the connection at line: " + e.getStackTrace()[0].getLineNumber());
            return;
        }

        //create gameHandler
        this.game = new GameHandler(id, this.filteredIn, this.connection.getOutQueue(), this.graphic, this);

        setCode(Errors.SETUP_FINISHED);

        this.setup = null;
    }

    private JsonElement sendInfo (boolean first, Collection<Errors> coll) throws InterruptedException {
        JsonElement answer = null;
        coll.add(Errors.INFO_RECEIVED); // all info are right
        coll.add(Errors.WIZARD_NOT_AVAILABLE);
        coll.add(Errors.USERNAME_NOT_AVAILABLE);
        if (first) {
            coll.add(Errors.NUM_OF_PLAYER_ERROR);
            coll.add(Errors.WRONG_GAME_MODE);
        }
        boolean ok = false;
        String error;

        if (this.setup.isInterrupted())
            throw new InterruptedException("Client Setup: interrupted");

        while (!ok) {

            JsonElement m = getInfo(first);

            if (this.setup.isInterrupted())
                throw new InterruptedException("Client Setup: interrupted");

            sendMessage(m);
            answer = waitForResponse(coll);
            Message temp = this.gson.fromJson(answer, Message.class);

            if (Errors.INFO_RECEIVED.equals(temp.getError())) {
                ok = true;
                this.graphic.setDone(true, null);
            } else {
                error = temp.getMessage();
                this.graphic.setDone(false, error);
            }
        }
        return answer;
    }

    private JsonElement getInfo (boolean first) throws InterruptedException {

        DataCollector dC = this.graphic.getDataCollector();


        if (this.setup.isInterrupted())
            throw new InterruptedException("Client Setup: interrupted");
        String username = dC.getUsername();
        System.out.println("Your username is: " + username);
        if (this.setup.isInterrupted())
            throw new InterruptedException("Client Setup: interrupted");
        Wizard w = dC.getWizard();
        System.out.println("Your wizard is: " + w);
        if (this.setup.isInterrupted())
            throw new InterruptedException("Client Setup: interrupted");


        if (first){
            int numOfPlayer = dC.getNumOfPlayers();
            System.out.println("num of player is: " + numOfPlayer);
            if (this.setup.isInterrupted())
                throw new InterruptedException("Client Setup: interrupted");
            int gameMode = dC.getGameMode();
            System.out.println("gamemode is: " + gameMode);
            FirstPlayerMessage m = new FirstPlayerMessage(Errors.FIRST_CLIENT.getDescription(), username, w, numOfPlayer, gameMode);
            return gson.toJsonTree(m);
        }

        NotFirstPlayerMessage m = new NotFirstPlayerMessage(Errors.NOT_FIRST_CLIENT.getDescription(), username, w);
        return gson.toJsonTree(m);
    }

    private void sendMessage (Errors codeMessage, String desc) throws InterruptedException {
        Message m = new Message(codeMessage, desc);
        sendMessage(this.gson.toJsonTree(m));
    }

    private void sendMessage (JsonElement json) throws InterruptedException {
        this.connection.getOutQueue().put(json);
    }

    private JsonElement waitForResponse(Collection<Errors> corrects) throws InterruptedException {

        JsonElement message = null;
        Errors codeReceived = null;
        while (!corrects.contains(codeReceived)){

            message = this.filteredIn.take();

            Message temp = this.gson.fromJson(message, Message.class);
            codeReceived = temp.getError();
        }
        return message;
    }

    //test method

    public static void main(String[] args){
        GraphicHandler gH = new GraphicHandler("Cli");
        gH.startGraphic();
        Client client = new Client (gH, "127.0.0.1", 5088);
        client.start();
    }

}

