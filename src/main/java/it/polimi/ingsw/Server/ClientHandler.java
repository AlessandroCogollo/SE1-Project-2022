package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;

import java.io.*;
import java.net.InetAddress;
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
    private String username;
    private Lobby l;
    private Wizard wizard;
    Gson gson;
    public ClientHandler (Server s, ServerSocket server, Socket client, int id, Lobby l){
        this.s = s;
        this.server = server;
        this.client = client;
        this.id = id;
        this.l = l;
        this.gson = new Gson();
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
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ( (line = in.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            String content = sb.toString();
            System.out.println(content);
            String json = this.gson.toJson(content);
            Message m = this.gson.fromJson(json, Message.class);
            return m;
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void Send(Message m){
        String json = this.gson.toJson(m);
        out.println(json);
    }

    @Override
    public void run() {

        System.out.println("Client accepted");

        try {

            ReceiveFirstMessage();

            SendFirstMessage();

            if (this.id == 0) {
                ReceiveFirstPlayerData();
            } else {
                ReceiveData();
            }

            SendId();

            ReceiveConfirm();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        l.SetOk(this.id, this.username, this.wizard);
        
        Ping ping = new Ping(this);
        new Thread(ping).start();

    }

    void ReceiveFirstMessage() throws IOException, ClassNotFoundException {
        Message m = readJson();
        System.out.println(m.getMessage());

    }

    void SendFirstMessage() throws IOException {
        Send(new NewPlayerMessage("Welcome!", this.id == 0));
    }

    void ReceiveFirstPlayerData(){
        FirstPlayerMessage m = (FirstPlayerMessage) readJson();
        this.username = m.getUsername();
        this.wizard = m.getWizard();
        while(m.getNumOfPlayer() < 2 || m.getNumOfPlayer()< 4 || m.getGameMode() < 0 || m.getGameMode() < 1){
            if(m.getNumOfPlayer() < 2 || m.getNumOfPlayer()< 4){
                Send(new Message(Errors.NUM_OF_PLAYER_ERROR, "Please select a valid number (2-4): "));
            }
            else if(m.getGameMode() < 0 || m.getGameMode() < 1){
                Send(new Message(Errors.WRONG_GAME_MODE, "Please select a valid game mode (0-1): "));
            }
            m = (FirstPlayerMessage) readJson();
        }
        this.l.setParameters(m.getNumOfPlayer(), m.getGameMode());
    }
    
    void ReceiveData(){
        NotFirstPlayerMessage m = (NotFirstPlayerMessage) readJson();
        while(l.getUsernames().contains(m.getUsername()) || l.getWizards().contains(m.getWizard())){
            if(l.getUsernames().contains(m.getUsername()) ){
                Send(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            }
            else if(l.getWizards().contains(m.getWizard())){
                Send(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            }
            m = (NotFirstPlayerMessage) readJson();
        }
        this.username = m.getUsername();
        this.wizard = m.getWizard();
    }

    void SendId(){
        Send(new IdMessage("Your id is: ", this.id));
    }

    void ReceiveConfirm(){
        Message m = readJson();
        System.out.println(m.getError() + ", " + m.getMessage());
    }

    public String getUsername() {
        return this.username;
    }

    public int getId() {
        return id;
    }

    void stop(){
        try {
            in.close();
            out.close();
            client.close();
            //server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClient() {
        return client;
    }
}

class Ping implements Runnable{

    private ClientHandler ch;

    public Ping(ClientHandler ch){
        this.ch = ch;
    }

    @Override
    public void run() {
        Socket client = ch.getClient();
        InetAddress inet = client.getInetAddress();
        boolean isReachable;

        while(true){
            try {
                isReachable = inet.isReachable(5000);
                if(isReachable){
                    System.out.println("Host is Reachable");
                }
                else{
                    System.out.println("Host is not reachable. Closing connection...");
                    ch.stop();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
