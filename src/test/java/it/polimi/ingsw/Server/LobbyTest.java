package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GraphicInterface.TestingCli;
import it.polimi.ingsw.Enum.Wizard;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

//real test done while testing the connection between server and client
public class LobbyTest {
    @Test
    void run() throws InterruptedException {
        Lobby l = new Lobby(21431, null);

        new Thread(l).start();

        Thread.sleep(500);

        //setParameters done by the first client thread
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            Client c = new Client(new TestingCli(), "127.0.0.1", 21431);
            executorService.execute(c::start);
        }

        Thread.sleep(500);

        executorService.shutdownNow();
        l.shutDownLobby();
        Thread.sleep(500);
        assertTrue(true);
    }

    @Test
    void shutDownLobby() throws InterruptedException {
        Lobby l = new Lobby(17431, null);

        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(l);

        Thread.sleep(1000);
        System.out.println("Shutting down lobby");
        l.shutDownLobby();

        ex.shutdown(); // if not shutdown executors are anyway considered not terminated even if their task are completed
        Thread.sleep(20);
        assertTrue(ex.awaitTermination(2, TimeUnit.MINUTES));

    }

    @Test
    void setParameters() throws InterruptedException {
        Lobby l = new Lobby(17431, null);

        new Thread(l).start();

        Thread.sleep(500);

        //setParameters done by the first client thread
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            Client c = new Client(new TestingCli(), "127.0.0.1", 17431);
            executorService.execute(c::start);
        }

        Thread.sleep(500);

        executorService.shutdownNow();
        l.shutDownLobby();
        Thread.sleep(500);
        assertTrue(true);
    }

    @Test
    void setOk() throws InterruptedException {
        Lobby l = new Lobby(17441, null);

        new Thread(l).start();

        Thread.sleep(2000);

        //setOk done by all the client thread
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            Client c = new Client(new TestingCli(), "127.0.0.1", 17441);
            executorService.execute(c::start);
        }

        Thread.sleep(5000);
        for (int i = 0; i < 4; i++){
            String user = l.getUsernames().get(i);
            Wizard w = l.getWizards().get(i);
            assertNotNull(user);
            assertNotNull(w);
            assertTrue(l.getUsernames().remove(i, user));
            assertTrue(l.getWizards().remove(i, w));
            assertFalse(l.getUsernames().containsValue(user));
            assertFalse(l.getWizards().containsValue(w));
        }

        executorService.shutdownNow();
        l.shutDownLobby();
        Thread.sleep(500);
        assertTrue(true);
    }

    @Test
    void setQueues() throws InterruptedException {
        Lobby l = new Lobby(17531, null);

        new Thread(l).start();

        int[] ids = {0, 1, 2, 3};

        QueueOrganizer q = new QueueOrganizer(ids);

        l.setQueues(q);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            Client c = new Client(new TestingCli(), "127.0.0.1", 17531);
            executorService.execute(c::start);
        }

        Thread.sleep(500);

        executorService.shutdownNow();
        l.shutDownLobby();
        Thread.sleep(500);
        assertTrue(true);
    }

    @Test
    void clientDown() throws InterruptedException {
        Server s = new Server(16323); // placeholder, not used
        Lobby l = new Lobby(18531, s);

        new Thread(l).start();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            Client c = new Client(new TestingCli(), "127.0.0.1", 18531);
            executorService.execute(c::start);
        }

        Thread.sleep(500);
        l.clientDown(1);

        executorService.shutdownNow();
        l.shutDownLobby();
        Thread.sleep(500);
        assertTrue(true);

    }

    @Test
    void getUsernames() {
        assertNotNull(new Lobby(17324, null).getUsernames());
    }

    @Test
    void getWizards() {
        assertNotNull(new Lobby(17325, null).getWizards());
    }
}
