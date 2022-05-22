package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GraphicInterface.TestingCli;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ClientMessageDecorator;
import it.polimi.ingsw.Message.Message;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

//real test done while testing the connection between server and client
class ClientHandlerTest {

    @Test
    void clientDown() throws IOException, InterruptedException {
        ExecutorService client = Executors.newSingleThreadExecutor();
        ExecutorService server = Executors.newSingleThreadExecutor();
        Server se = new Server(16329); //used as placeholder
        Lobby l = new Lobby(43621, se); //used as placeholder

        ServerSocket s = new ServerSocket(15342);

        Client c = new Client(new TestingCli(), "127.0.0.1", 15342);
        Socket skt = s.accept();
        client.execute(c::start);
        ClientHandler cH = new ClientHandler(skt, 0, l);
        server.execute(cH);

        Thread.sleep(1000);

        cH.clientDown();

        assertTrue(true);
    }

    @Test
    void shutdown() throws IOException, InterruptedException {
        ExecutorService client = Executors.newSingleThreadExecutor();
        ExecutorService server = Executors.newSingleThreadExecutor();

        ServerSocket s = new ServerSocket(8374);

        Client c = new Client(new TestingCli(), "127.0.0.1", 8374);
        Socket skt = s.accept();
        client.execute(c::start);
        ClientHandler cH = new ClientHandler(skt, 0, null);
        server.execute(cH);

        Thread.sleep(1000);
        System.out.println("Shutting down ClientHandler");
        cH.shutdown();

        server.shutdown(); // if not shutdown executors are anyway considered not terminated even if their task are completed
        Thread.sleep(20);
        assertTrue(server.awaitTermination(2, TimeUnit.MINUTES));

        client.shutdownNow();

    }

    @Test
    void setQueues() throws IOException, InterruptedException {

        ExecutorService client = Executors.newSingleThreadExecutor();
        ExecutorService server = Executors.newSingleThreadExecutor();

        ServerSocket s = new ServerSocket(19374);

        Client c = new Client(new TestingCli(), "127.0.0.1", 19374);
        Socket skt = s.accept();
        client.execute(c::start);
        ClientHandler cH = new ClientHandler(skt, 0, null);
        server.execute(cH);

        Thread.sleep(1000);

        BlockingQueue<ClientMessageDecorator> model = new LinkedBlockingQueue<>();
        BlockingQueue<JsonElement> cl  = new LinkedBlockingQueue<>();
        cH.setQueues(model, cl);

        Thread.sleep(500);
        cH.shutdown();
        client.shutdownNow();
        //output client handler 0 game started
        assertTrue(true);
    }

    @Test
    void run() throws IOException, InterruptedException {
        ExecutorService client = Executors.newSingleThreadExecutor();
        ExecutorService server = Executors.newSingleThreadExecutor();

        ServerSocket s = new ServerSocket(1975);

        Client c = new Client(new TestingCli(), "127.0.0.1", 1975);
        Socket skt = s.accept();
        client.execute(c::start);
        ClientHandler cH = new ClientHandler(skt, 0, null);
        server.execute(cH);

        Thread.sleep(1000);
        cH.shutdown();

        server.shutdown(); // if not shutdown executors are anyway considered not terminated even if their task are completed
        Thread.sleep(20);
        assertTrue(true);
        client.shutdownNow();
    }

    @Test
    void sendJsonToClient() throws IOException, InterruptedException {
        ExecutorService client = Executors.newSingleThreadExecutor();
        ExecutorService server = Executors.newSingleThreadExecutor();
        Lobby l = new Lobby(12365, null); //used as placeholder

        ServerSocket s = new ServerSocket(7623);

        Client c = new Client(new TestingCli(), "127.0.0.1", 7623);
        Socket skt = s.accept();
        client.execute(c::start);
        ClientHandler cH = new ClientHandler(skt, 0, l);
        server.execute(cH);
        Thread.sleep(500);
        JsonElement j = new Gson().toJsonTree(new Message(Errors.NO_ERROR, "TEST"));
        cH.sendMessage(j);

        assertTrue(true);
        cH.shutdown();
        client.shutdownNow();
    }
}