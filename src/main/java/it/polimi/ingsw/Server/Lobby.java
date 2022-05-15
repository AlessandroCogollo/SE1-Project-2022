package it.polimi.ingsw.Server;

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
    private final ArrayList<Integer> ids;
    private final ArrayList<String> usernames  = null;
    private int numOfPlayers = 0;
    private int gameMode;
    private final ExecutorService exec;
    public Lobby(int port, Server server) {
        this.ids = null;
        this.server = null;
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
        for (int i = 1; i < numOfPlayers; i++) {
            try {
                client = this.serverSocket.accept();
                exec.execute(new ClientHandler(this.server, this.serverSocket, client, i, this));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        int[] ids = this.ids.stream().mapToInt(i -> i).toArray();
        this.server.setGameProperties(ids, this.gameMode);
    }

    public void shutDownLobby() {
        this.exec.shutdownNow();
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

    public void SetOk(int id, String username) {
        if (id == 0) {
            this.isFirstConnected = true;
        }
        ids.add(id);
        this.usernames.add(username);
    }
}
