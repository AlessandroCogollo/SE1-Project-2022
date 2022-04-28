package it.polimi.ingsw.Client;
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
    private BufferedReader in = null;
    private PrintWriter out = null;

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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // sends output to the socket
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        Talk();

    }

    void Talk(){
        String line = "";
        String resp = "";
        Scanner sc=new Scanner(System.in);

        // keep reading until "Over" is input
        while (!line.equals("END"))
        {
            try
            {
                System.out.println(resp);
                line = sc.nextLine();
                resp = sendMessage(line);

            }
            catch(IOException i)
            {
                System.out.println(i);
                System.out.println("IOException : Closing connection...");
                try
                {
                    in.close();
                    out.close();
                    socket.close();
                }
                catch(IOException io)
                {
                    System.out.println(io);
                }

            }
        }
        if(line.equals("END")){
            try {
                sendMessage("Closing connection by client with port : " + port);
                stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5011);
    }
}

