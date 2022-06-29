package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GraphicInterface.TestingGraphicHandler;
import it.polimi.ingsw.Server.PortGetter;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientTest {

    @Test
    void start() throws IOException {
        //test that the client will stop after timeout x 2 sec
        ServerSocket s = new ServerSocket(PortGetter.getPort());
        Thread serv = new Thread(() -> startServer(s));
        TestingGraphicHandler t = new TestingGraphicHandler("Cli");
        t.startGraphic();
        Client c = new Client(t, s.getInetAddress().getHostAddress(), s.getLocalPort(), Duration.ofSeconds(2));

        c.start();

        assertTrue(true);
        //done in other test
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

    @Test
    void AskGraphic() {
        //not done because need input
        assertTrue(true);
    }
}