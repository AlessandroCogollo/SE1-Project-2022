package it.polimi.ingsw.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Cli;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Network.ConnectionHandler;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * Main Client class
 */

//todo add player disconnected to stop the game
public class Client{

    private final Graphic graphic;
    private final ConnectionHandler connection;
    private final Gson gson = new GsonBuilder().create();

    private ExecutorService executors = null;

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
    public Client (Graphic graphic, String serverIp, int serverPort){
        this( graphic, serverIp, serverPort, Duration.ofSeconds(60));
    }

    /**
     * Start a client that connects to server with coordination given and use the given Interface to display information, the ping timer is set to the given timeout
     * @param graphic Interface for display information to player
     * @param serverHost ip of server
     * @param serverPort port of server
     * @param defaultTimeout timeout for ping
     */
    public Client(Graphic graphic, String serverHost, int serverPort, Duration defaultTimeout) {
        this.graphic = graphic;
        try {
            this.connection = new ConnectionHandler(serverHost, serverPort, defaultTimeout, this::serverDown);
        } catch (IOException e) {
            e.printStackTrace();
            graphic.displayMessage("Error connecting to " + serverHost + " at " + serverPort);
            throw new RuntimeException("Error connecting to " + serverHost + " at " + serverPort);
        }
    }


    /**
     * Ask the player what type of interface he prefers, CLi or Gui
     * @return the chosen interface
     */
    public static Graphic askGraphic() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        while ((!"Cli".equals(s) && !"Gui".equals(s))){
            try {
                s = input.readLine();
            } catch (IOException ignored) {}
        }
        return "Cli".equals(s) ? new Cli() : new Gui();
    }

    /**
     * Start the main thread of client
     */
    public void start() {

        new Thread(this.connection).start();

        this.executors = Executors.newSingleThreadExecutor();
        executors.execute(this::setupConnectionAndStartGame);


        //do nothing until some other thread tells him what to do with a code

        synchronized(this.lock) {
            while(doSomething()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Client main Thread interrupted");
                    this.code = Errors.GAME_OVER; //only possible in tests
                }
            }
        }
        this.graphic.displayMessage("Client is shutting down");
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




    private void shutdownAll() {
        executors.shutdownNow();
        this.connection.stopConnectionHandler();
        if (this.game != null)
            this.game.stopGameHandler();
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
                this.graphic.displayMessage("Ready to play, waiting for the start of the game");
                new Thread(this.game).start();
            }
            case SERVER_DOWN -> {
                this.graphic.displayMessage("The server go down, shutting down");
                go = false;
            }
            case GAME_OVER -> {
                this.graphic.displayMessage("The game is finished, shutting down");
                go = false;
            }
        }

        //reset code
        this.code = Errors.NOTHING_TODO;
        return go;
    }


    //method for set the initial connection

    private void setupConnectionAndStartGame() {
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

            this.graphic.displayMessage(npM.isYouAreFirst() ? "You are the first client" : "You are not the first client");

            //ask for information about game only if needed and send this information to server waiting for answer that are correct
            answer = sendInfo(npM.isYouAreFirst(), temp);

            System.out.println("Sent correct info to server");

            IdMessage idM = gson.fromJson(answer, IdMessage.class);

            //finish the setup
            sendMessage(Errors.CLIENT_READY, Errors.CLIENT_READY.getDescription());

            System.out.println("Sent Client ready");

            //get the player id
            id = idM.getPlayerId();
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            //this.setCode(Errors.SERVER_NOT_RESPONDING);
            return;
        }

        //create gameHandler
        this.game = new GameHandler(id, this.connection, this.graphic, this);

        setCode(Errors.SETUP_FINISHED);
    }

    private JsonElement sendInfo (boolean first, Collection<Errors> coll) throws IOException, InterruptedException {
        JsonElement answer = null;
        coll.add(Errors.INFO_RECEIVED); // all info are right
        coll.add(Errors.WIZARD_NOT_AVAILABLE);
        coll.add(Errors.USERNAME_NOT_AVAILABLE);
        if (first) {
            coll.add(Errors.NUM_OF_PLAYER_ERROR);
            coll.add(Errors.WRONG_GAME_MODE);
        }
        boolean ok = false;
        String error = null;
        while (!ok) {
            JsonElement m = getInfo(error, first);
            sendMessage(m);
            answer = waitForResponse(coll);
            Message temp = this.gson.fromJson(answer, Message.class);
            if (Errors.INFO_RECEIVED.equals(temp.getError())) {
                ok = true;
            } else {
                error = temp.getMessage();
            }
        }
        return answer;
    }

    private JsonElement getInfo (@Nullable String message, boolean first) throws IOException, InterruptedException {
        if (message != null)
            this.graphic.displayMessage(message);

        String username = this.graphic.getUsername();
        Wizard w = this.graphic.getWizard();


        if (first){
            int numOfPlayer = this.graphic.getNumOfPLayer();
            int gameMode = this.graphic.getGameMode();
            FirstPlayerMessage m = new FirstPlayerMessage(Errors.FIRST_CLIENT.getDescription(), username, w, numOfPlayer, gameMode);
            return gson.toJsonTree(m);
        }

        NotFirstPlayerMessage m = new NotFirstPlayerMessage(Errors.NOT_FIRST_CLIENT.getDescription(), username, w);
        return gson.toJsonTree(m);
    }

    private void sendMessage (Errors codeMessage, String desc) {
        Message m = new Message(codeMessage, desc);
        sendMessage(this.gson.toJsonTree(m));
    }

    private void sendMessage (JsonElement json){
        try {
            this.connection.getQueueToServer().put(json);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //private JsonElement waitForResponse(Collection<Errors> corrects, Duration timeout) throws TimeoutException, InterruptedException
    private JsonElement waitForResponse(Collection<Errors> corrects) throws InterruptedException {

        JsonElement message = null;
        Errors codeReceived = null;
        while (!corrects.contains(codeReceived)){
            /*try{
                message = getAnswer(timeout);
            } catch (TimeoutException e) {
                e.printStackTrace();
                StringBuilder erString = null;
                for (Errors er: corrects){
                    if (erString == null)
                        erString = new StringBuilder(er.toString() + " ");
                    else
                        erString.append(er.toString()).append(" ");
                }
                throw new TimeoutException("Timer over while waiting for " + erString);
            }*/

            message = this.connection.getQueueFromServer().take();

            Message temp = this.gson.fromJson(message, Message.class);
            codeReceived = temp.getError();
        }
        return message;
    }

    /*
    private JsonElement getAnswer (Duration timeout) throws TimeoutException {

        JsonElement message = null;
        try {
            message = this.connection.getQueueFromServer().poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.graphic.displayMessage("Server went down");
        }

        if (message == null)
            throw new TimeoutException();

        return message;
    }
    */

    //test method

    public static void main(String[] args){
        Client client = new Client (new Cli(), "127.0.0.1", 5088);
        client.start();
    }

}

