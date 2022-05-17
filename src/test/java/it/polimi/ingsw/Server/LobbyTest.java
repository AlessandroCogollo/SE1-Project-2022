package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Enum.Wizard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LobbyTest {
    @Test
    void run() {
        int portNum = (int)(Math.random()*64512) + 1024;
        Server server = new Server(portNum);

        //create a thread that stops the server after some time
        new Thread(new ServerTest.ServerStopper(500, server)).start();
        server.start();

        // connect a random number of clients (between 2 and 4)
        for (int i = 0; i < (int)(Math.random() * 3) + 2; i++) {
            Client client = new Client("127.0.0.1", portNum);
            System.out.println(i);
        }

        // TODO: actual testing
        assertTrue(true);
    }

    @Test
    void shutDownLobby() {
        int portNum = (int)(Math.random()*64512) + 1024;
        Server server = new Server(portNum);

        //create a thread that stops the server after some time
        new Thread(new ServerTest.ServerStopper(500, server)).start();
        server.start();

        Lobby lobby = server.getLobby();
        assertTrue(lobby.shutDownLobby());
    }

    @Test
    void setParameters() {
        int portNum = (int)(Math.random()*65534);
        Server server = new Server(portNum);

        new Thread(new ServerTest.ServerStopper(500, server)).start();
        server.start();

        int randPlayers = (int)(Math.random() * 4);
        int randGameMode = (int)(Math.random() * 2);

        Lobby lobby = server.getLobby();

        lobby.setParameters(randPlayers, randGameMode);

        assertEquals(randPlayers, lobby.getNumOfPlayers());
        assertEquals(randGameMode, lobby.getGameMode());
    }

    @Test
    void setOk() {
        int portNum = (int)(Math.random()*64512) + 1024;
        Server server = new Server(portNum);

        new Thread(new ServerTest.ServerStopper(500, server)).start();
        server.start();

        Lobby lobby = server.getLobby();
        int randId = (int)(Math.random() * lobby.getNumOfPlayers());
        String username = "test";
        Wizard w = Wizard.King;

        lobby.SetOk(randId, username, w);

        assertTrue(lobby.getIds().contains(randId));
        assertTrue(lobby.getUsernames().containsValue(username));
    }
}
