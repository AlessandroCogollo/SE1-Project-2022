package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void getNumOfPlayers() {
    }

    @Test
    void getServer() {
    }

    @Test
    void isFirstConnected() {
    }

    @Test
    void getPort() {
    }

    @Test
    void setNumOfPlayers() {
    }

    @Test
    void addId() {
    }

    @Test
    void getIds() {
    }

    @Test
    void main() {
        Server testServer = new Server(5088);
        Client testClient1 = new Client("127.0.0.1", 5088);
        System.out.println(testServer.getNumOfPlayers());
    }
}