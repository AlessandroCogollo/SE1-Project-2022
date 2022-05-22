package it.polimi.ingsw.Network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Message.Ping;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionHandlerTest {

    @Test
    void getQueueToServer() throws IOException {
        ServerSocket s = new ServerSocket(8743);
        ConnectionHandler c = new ConnectionHandler(s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(60), null);

        Thread t = new Thread(c);
        t.start();
        Socket ser = s.accept();
        assertNotNull(c.getOutQueue());
    }

    @Test
    void getQueueFromServer() throws IOException {
        ServerSocket s = new ServerSocket(4321);
        ConnectionHandler c = new ConnectionHandler(s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(60), null);

        Thread t = new Thread(c);
        t.start();
        Socket ser = s.accept();
        assertNotNull(c.getInQueue());;
    }

    class UtilClass {
        private boolean bool;
        private ConnectionHandler c = null;

        public UtilClass(boolean bool) {
            this.bool = bool;
        }

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public ConnectionHandler getC() {
            return c;
        }

        public void setC(ConnectionHandler c) {
            this.c = c;
        }
    }

    @Test
    void run() throws IOException, InterruptedException {



        ServerSocket s = new ServerSocket(5088);
        ConnectionHandler c = new ConnectionHandler(s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(60), null);

        Thread t = new Thread(c);
        t.start();
        Socket ser = s.accept();

        BufferedReader serverIn = new BufferedReader(new InputStreamReader(ser.getInputStream()));
        PrintWriter serverOut = new PrintWriter(ser.getOutputStream(), true);

        BlockingQueue <JsonElement> clientIn = c.getInQueue();
        BlockingQueue <JsonElement> clientOut = c.getOutQueue();

        //set ping from server to client
        Callable pingSender = new Callable() {
            private PrintWriter w;
            private Gson g;

            public Callable init(PrintWriter w){
                this.w = w;
                this.g = new Gson();
                return this;
            }

            @Override
            public Object call() {
                w.println(g.toJson(new Ping()));
                return null;
            }
        }.init(serverOut);

        Timer timer = new Timer();
        timer.schedule(new TimerTaskCloneable(pingSender), 0,  1000); //each seconds

        testClientToServer(clientOut, serverIn, false); //the client is on other thread
        testServerToClient(serverOut, clientIn, false); //the client is on other thread

        //mixed together
        final BlockingQueue<JsonElement> finalClientOut = clientOut;
        final BufferedReader finalServerIn = serverIn;
        Thread t1 = new Thread(() -> {
            try {
                testClientToServer(finalClientOut, finalServerIn, false);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        final PrintWriter finalServerOut = serverOut;
        final BlockingQueue<JsonElement> finalClientIn = clientIn;
        Thread t2 = new Thread(() -> {
            try {
                testServerToClient(finalServerOut, finalClientIn, false);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();



        timer.cancel();
        c.stopConnectionHandler();
        s.close();
        t.join(); //assure that all thread has finished


        //ping test


        //create new connection handler for faster ping test

        //use a little class to pass information to runnable and verifying that everything works fine
        UtilClass serverIsUp = new UtilClass(true);
        s = new ServerSocket(5088);
        Callable call = new Callable() {
            private UtilClass b;

            public Callable init (UtilClass b){
                this.b = b;
                return this;
            }

            @Override
            public Object call() {
                b.setBool(false);
                b.getC().stopConnectionHandler();
                return null;
            }
        }.init(serverIsUp);

        c = new ConnectionHandler(s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(2), call);
        serverIsUp.setC(c);

        t = new Thread(c);
        t.start();
        ser = s.accept();

        serverIn = new BufferedReader(new InputStreamReader(ser.getInputStream()));
        serverOut = new PrintWriter(ser.getOutputStream(), true);


        //set ping from server to client
        pingSender = new Callable() {
            private PrintWriter w;
            private Gson g;

            public Callable init(PrintWriter w){
                this.w = w;
                this.g = new Gson();
                return this;
            }

            @Override
            public Object call() {
                w.println(g.toJson(new Ping()));
                return null;
            }
        }.init(serverOut);

        timer = new Timer();
        timer.schedule(new TimerTaskCloneable(pingSender), 0,  200); //5 per sec


        //test ping from client to server for 2 seconds
        final BufferedReader finalServerIn1 = serverIn;
        Thread t3 = new Thread(() -> {
            try {
                testPingFromClient(finalServerIn1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(2000);
        t3.interrupt();

        //test correct call to error callable

        timer.cancel();

        Thread.sleep(4000); //the timeout for the server is double the timeout to send the ping

        assertFalse(serverIsUp.isBool());

        s.close();
        t.join(); //assure that all thread has finished
        assertTrue(true);
    }

    //only to use with thread infinite method
    private void testPingFromClient(BufferedReader serverIn) throws IOException {
        Gson g = new Gson();
        Message last = null;

        //first ping
        while (last == null){
            String s = null;
            while (s == null)
                s = serverIn.readLine();

            last = g.fromJson(s, Message.class);
            assertEquals(Errors.PING, last.getError());
        }

        while (true){
            String s = null;
            while (s == null)
                s = serverIn.readLine();

            Message newM = g.fromJson(s, Message.class);
            assertEquals(Errors.PING, newM.getError());

            Duration diff = Duration.between(last.getTime(), newM.getTime());
            assertTrue(diff.toMillis() <= 2000);
            last = newM;
        }

    }

    private void testServerToClient(PrintWriter serverOut, BlockingQueue<JsonElement> clientIn, boolean output) throws InterruptedException {

        Gson g = new Gson();

        //ping message from server are automatically removed from the queue

        //test simple message
        Message m = new Message(Errors.NO_ERROR, "Test");
        serverOut.println(g.toJson(m));

        JsonElement mJ = clientIn.take();
        Message mR = g.fromJson(mJ, Message.class);
        assertEquals(m, mR);

        //test arrive in the right order

        ArrayList<Integer> messageOrder = new java.util.ArrayList<>(Arrays.stream(IntStream.range(0, 100).toArray()).boxed().toList());
        Collections.shuffle(messageOrder);


        for(Integer i: messageOrder) {
            if (output) System.out.println("Sent to client Test " + i);
            serverOut.println(g.toJson(new Message(Errors.NO_ERROR, "Test " + i)));
        }

        for(Integer i: messageOrder) {
            mJ = clientIn.take();
            mR = g.fromJson(mJ, Message.class);
            Message temp = (new Message(Errors.NO_ERROR, "Test " + i));
            if (output) System.out.println("Received from Server Test " + i);
            assertEquals(temp.getMessage(), mR.getMessage());
        }

    }

    private void testClientToServer(BlockingQueue<JsonElement> clientOut, BufferedReader serverIn, boolean output) throws InterruptedException, IOException {

        Gson g = new Gson();


        //test arrives
        Message m = new Message(Errors.NO_ERROR, "Test");
        JsonElement mJ1 = g.toJsonTree(m);

        clientOut.put(mJ1);

        Message mR = null;
        String sR;
        while (mR == null || Errors.PING.equals(mR.getError())){
            sR = null;
            while (sR == null)
                sR = serverIn.readLine();

            mR = g.fromJson(sR, Message.class);
        }

        assertEquals(m, mR);

        //test arrive in the right order

        ArrayList<Integer> messageOrder = new java.util.ArrayList<>(Arrays.stream(IntStream.range(0, 100).toArray()).boxed().toList());
        Collections.shuffle(messageOrder);

        for(Integer i: messageOrder) {
            if (output) System.out.println("Sent to Server Test " + i);
            clientOut.put(g.toJsonTree(new Message(Errors.NO_ERROR, "Test " + i)));
        }


        for(Integer i: messageOrder) {
            mR = null;
            while (mR == null || (mR != null && Errors.PING.equals(mR.getError()))) {
                sR = null;
                while (sR == null){
                    sR = serverIn.readLine();
                }
                mR = g.fromJson(sR, Message.class);
            }
            Message temp = (new Message(Errors.NO_ERROR, "Test " + i));
            //System.out.println("Right: " + temp);
            //System.out.println("Tested: " + mR);
            if (output) System.out.println("Received from Client Test " + i);
            assertEquals(temp.getMessage(), mR.getMessage()); //not equals for creation time
        }
    }
    @Test
    void stopConnectionHandler() throws IOException, InterruptedException {
        ServerSocket s = new ServerSocket(5088);
        ConnectionHandler c = new ConnectionHandler(s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(60), null);

        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(c);
        s.accept();

        Thread.sleep(200);

        c.stopConnectionHandler();
        ex.shutdown();
        s.close();
        Thread.sleep(200);

        assertTrue(ex.isTerminated());
    }
}