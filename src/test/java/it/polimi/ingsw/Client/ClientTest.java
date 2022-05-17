package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GraphicInterface.Cli;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void listen() {

    }

    @Test
    void talk() {
    }

    @Test
    void stopConnection() {
    }

    @Test
    void askGraphic() {
        //todo
    }

    @Test
    void start() throws IOException {
        //test that the client will stop after timeout x 2 sec
        ServerSocket s = new ServerSocket(5088);
        Thread serv = new Thread(() -> startServer(s));
        Client c = new Client(new Cli(), s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(2));

        c.start();

        assertTrue(true);
        //todo
    }

    private void startServer(ServerSocket serverSocket) {
        try {
            Socket s = serverSocket.accept();
            BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true){
                String st = null;
                while (st == null)
                    st = b.readLine();
                System.out.println(st);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void setCode() {
    }

    @Test
    void main() {
    }
}