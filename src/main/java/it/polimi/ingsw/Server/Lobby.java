package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.GraphicInterface.Cli;
import it.polimi.ingsw.Enum.Wizard;

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

public class Lobby implements Runnable {

    private final Server server;
    private final ServerSocket serverSocket;
    private final ArrayList<Integer> ids = new ArrayList<>();
    private final Map <Integer, String> usernames  = new HashMap<>();
    private final Map <Integer, Wizard> wizards  = new HashMap<>();
    private final Map <Integer, ClientHandler> abstractClients = new HashMap<>();
    private final ExecutorService exec;
    private int numOfPlayers = -1;
    private int gameMode = -1;
    private QueueOrganizer queues = null;

    private final Object lock = new Object();
    public Lobby(int port, Server server) {
        this.server = server;
        this.exec = Executors.newFixedThreadPool(4);
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Socket client;
        try {
            System.out.println("Lobby: " + "Waiting for First Client connected");
            client = serverSocket.accept();
            System.out.println("Lobby: " + "First Client connected");
            ClientHandler c = new ClientHandler(this.server, client, 0, this);
            exec.execute(c);
            this.abstractClients.put(0, c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Lobby: " + "Waiting for first player to send data");
        synchronized (this.lock) {
            while (this.numOfPlayers < 2 || this.numOfPlayers > 4 || gameMode < 0 || gameMode > 1){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        int i = 1;
        while (i < numOfPlayers) {
            try {
                System.out.println("Lobby: " + "Waiting for other " + (this.numOfPlayers - i) + " players");
                client = this.serverSocket.accept();
                System.out.println("Lobby: " + "New Player Connected");
                if (client != null) {
                    ClientHandler c = new ClientHandler(this.server, client, i, this);
                    exec.execute(c);
                    this.abstractClients.put(i, c);
                    i++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        int[] ids = this.ids.stream().mapToInt(l -> l).toArray();
        this.server.setGameProperties(ids, this.gameMode);
        synchronized (this.lock) {
            while (this.queues == null){
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //todo give to all client handler the right queue
    }

    @VisibleForTesting boolean shutDownLobby() {
        this.exec.shutdownNow();
        return exec.isTerminated();
    }

    public void setParameters(int num, int gameMode) {
        synchronized (this.lock) {
            this.numOfPlayers = num;
            this.gameMode = gameMode;
            this.lock.notifyAll();
        }
    }

    public void setQueues(QueueOrganizer queues) {
        synchronized (this.lock) {
            this.queues = queues;
            this.lock.notifyAll();
        }
    }

    public void SetOk(int id, String username, Wizard w) {
        this.ids.add(id);
        this.usernames.put(id, username);
        this.wizards.put(id, w);
    }

    @VisibleForTesting int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    @VisibleForTesting int getGameMode() {
        return this.gameMode;
    }

    @VisibleForTesting ArrayList<Integer> getIds() {
        return this.ids;
    }

    @VisibleForTesting Map<Integer, String> getUsernames() {
        return usernames;
    }

    @VisibleForTesting Map<Integer, Wizard> getWizards() {
        return wizards;
    }
}
