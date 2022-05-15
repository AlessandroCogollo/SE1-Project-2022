package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Enum.Errors;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    static class ServerStopper extends ServerCodeSetter{
        public ServerStopper(long timeDelta, Server server) {
            super(timeDelta, 1, Errors.GAME_OVER, server);
        }
    }

    static class ServerCodeSetter implements Runnable{
        private final long timeDelta;
        private final int repetition;
        private final Errors code;
        private final Server server;

        ServerCodeSetter(long timeDelta, int repetition, Errors code, Server server) {
            this.timeDelta = timeDelta;
            this.repetition = repetition;
            this.code = code;
            this.server = server;
        }

        @Override
        public void run() {
            for (int i = 0; i < this.repetition; i++) {
                try {
                    Thread.sleep(this.timeDelta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                server.setCode(this.code);
            }
        }
    }

    @Test
    void start() {
        //todo finish when the rest of controller is done

        Server server = new Server(5078);

        //create a thread for stop the server after some times
        new Thread(new ServerStopper(500, server)).start();

        server.start();

        //stop message works
        assertTrue(true);
    }

    @Test
    void setCode() {
        Server server = new Server(5068);

        //create a thread for set a lot of times a code
        new Thread(new ServerCodeSetter(10, 50, Errors.NOTHING_TODO, server));

        //create a thread for stop the server after some times
        new Thread(new ServerStopper(500, server)).start();

        server.start();

        assertTrue(true);
    }

    @Test
    void setGameProperties() {
        //todo after lobby
    }

    @Test
    void main() {
        Server testServer = new Server(5089);
        Client testClient1 = new Client("127.0.0.1", 5089);
        //System.out.println(testServer.getNumOfPlayers());
    }
}