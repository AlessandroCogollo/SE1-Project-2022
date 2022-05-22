package it.polimi.ingsw.Server;
import it.polimi.ingsw.Enum.Errors;
import org.jetbrains.annotations.VisibleForTesting;

import javax.swing.*;

/**
 * Main class of the Server
 */
public class Server {

    private final Lobby lobby;

    private final Object lock = new Object();

    private int[] ids = null;
    private int gameMode = -1;
    private QueueOrganizer queueOrganizer = null;
    private ModelHandler model = null;
    private Errors code = Errors.NOTHING_TODO;


    /**
     * Constructor of the Server, set all his parameter
     * @param port passed to the lobby for create the server socket
     */
    public Server (int port){
        //create lobby
        this.lobby = new Lobby(port, this);
    }

    /**
     * Set the code of the server and notify it that there is something to do
     * @param er the error that notify something to the server
     */
    //meant to be used only outside the server class
    public void setCode (Errors er){
        synchronized(this.lock) {
            this.code = er;
            this.lock.notifyAll();
        }
    }

    /**
     * Used to set the game properties from the lobby after all the players have connected
     * @param ids ids of players
     * @param gameMode game mode of the game
     */
    //meant to be used only outside the server class
    public void setGameProperties (int[] ids, int gameMode){
        this.ids = ids;
        this.gameMode = gameMode;
        setCode(Errors.CREATE_MODEL);
    }

    /**
     * Start the main Thread of the server
     */
    public void start (){
        Thread.currentThread().setName("Main Server Thread");
        System.out.println("Main Server: " + "Server started");

        new Thread(this.lobby, "Lobby").start();

        synchronized(this.lock) {
            while(doSomething()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                   System.err.println("Main Server: Thread interrupted, shutting down");
                   Thread.currentThread().interrupt(); //reset interrupted flag on the thread
                   break;
                }
            }
        }

        System.out.println("Main Server: " + "Server shutdown");
        //came here only when the server has to shutdown
        shutdownAll();
    }

    //this method is invoked any time someone modify the code, so the server know what to do.
    private boolean doSomething() {

        if (Thread.currentThread().isInterrupted()) {
            System.err.println("Main Server: Thread interrupted, shutting down");
            return false;
        }

        boolean go = true;

        switch (this.code){
            case CREATE_MODEL -> {
                System.out.println("Main Server: " + "Creating model");
                startGame();
            }
            case PLAYER_DISCONNECTED -> {
                //all message for the disconnection of the player are sent by the lobby, so the Main server has only to shut down itself
                System.out.println("Main Server: " + "A player has been disconnected, shutting down the server");
                go = false;
            }
            case GAME_OVER -> {
                //the game is finished and the last model message is in the queues to the players, need only to wait some times and shut down the server
                System.out.println("Main Server: " + "The game has finished, shutting down the server");
                go = false;
            }
        }

        //return false only when the Server needs to be shut down otherwise return always true

        //reset code
        this.code = Errors.NOTHING_TODO;

        return go;
    }

    private void shutdownAll() {
        if (this.model != null){
           this.model.stopModel();
        }
        this.lobby.shutDownLobby();
    }

    private void startGame() {
        this.queueOrganizer = new QueueOrganizer(this.ids);

        this.model = new ModelHandler(this.ids, this.gameMode, this, this.queueOrganizer);
        new Thread(this.model, "Model").start();

        this.lobby.setQueues(this.queueOrganizer);
    }

    /**
     * Used only in testing for start the server without have to write the parameter
     * @param args standard main args param
     */
    public static void main(String[] args) {
        Server server = new Server(5088);
        server.start();
    }
}
