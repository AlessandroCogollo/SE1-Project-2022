package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Server s;
    private Socket client = null;
    private ServerSocket server = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private int port = 0;
    private int id;
    private Lobby l;
    public ClientHandler (Server s, ServerSocket server, Socket client, int id, Lobby l){
        this.s = s;
        this.server = server;
        this.client = client;
        this.id = id;
        this.l = l;
        try {
            this.in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            this.out = new PrintWriter(
                    client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Message readJson(){
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ( (line = in.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            String content = sb.toString();
            System.out.println(content);
            String json = gson.toJson(content);
            Message m = gson.fromJson(json, Message.class);
            return m;
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {

        System.out.println("Client accepted");

        try {

            ReceiveFirstMessage();

            SendFirstMessage();

            ReceiveData();

            SendId();

            ReceiveConfirm();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //l.SetOk(id);
        
        Ping ping = new Ping();
        new Thread(ping).start();

    }

    void ReceiveFirstMessage() throws IOException, ClassNotFoundException {
        Message m = readJson();
        System.out.println(m.getError() + ", " + m.getMessage());

    }

    void SendFirstMessage() throws IOException {
        Message m = new Message(Errors.NO_ERROR, "Welcome Client");
        //out.writeObject(m);
    }

    void ReceiveData(){

    }

    void SendId(){

    }

    void ReceiveConfirm(){

    }
    /*
    void firstMessageClient(){
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
    */
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

class Ping implements Runnable{

    @Override
    public void run() {

    }
}
