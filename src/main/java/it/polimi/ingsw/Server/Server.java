package it.polimi.ingsw.Server;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server
{

    private int port;
    private ServerSocket server = null;
    private int numOfPlayers = 0;
    private boolean isFirstConnected = false;
    private ArrayList<Integer> ids;


    public Server(int port) {

        ids = new ArrayList<>();
        this.port = port;
        Executor e = Executors.newFixedThreadPool(4);
        ClientHandler c = null;

        while(!isFirstConnected){
            try {
                server = new ServerSocket(port);
                Socket client = server.accept();
                c = new ClientHandler(this, client, 0);
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




        //now the client is connected: waiting (for 60 seconds) for the number of players
        if(this.numOfPlayers == -1){
            System.out.println("Timeout occurred: unspecified number of players.");
            System.out.println("Disconnecting...");
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{
            for(int i=1; i<numOfPlayers; i++){
                try {
                    e.execute(new ClientHandler(this, server.accept(), i));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public ServerSocket getServer() {
        return server;
    }

    public boolean isFirstConnected() {
        return isFirstConnected;
    }

    public int getPort() {
        return port;
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

    public static void main(String[] args) throws IOException {
        Server server = new Server(5088);
    }

}
