package it.polimi.ingsw.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Cli;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;

import java.time.Duration;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Main Client class
 */
public class Client{

    private final Graphic graphic;
    private final ConnectionHandler connection;
    private final Gson gson = new GsonBuilder().create();

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
        TimerTask er = new TimerTask() {
            private Client c;

            public TimerTask init (Client c){
                this.c = c;
                return this;
            }
            @Override
            public void run() {
                this.c.setCode(Errors.SERVER_DOWN);
            }
        }.init(this);
        this.connection = new ConnectionHandler(serverHost, serverPort, graphic, defaultTimeout, er);
    }

    /**
     * Ask the player what type of interface he prefers, CLi or Gui
     * @return the chosen interface
     */
    public static Graphic askGraphic() {

        //todo
        return null;
    }

    /**
     * Start the main thread of client
     */
    public void start() {

        new Thread(this.connection).start();

        int id = -1;
        //setup of connection between client and server
        try { //surround with try catch for wait for response timeout if the server go down during the setup of the connection of don't send the right message for too much time
            id = setupConnection();
        }catch (TimeoutException e) {
            graphic.displayMessage(e.getMessage());
        }

        this.game = new GameHandler(id, this.connection, this.graphic);

        this.graphic.displayMessage("Ready to play, waiting for the start of the game");

        new Thread(this.game).start();

        //do nothing until some other thread tells him what to do with a code

        synchronized(this.lock) {
            while(doSomething()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        //came here only when the client has to shutdown
        shutdownAll();
    }

    private void shutdownAll() {
        this.connection.stopConnectionHandler();
    }

    private boolean doSomething() {
        //return false only when the Server need to shutdown otherwise return always true
        if (this.code.equals(Errors.SERVER_DOWN) || this.code.equals(Errors.GAME_OVER)){
            //todo handling of this codes

            return false;
        }

        //reset code
        this.code = Errors.NOTHING_TODO;
        return true;
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

    private int setupConnection() throws TimeoutException{

        //this operation are done without the intervention of user and must be done in this order, so is not needed for have multiple thread (except for the timeout)

        //start the handbrake with server
        sendMessage(Errors.FIRST_MESSAGE_CLIENT, Errors.FIRST_MESSAGE_CLIENT.getDescription());

        //wait for the first answer from server
        JsonElement answer = waitForResponse(Errors.FIRST_MESSAGE_SERVER, Duration.ofSeconds(120)); //longer timeout for let server start
        NewPlayerMessage npM = gson.fromJson(answer, NewPlayerMessage.class);


        //ask for information about game only if needed
        JsonElement info;
        if (npM.isYouAreFirst())
            info = first();
        else
            info = notFirst();

        //send info to server
        sendMessage(info);

        answer = waitForResponse(Errors.INFO_RECEIVED, Duration.ofSeconds(60));
        IdMessage idM = gson.fromJson(answer, IdMessage.class);

        //finish the setup
        sendMessage(Errors.CLIENT_READY, Errors.CLIENT_READY.getDescription());

        return idM.getPlayerId();
    }

    private JsonElement notFirst() {
        Wizard w = this.graphic.getWizard();
        String username = this.graphic.getUsername();

        NotFirstPlayerMessage m = new NotFirstPlayerMessage(Errors.NOT_FIRST_CLIENT.getDescription(), username, w);
        return gson.toJsonTree(m);
    }

    private JsonElement first() {
        Wizard w = this.graphic.getWizard();
        String username = this.graphic.getUsername();
        int numOfPlayer = this.graphic.getNumOfPLayer();
        int gameMode = this.graphic.getGameMode();

        FirstPlayerMessage m = new FirstPlayerMessage(Errors.FIRST_MESSAGE_CLIENT.getDescription(), username, w, numOfPlayer, gameMode);
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

    private JsonElement waitForResponse(Errors correctCode, Duration timeout) throws TimeoutException{

        JsonElement message = null;
        int codeReceived = -1;
        int correct = correctCode.getCode();
        while (correct != codeReceived){
            try{
                message = getAnswer(timeout);
            } catch (TimeoutException e) {
                e.printStackTrace();
                throw new TimeoutException("Timer over while waiting for" + correctCode);
            }

            Message temp = this.gson.fromJson(message, Message.class);
            codeReceived = temp.getError().getCode();
        }
        return message;
    }

    private JsonElement getAnswer (Duration timeout) throws TimeoutException {

        JsonElement message;
        try {
            message = this.connection.getQueueFromServer().poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        if (message == null)
            throw new TimeoutException();

        return message;
    }

    public static void main(String[] args){
        Client client = new Client (new Cli(), "127.0.0.1", 5088);
        client.start();
    }

}

