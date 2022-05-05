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


    public Server(int port) {
        this.port = port;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Executor e = Executors.newFixedThreadPool(4);
        Runnable task = () -> {
            try {
                ClientHandler(server.accept());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        e.execute(task);
        while(numOfPlayers == 0){
        }

        //now the client is connected: waiting for 60 seconds for the number of players
        if(this.numOfPlayers == 0){
            System.out.println("Timeout occurred: unspecified number of players.");
            System.out.println("Disconnecting...");
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{
            for(int i=0; i<numOfPlayers-1; i++){
                e.execute(task);
            }
        }

    }

    void ClientHandler(Socket client){

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(
                    client.getOutputStream(), true);
            out.println("Hi");

            String line = "";

            if(this.numOfPlayers == 0){
                System.out.println("Sending numOfPlayers request...");
                out.println("How many players will the game be composed of?");
                line = in.readLine();
                System.out.println("Received: " + line);
                this.numOfPlayers = Integer.parseInt(line);
                out.write("Creating a game composed by " + this.numOfPlayers + " players...");

            }
            // reads message from client until "END" is sent
            while (!line.equals("END")) {
                try {
                    System.out.println("Listening...");
                    line = in.readLine();
                    System.out.println("#" + port + " --> Received : " + line);
                    out.println(line);


                    if(line.equals("ping")){
                        System.out.println("PONG --> #" + port);
                        out.write("your ip: " + client.getInetAddress().getHostAddress() + ", your port: " + client.getPort() + ", my ip: " + client.getLocalAddress() + ", my port: " + client.getLocalPort() + ", my socket address: " + client.getLocalSocketAddress() + ", are you connected? " + client.isConnected());
                    }

                } catch (IOException i) {
                    System.out.println(i);
                    System.out.println("Closing connection");
                    stop(in, out, client, server);
                    break;
                }
            }
            if(line.equals("END")){
                try {
                    System.out.println("Closing connection");
                    stop(in, out, client, server);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException : Closing connection...");
            try {
                stop(in, out, client, server);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    void stop(BufferedReader in, PrintWriter out, Socket client, ServerSocket server) throws IOException {
        in.close();
        out.close();
        client.close();
        server.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(5088);
    }

}
