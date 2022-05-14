package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Lobby implements Runnable{

    private int port;
    private ServerSocket server = null;
    private int numOfPlayers = 0;
    private boolean isFirstConnected = false;
    private ArrayList<Integer> ids;
    private Server s;

    public Lobby(int port, Server s){
        ids = new ArrayList<>();
        this.port = port;
        this.s = s;
        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        ClientHandler c = null;
        Executor e = Executors.newFixedThreadPool(4);
        while(!isFirstConnected){
            try {
                Socket client = server.accept();
                c = new ClientHandler(s, server, client, 0, this);
                Thread t = new Thread(c);
                e.execute(c);
                //while the server is connected and has not decided the number of players
                while(numOfPlayers == 0 && client.isConnected()){}
                //if the player disconnected and hadn't decided the number of players yet, redo this cycle
                if(!client.isConnected()){
                    this.isFirstConnected = false;
                }
                else{
                    isFirstConnected = true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    void SetQueue(){

    }

}
