package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.GraphicInterface.Cli;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;

import it.polimi.ingsw.Message.Message;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that manage all the client handler
 */
public class Lobby implements Runnable {

    private final Server server;
    private final ServerSocket serverSocket;
    private final ArrayList<Integer> ids = new ArrayList<>();
    private final Map <Integer, String> usernames  = new HashMap<>();
    private final Map <Integer, Wizard> wizards  = new HashMap<>();
    private final Map <Integer, ClientHandler> abstractClients = new HashMap<>();

    private final ExecutorService main = Executors.newSingleThreadExecutor();

    private final Object lock = new Object();

    private int numOfPlayers = -1;
    private int gameMode = -1;
    private QueueOrganizer queues = null;

    /**
     * Constructor
     * @param port the port of the server where accept the clients
     * @param server The main server class, used to send info
     */
    public Lobby(int port, Server server) {
        this.server = server;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Server socket error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Used by ClientHandler when received the data from the first player
     * @param num number of player of this game
     * @param gameMode game mode of the game
     */
    public void setParameters(int num, int gameMode) {
        synchronized (this.lock) {
            this.numOfPlayers = num;
            this.gameMode = gameMode;
            this.lock.notifyAll();
        }
    }

    /**
     * Used by the Main server for start the real game
     * @param queues Queues connecting all the players and the model
     */
    public void setQueues(QueueOrganizer queues) {
        synchronized (this.lock) {
            this.queues = queues;
            this.lock.notifyAll();
        }
    }

    /**
     * Used by connection Handler to set that his client is ready for the start of the game
     * @param id id of player ready
     * @param username username of the player
     * @param w wizard of the player
     */
    public void SetOk(int id, String username, Wizard w) {
        synchronized (this.lock) {
            this.usernames.put(id, username);
            this.wizards.put(id, w);
            this.lock.notifyAll();
        }
    }

    /**
     * Called by the connection handler when the clients disconnected
     * @param id the id of the player disconnected
     */
    public void clientDown (int id){
        String errorMessage = "The player " + this.usernames.get(id) + " with id " + id + "disconnected. The game will be stopped.";
        System.out.println("Lobby: " + errorMessage);

        sendDisconnectedMessage (errorMessage);

        this.server.setCode(Errors.PLAYER_DISCONNECTED);
    }

    /**
     * Method for stop the main thread of Lobby and all sub thread, this includes all clientHandler already created
     */
    public void shutDownLobby() {
        try {
            this.serverSocket.close();
        } catch (IOException ignored){}
        this.main.shutdownNow();
        for (Integer id: this.ids){
            this.abstractClients.get(id).shutdown();
        }
    }

    /**
     * Used by the client handler for check for not repeated usernames
     * @return the map between the id and username
     */
    public Map<Integer, String> getUsernames() {
        return usernames;
    }

    /**
     * Used by the client handler for check for not repeated wizards
     * @return the map between the id and wizards
     */
    public Map<Integer, Wizard> getWizards() {
        return wizards;
    }

    /**
     * First wait for the first client and the data of the game.
     * When it receives them it will start to accept other player.
     * When all the player are connected communicates to the main server thread to star the game.
     */
    @Override
    public void run() {
        this.main.execute(this::main);
    }

    private void sendDisconnectedMessage(String errorMessage) {
        Message m = new Message(Errors.PLAYER_DISCONNECTED, errorMessage);
        JsonElement mJ = new Gson().toJsonTree(m);
        for (Integer id: this.ids){
            boolean done = false;
            while (!done){
                try {
                    this.queues.getPlayerQueue(id).put(mJ);
                } catch (InterruptedException e) {
                    System.err.println("Error interrupted while sending last message to player " + id + " Lobby line: " + new Throwable().getStackTrace()[0].getLineNumber());
                    continue;
                }
                done = true;
            }

        }
    }


    //thread method
    private void main (){
        System.out.println("Lobby: " + "Waiting for First Client connected");
        int i = 0;
        Socket client;
        ClientHandler temp;
        try {
            client = this.serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Lobby: Error connecting to client line: " + new Throwable().getStackTrace()[0].getLineNumber());
            return;
        }

        System.out.println("Lobby: " + "First Client connected");
        temp = new ClientHandler(client, i, this);
        new Thread(temp).start();
        this.abstractClients.put(0, temp);
        this.ids.add(0);

        System.out.println("Lobby: " + "Waiting for first player to send data");
        synchronized (this.lock) {
            while (this.numOfPlayers < 2 || this.numOfPlayers > 4 || gameMode < 0 || gameMode > 1){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Lobby:  receiving data from client line: " + new Throwable().getStackTrace()[0].getLineNumber());
                    return;
                }
            }
        }

        i++;

        while (i < this.numOfPlayers) {
            System.out.println("Lobby: " + "Waiting for other " + (this.numOfPlayers - i) + " players");
            try {
                client = this.serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Lobby: Error connecting to client line: " + new Throwable().getStackTrace()[0].getLineNumber());
                return;
            }
            System.out.println("Lobby: " + "New Player Connected");
            temp = new ClientHandler(client, i, this);
            new Thread(temp).start();
            this.abstractClients.put(i, temp);
            this.ids.add(i);
            i++;
        }

        System.out.println("Lobby: " + "Waiting for all players finish setup");

        synchronized (this.lock) {
            while (this.usernames.size() < this.numOfPlayers || this.wizards.size() < this.numOfPlayers){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Error wait interrupt while waiting for the all players to finish their setup line: " + new Throwable().getStackTrace()[0].getLineNumber());
                    return;
                }
            }
        }

        System.out.println("Lobby: " + "All players finished setup");

        int[] ids = this.ids.stream().mapToInt(l -> l).toArray();
        this.server.setGameProperties(ids, this.gameMode);

        synchronized (this.lock) {
            while (this.queues == null){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Error wait interrupt while waiting for the start of the game line: " + new Throwable().getStackTrace()[0].getLineNumber());
                    return;
                }
            }
        }

        System.out.println("Lobby: " + "Setting all player handler to start the game");

        for (Integer id: this.ids){
            this.abstractClients.get(id).setQueues(this.queues.getModelQueue(), this.queues.getPlayerQueue(id));
        }
        //At this point the lobby is only useful for stop all the connection handler and for some method but his main thread can stop
    }
}
