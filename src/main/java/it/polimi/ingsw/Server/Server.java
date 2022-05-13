package it.polimi.ingsw.Server;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server
{
    private ServerSocket server = null;
    private int numOfPlayers = 0;
    private ArrayList<Integer> ids;


    public Server(int port) {

        Lobby l = new Lobby(port, this);
        //just for testing

        try {
            server = new ServerSocket(port);
            Socket client = server.accept();
            Thread t = new Thread(new ClientHandler(this, client, 0, l));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public ServerSocket getServer() {
        return server;
    }

    void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    void addId(int id){
        ids.add(id);
    }

    public ArrayList<Integer> getIds() {
        return new ArrayList<>(this.ids);
    }

    public static void main(String[] args) {
        int port = 5088; //used for testing
        /*
        if(args.length != 1)
            System.out.println("Error missing arguments");
        else
            port = Integer.parseInt(args[0]);

         */
        //todo add a method to stop server
        //deactivate for pass auto test
        new Server(port);
    }

}
