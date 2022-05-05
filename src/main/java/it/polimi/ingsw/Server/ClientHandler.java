package it.polimi.ingsw.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Server s;
    private Socket client = null;
    private ServerSocket server = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private int port = 0;

    public ClientHandler(Server s, Socket client){
        this.client = client;
        this.server = s.getServer();
        this.port = s.getPort();
        this.s = s;
        try {
            this.in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            this.out = new PrintWriter(
                    client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        System.out.println("Client accepted");

        out.println("Hi");


        if(!s.isFirstConnected()){
            firstClient();
        }

        String line = "";

        // reads message from client and send it back until "END" is sent
        while (!line.equals("END")) {
            try {
                System.out.println("Listening...");
                line = in.readLine();
                System.out.println("#" + client.getPort() + " --> Received : " + line);
                out.println(line);


                if(line.equals("ping")){
                    System.out.println("PONG --> #" + port);
                    out.println("your ip: " + client.getInetAddress().getHostAddress() + ", your port: " + client.getPort() + ", my ip: " + client.getLocalAddress() + ", my port: " + client.getLocalPort() + ", my socket address: " + client.getLocalSocketAddress() + ", are you connected? " + client.isConnected());
                }

            } catch (IOException i) {
                System.out.println(i);
                System.out.println("Closing connection");
                stop();
                break;
            }
        }
        if(line.equals("END")){
            System.out.println("Closing connection");
            stop();
        }
    }

    void firstClient(){
        String line = "";
        System.out.println("Sending numOfPlayers request...");
        out.println("How many players will the game be composed of?");
        try {
            line = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
        System.out.println("Received: " + line);
        this.s.setNumOfPlayers(Integer.parseInt(line));
        out.println("Creating a game composed by " + this.s.getNumOfPlayers() + " players...");
    }

    void stop(){
        try {
            in.close();
            out.close();
            client.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
