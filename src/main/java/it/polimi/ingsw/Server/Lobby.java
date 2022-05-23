package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;

import it.polimi.ingsw.Message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private final Object lock = new Object();

    private Thread thread = null;

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
        ServerSocket s = null;
        try {
            s = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Lobby: error while creating serverSocket at this port: " + port);
            e.printStackTrace();
            System.exit(-1);
        }
        this.serverSocket = s;
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
        String errorMessage = "The player " + this.usernames.get(id) + " with id " + id + " disconnected. The game will be stopped.";
        System.out.println("Lobby: " + errorMessage);

        this.ids.remove((Integer)id);
        ClientHandler disc = this.abstractClients.get(id);
        disc.shutdown();
        this.abstractClients.remove(id, disc);

        sendDisconnectedMessage (errorMessage);
        this.server.setCode(Errors.PLAYER_DISCONNECTED);
    }

    /**
     * Method for stop the main thread of Lobby and all sub thread, this includes all clientHandler already created
     */
    public void shutDownLobby() {

        if (this.thread == null){
            System.out.println("Cannot shut down lobby if it is stopped yet");
            return;
        }

        //first try to stop the main thread of lobby with interrupt
        this.thread.interrupt();

        //assert that all message to player are sent
        boolean allDone = false;
        while (!allDone){
            allDone = true;
            for (Integer id: this.ids){
                if (!this.abstractClients.get(id).allMessageSent()){
                    allDone = false;
                    break;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }


        //then close all the abstract client started
        for (Integer id: this.ids){
            this.abstractClients.get(id).shutdown();
        }

        try {
            this.serverSocket.close();
        } catch (IOException e){
            System.err.println("Lobby: cannot close the serverSocket");
            e.printStackTrace();
            System.exit(-1);
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

        this.thread = Thread.currentThread();

        int i = 0;


        acceptNewClient(i, -1);

        if (this.thread.isInterrupted()) {
            System.out.println("Lobby: my main loop interrupted");
            return;
        }

        System.out.println("Lobby: waiting for first player to send data");
        synchronized (this.lock) {
            while (this.numOfPlayers < 2 || this.numOfPlayers > 4 || gameMode < 0 || gameMode > 1){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Lobby: interrupted while waiting data from first client line due to shutdown of the server");
                    return;
                }
            }
        }

        if (this.thread.isInterrupted()) {
            System.out.println("Lobby: my main loop interrupted");
            return;
        }

        i++;

        while (i < this.numOfPlayers) {
            acceptNewClient(i, this.numOfPlayers);
            if (this.thread.isInterrupted()) {
                System.out.println("Lobby: my main loop interrupted");
                return;
            }
            i++;
        }

        System.out.println("Lobby: waiting for all players finish setup");

        synchronized (this.lock) {
            while (this.usernames.size() < this.numOfPlayers || this.wizards.size() < this.numOfPlayers){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Lobby: interrupted while waiting for all clients ready");
                    return;
                }
            }
        }

        if (this.thread.isInterrupted()) {
            System.out.println("Lobby: my main loop interrupted");
            return;
        }

        System.out.println("Lobby: All players finished setup");

        int[] ids = this.ids.stream().mapToInt(l -> l).toArray();
        this.server.setGameProperties(ids, this.gameMode);

        synchronized (this.lock) {
            while (this.queues == null){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Lobby: interrupted while waiting for the start of the game");
                    return;
                }
            }
        }

        if (this.thread.isInterrupted()) {
            System.out.println("Lobby: my main loop interrupted");
            return;
        }

        System.out.println("Lobby: setting all player handler to start the game");

        for (Integer id: this.ids){
            this.abstractClients.get(id).setQueues(this.queues.getModelQueue(), this.queues.getPlayerQueue(id));
        }

        System.out.println("Main Thread Lobby: nothing else to do");
        //At this point the lobby is only useful for stop all the connection handler and for some method but his main thread can stop
    }

    private void acceptNewClient (int id, int maxPlayers){
        //no check over thread interrupted because this operation if the accept() succeed must be done
        Socket client;
        ClientHandler temp;
        System.out.println("Lobby: waiting for " + ((id == 0) ? "first" : ((Integer)(id + 1)).toString()) + " Client to connect" + ((id > 0) ? (" out of " + maxPlayers) : ""));
        try {
            client = this.serverSocket.accept();
        } catch (IOException e) {
            //when the server need to shutdown
            System.out.println("Lobby: ServerSocket closed while connecting to the " + ((id == 0) ? "first" : ((Integer)(id + 1)).toString()) + " client line due to the shutdown of the server");
            return;
        }

        System.out.println("Lobby: " + ((id == 0) ? "first" : ((Integer)(id + 1)).toString()) + " Client connected");
        temp = new ClientHandler(client, id, this);
        new Thread(temp, "ClientHandler " + id).start();
        this.abstractClients.put(id, temp);
        this.ids.add(id);
    }

    private void sendDisconnectedMessage(String errorMessage) {
        Message m = new Message(Errors.PLAYER_DISCONNECTED, errorMessage);
        JsonElement mJ = new Gson().toJsonTree(m);

        System.out.println("Lobby: sending player disconnected message");

        for (Integer id: this.ids){
            boolean done = false;
            while (!done){
                try {
                    this.abstractClients.get(id).sendMessage(mJ);
                    done = true;
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
