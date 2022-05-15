package it.polimi.ingsw.Server;
import it.polimi.ingsw.Enum.Errors;

/**
 * Main class of the Server
 */
public class Server {

    private final Lobby lobby;
    private int[] ids;
    private int gameMode;
    private QueueOrganizer queueOrganizer;
    private ModelHandler model;
    private Errors code;
    private final Object lock;

    /**
     * Constructor of the Server, set all his parameter
     * @param port passed to the lobby for create the server socket
     */
    public Server (int port){
        //create lobby
        this.lobby = new Lobby(port, this);

        //set the placeholder for information
        this.ids = null;
        this.gameMode = -1;
        this.queueOrganizer = null;
        this.model = null;

        //set the lock for thread wait
        this.code = Errors.NOTHING_TODO;
        this.lock = new Object();
    }

    /**
     * Start the main Thread of the server
     */
    public void start (){

        new Thread(lobby).start();

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

        //came here only when the server has to shutdown
        shutdownAll();
    }

    private void shutdownAll() {
        // stop model thread
        if (this.model != null && this.model.getHasToRun()){
            this.model.stopModel();
        }
        if (this.lobby != null) {
            this.lobby.shutDownLobby();
        }
    }

    //this method is invoked any time someone modify the code, so the server know what to do.
    private boolean doSomething() {
        //return false only when the Server needs to shut down otherwise return always true
        if (this.code.equals(Errors.PLAYER_DISCONNECTED) || this.code.equals(Errors.GAME_OVER)){
            //todo handling of this codes

            return false;
        }

        //all other possible code
        switch (this.code){
            case CREATE_MODEL -> startGame();
        }

        //reset code
        this.code = Errors.NOTHING_TODO;
        return true;
    }

    private void startGame() {
        if (this.model == null) {
            this.queueOrganizer = new QueueOrganizer(this.ids);
            this.model = new ModelHandler(this.ids, this.gameMode, this, this.queueOrganizer);
            //todo give lobby queue organizer
        }
        if (!this.model.getHasToRun())
            new Thread(this.model).start();
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
     * Used only in testing for start the server without have to write the parameter
     * @param args standard main args param
     */
    public static void main(String[] args) {
        Server server = new Server(5088);
        server.start();
    }
}
