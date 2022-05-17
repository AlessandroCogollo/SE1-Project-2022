package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Network.ConnectionHandler;

import javax.print.attribute.standard.NumberOfDocuments;

public class ClientHandler implements Runnable{
    private final Server s;
    private final Socket client;
    private final int id;
    private final Lobby l;
    private final ConnectionHandler net;
    private final Gson gson = new Gson();

    private String username;
    private Wizard wizard;

    public ClientHandler(Server s, Socket client, int id, Lobby l){
        this.s = s;
        this.client = client;
        this.id = id;
        this.l = l;
        this.net = new ConnectionHandler(client, Duration.ofSeconds(60), this::stop);
    }

    /**
     * Used only by the timer in connection handler if the client go down
     * @return always null for override call method of Callable
     */
    public Object stop (){
        this.net.stopConnectionHandler();
        //todo
        return null;
    }

    public Socket getClient() {
        return client;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public JsonElement readJson(){
        JsonElement J = null;
        while (J == null) {
            try {
                J = this.net.getQueueFromServer().poll(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("readJson method in Client handler interrupted");
            }
        }
        return J;
    }

    public void Send (JsonElement m){
        try {
            this.net.getQueueToServer().put(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("send method in Client handler interrupted");
        }
    }

    public void Send (Message m){
        Send(this.gson.toJsonTree(m));
    }

    @Override
    public void run() {

        new Thread(this.net).start();
        System.out.println("Client Handler Started id: " + this.id);

        ReceiveFirstMessage();

        System.out.println("ClientHandler: " + this.id + ". " + "Received first message from client");

        SendFirstMessage();

        System.out.println("ClientHandler: " + this.id + ". " + "Sent if client is first client");

        if (this.id == 0)
            ReceiveFirstPlayerData();
        else
            ReceiveData();

        System.out.println("ClientHandler: " + this.id + ". " + "Received data from client client");

        SendId();

        System.out.println("ClientHandler: " + this.id + ". " + "Sent Id");

        ReceiveConfirm();

        System.out.println("ClientHandler: " + this.id + ". " + "This client is ready");

        l.SetOk(this.id, this.username, this.wizard);

        //todo wait until lobby gives you the queues
    }

    void ReceiveFirstMessage() {
        Message m = null;
        while (m == null){
            JsonElement j = readJson();
            Message temp = this.gson.fromJson(j, Message.class);
            if (temp.getError().equals(Errors.FIRST_MESSAGE_CLIENT))
                m = temp;
        }
    }

    void SendFirstMessage() {
        Send(new NewPlayerMessage("Welcome!", this.id == 0));
    }

    void ReceiveFirstPlayerData(){
        boolean ok = false;
        int playerN = -1;
        int gamemode = -1;
        FirstPlayerMessage m = null;
        while (!ok) {
            JsonElement Jm = readJson();
            Message temp = this.gson.fromJson(Jm, Message.class);
            while (!temp.getError().equals(Errors.FIRST_CLIENT)) {
                Jm = readJson();
                temp = this.gson.fromJson(Jm, Message.class);
            }
            m = this.gson.fromJson(Jm, FirstPlayerMessage.class);
            playerN = m.getNumOfPlayer();
            gamemode = m.getGameMode();
            if (l.getUsernames().containsValue(m.getUsername())) {
                Send(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            } else if (l.getWizards().containsValue(m.getWizard())) {
                Send(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            } else if (playerN < 2 || playerN > 4){
                Send(new Message(Errors.NUM_OF_PLAYER_ERROR, "Please select a valid number (2-4): "));
            } else if (gamemode < 0 || gamemode > 1){
                Send(new Message(Errors.WRONG_GAME_MODE, "Please select a valid game mode (0-1): "));
            } else {
                ok = true;
            }
        }
        this.l.setParameters(playerN, gamemode);
        this.username = m.getUsername();
        this.wizard = m.getWizard();
    }
    
    void ReceiveData(){
        boolean ok = false;
        NotFirstPlayerMessage m = null;
        while (!ok) {
            JsonElement Jm = readJson();
            Message temp = this.gson.fromJson(Jm, Message.class);
            while (!temp.getError().equals(Errors.NOT_FIRST_CLIENT)) {
                Jm = readJson();
                temp = this.gson.fromJson(Jm, Message.class);
            }
            m = this.gson.fromJson(Jm, NotFirstPlayerMessage.class);
            if (l.getUsernames().containsValue(m.getUsername())) {
                Send(new Message(Errors.USERNAME_NOT_AVAILABLE, "Please select another username"));
            } else if (l.getWizards().containsValue(m.getWizard())) {
                Send(new Message(Errors.WIZARD_NOT_AVAILABLE, "Please select another wizard"));
            } else {
                ok = true;
            }
        }
        this.username = m.getUsername();
        this.wizard = m.getWizard();
    }

    void SendId(){
        Send(new IdMessage("Your id is: ", this.id));
    }

    void ReceiveConfirm(){
        Message m = null;
        while (m == null){
            JsonElement j = readJson();
            Message temp = this.gson.fromJson(j, Message.class);
            if (temp.getError().equals(Errors.CLIENT_READY))
                m = temp;
        }
    }
}
