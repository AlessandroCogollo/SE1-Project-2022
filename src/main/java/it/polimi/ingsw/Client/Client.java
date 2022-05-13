package it.polimi.ingsw.Client;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Client
{
    // initialize socket and input output streams
    private int port;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    // constructor to put ip address and port
    public Client(String address, int port)
    {
        this.port = port;
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            FileOutputStream fileOut = new FileOutputStream("out.txt");
            this.out = new ObjectOutputStream(fileOut);

            FileInputStream fileStream = new FileInputStream("in.txt");
            this.in = new ObjectInputStream(fileStream);
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }


        Runnable listening = () -> {
            try {
                Listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable talking = () -> {
            try {
                Talk();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Executor e = Executors.newFixedThreadPool(2);
        e.execute(listening);
        e.execute(talking);


    }

    void Listen() throws IOException {
        String line = "";
        while(true){
            line = in.readLine();
            System.out.println(line);
        }

    }

    void Talk() throws IOException {
        String line = "";
        Scanner sc=new Scanner(System.in);

        // keep reading until "Over" is input
        while (!line.equals("END"))
        {
            line = sc.nextLine();
            Message m = new Message(Errors.NO_ERROR, "Hi Server");
            out.writeObject(m);

        }
        if(line.equals("END")){
            try {
                stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String args[]){
        int port = 5088; //used for testing
        String ip = "127.0.0.1";
        /*
        if(args.length != 2)
            System.out.println("Error missing arguments");
        else {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        */
        //todo add a method to stop server
        //deactivate for pass auto test
        new Client(ip, port);
    }
}

