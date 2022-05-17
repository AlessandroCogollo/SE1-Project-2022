package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.ConnectionHandler;
import it.polimi.ingsw.Enum.Wizard;

import org.jetbrains.annotations.VisibleForTesting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby implements Runnable {

    private final Server server;
    private final ServerSocket serverSocket;
    private boolean isFirstConnected = false;
    private ArrayList<Integer> ids;
    private ArrayList<String> usernames  = null;
    private final ArrayList<Wizard> wizards  = null;
    private int numOfPlayers = 0;
    private int gameMode;
    private ExecutorService exec;
    public Lobby(int port, Server server) {
        this.ids = null;
        this.server = null;
        this.ids = new ArrayList<>();
        this.usernames = new ArrayList<>();
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
        while (!isFirstConnected) {
            try {
                client = serverSocket.accept();
                ClientHandler c = new ClientHandler(this.server, this.serverSocket, client, 0, this);
                exec.execute(c);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        int i = 1;
        while (i < numOfPlayers) {
            try {
                client = this.serverSocket.accept();
                if (client != null) {
                    exec.execute(new ClientHandler(this.server, this.serverSocket, client, i, this));
                    i++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        int[] ids = this.ids.stream().mapToInt(l -> l).toArray();
        this.server.setGameProperties(ids, this.gameMode);
    }

    @VisibleForTesting boolean shutDownLobby() {
        this.exec.shutdownNow();
        return exec.isTerminated();
    }

    /*
    public void setNumOfPlayers(int num) {
        this.numOfPlayers = num;
    }
    */

    // stands for setNumOfPlayers (temporarily)
    public void setParameters(int num, int gameMode) {
        this.numOfPlayers = num;
        this.gameMode = gameMode;
    }

    public void SetOk(int id, String username, Wizard w) {
        if (id == 0) {
            this.isFirstConnected = true;
        }
        this.ids.add(id);
        this.usernames.add(username);
        this.wizards.add(w);
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

    @VisibleForTesting ArrayList<String> getUsernames() {
        return this.usernames;
    }

    @VisibleForTesting ArrayList<Wizard> getWizards() {
        return this.wizards;
    }
}
