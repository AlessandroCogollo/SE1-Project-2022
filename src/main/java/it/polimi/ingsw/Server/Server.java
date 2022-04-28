package it.polimi.ingsw.Server;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server
{

    private int portA, portB, portC, portD;


    public Server(int port, int numOfPlayers) {
        this.portA = port;
        this.portB = port +1;
        this.portC = port +2;
        this.portD = port +3;
        Runnable taskA = () -> {
            ClientHandler(this.portA);
        };
        Runnable taskB = () -> {
            ClientHandler(this.portB);
        };
        Runnable taskC = () -> {
            ClientHandler(this.portC);
        };
        Runnable taskD = () -> {
            ClientHandler(this.portD);
        };
        // starts server and waits for a connection
        Executor e = Executors.newFixedThreadPool(numOfPlayers);
        e.execute(taskA);
        e.execute(taskB);
        if(numOfPlayers>2){
            e.execute(taskC);
            if (numOfPlayers>3){
                e.execute(taskD);
            }
        }
    }

    void ClientHandler(int port){
        ServerSocket server = null;
        Socket client = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            System.out.println("Trying to start a server on port " + port);
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for a client ...");
            client = server.accept();
            System.out.println("Client accepted on port " + port);

            ArrayList<String> commands = new ArrayList<>();
            commands.add("ping");


            // takes input from the client socket
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(
                    client.getOutputStream(), true);

            String line = "";

            // reads message from client until "END" is sent
            line = in.readLine();
            while (!line.equals("END")) {
                try {
                    System.out.println("#" + port + " --> Received : " + line + ", Listening...");
                    line = sendMessage(line, in, out);

                    if(line.equals("ping")){
                        System.out.println("PONG --> #" + port);
                        line = sendMessage(("port : " + port), in, out);
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
                    System.out.println("Closing connection by client with port : " + port);
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


    String sendMessage(String msg, BufferedReader in, PrintWriter out) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    void stop(BufferedReader in, PrintWriter out, Socket client, ServerSocket server) throws IOException {
        in.close();
        out.close();
        client.close();
        server.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(5010, 2);
    }

}
