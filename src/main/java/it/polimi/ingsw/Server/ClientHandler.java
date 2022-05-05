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
    private int myId = -1;

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

        idRequest();

        Parrot();
    }

    void firstClient(){
        String line = "";
        System.out.println("Sending numOfPlayers request...");
        out.println("How many players will the game be composed of?");

        int num = -1;
        while(num == -1){
            try{
                line = read();
                System.out.println("Received: " + line);
                num = Integer.parseInt(line);
                if(num<2 || num>4){
                    out.println("Please select a valid number (2-4): ");
                    num = -1;
                }
            }catch(Exception e){
                out.println("Please select a valid number (2-4): ");
                num = -1;
            }
        }

        this.s.setNumOfPlayers(num);
        out.println("Creating a game composed by " + this.s.getNumOfPlayers() + " players...");
    }

    void idRequest(){
        String line = "";
        System.out.println("Sending id request...");
        out.println("What's your ID?");
        int id = -1;
        while(id == -1){
            try{
                line = read();
                System.out.println("Received: " + line);
                id = Integer.parseInt(line);
                while(s.getIds().contains(id)){
                    out.println("Id already in use, please choose another one: ");
                    line = read();
                    System.out.println("Received: " + line);
                    id = Integer.parseInt(line);
                }
            }catch(Exception ex){
                out.println("Please use a valid id: ");
                id = -1;
            }
        }
        s.addId(id);
        this.myId = id;
        out.println("Your id is: " + id);

    }

    void Parrot(){
        String line = "";

        // reads message from client and send it back until "END" is sent
        while (!line.equals("END")) {
            System.out.println("Listening...");
            line = read();
            System.out.println("#" + client.getPort() + " --> Received : " + line);
            out.println(line);


            if(line.equals("ping")){
                System.out.println("PONG --> #" + port);
                out.println("your ip: " + client.getInetAddress().getHostAddress() + ", your port: " + client.getPort() + ", my ip: " + client.getLocalAddress() + ", my port: " + client.getLocalPort() + ", my socket address: " + client.getLocalSocketAddress() + ", are you connected? " + client.isConnected());
            }

        }
        System.out.println("Closing connection");
        stop();
    }

    String read(){
        String line = "";
        try {
            line = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Stopping service...");
            stop();
        }
        return line;

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
