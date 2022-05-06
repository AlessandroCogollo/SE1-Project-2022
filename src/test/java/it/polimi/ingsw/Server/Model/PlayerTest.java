package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Assistant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    protected static Player getPlayer(int gameMode, School school, Player mate, int towerColor) {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 0);
        if (gameMode == 0) {
            return new Player(1, towerColor, mate, g, school);
        }
        else {
            return new AdvancedPlayer(1, towerColor, mate, g, school);
        }
    }

    @Test
    void getId() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getTowerColor() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getActiveAssistant() {
        //trivial
        assertTrue(true);
    }

    @Test
    void hasAssistant() {
        //trivial
        assertTrue(true);
    }

    @Test
    void hasStudent() {
        //done in school test
        assertTrue(true);
    }

    @Test
    void playAssistant() {
        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(0, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        Player p = r.getCurrent();
        for (int i = 1; i < Assistant.getNumberOfAssistants(); i++) {
            p.playAssistant(Assistant.getAssistantByValue(i));
            assertFalse(r.getIsFinalRound());
        }
        p.playAssistant(Assistant.getAssistantByValue(10));
        assertTrue(r.getIsFinalRound());
    }

    @Test
    void moveStudent() {

        //done in school and cloud
        assertTrue(true);
    }

    @Test
    void moveMotherNature() {
        //done in board test
        assertTrue(true);
    }

    @Test
    void chooseCloud() {
        //done in school test
        assertTrue(true);
    }

    @Test //testing only the mate possibility
    void getTowers() {
        School s1 = new School(8, 0, new Bag(null));
        School s2 = new School(0, 0, new Bag(null));
        Player p1 = getPlayer(0, s1, null, 1);
        Player p2 = getPlayer(0, s2, p1, 1);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
    }

    @Test //testing only the mate possibility
    void moveTowerToIsland() {
        School s1 = new School(8, 0, new Bag(null));
        School s2 = new School(0, 0, new Bag(null));
        Player p1 = getPlayer(0, s1, null, 1);
        Player p2 = getPlayer(0, s2, p1, 1);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
        assertEquals(8, p2.getTowers(), "Test 1");
        assertEquals(0, s2.getTowers(), "Test 2");

        p1.moveTowerToIsland(1);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
        assertEquals(7, p2.getTowers(), "Test 1");
        assertEquals(0, s2.getTowers(), "Test 2");

        p2.moveTowerToIsland(1);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
        assertEquals(6, p2.getTowers(), "Test 1");
        assertEquals(0, s2.getTowers(), "Test 2");

        //todo test win case
    }

    @Test //testing only the mate possibility
    void receiveTowerFromIsland() {
        School s1 = new School(8, 0, new Bag(null));
        School s2 = new School(0, 0, new Bag(null));
        Player p1 = getPlayer(0, s1, null, 1);
        Player p2 = getPlayer(0, s2, p1, 1);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
        assertEquals(8, p2.getTowers(), "Test 1");
        assertEquals(0, s2.getTowers(), "Test 2");

        p1.moveTowerToIsland(7);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
        assertEquals(1, p2.getTowers(), "Test 1");
        assertEquals(0, s2.getTowers(), "Test 2");

        p2.receiveTowerFromIsland(7);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
        assertEquals(8, p2.getTowers(), "Test 1");
        assertEquals(0, s2.getTowers(), "Test 2");
    }

    @Test
    void getNumberOfStudentInRoomByColor() {

        //done in school test
        assertTrue(true);
    }

    @Test
    void factoryPlayers() {

        //testing only player parameter, other test will be done in school

        int[] id2 = new int[2];
        id2[0] = 1;
        id2[1] = 2;

        int[] id3 = new int[3];
        id3[0] = 1;
        id3[1] = 2;
        id3[2] = 3;

        int[] id4 = new int[4];
        id4[0] = 1;
        id4[1] = 2;
        id4[2] = 3;
        id4[3] = 4;

        GameInitializer g = new GameInitializer(0, 2);
        g.createAllGame(id2, null);
        Collection<Player> players = new ArrayList<>();
        for (int j : id2) players.add(g.getPlayerById(j));

        for (Player p : players) {
            assertFalse(p instanceof AdvancedPlayer, "test 1 - 2 player easy mode");
            assertNotNull(p, "test 1 - 2 player easy mode");
        }

        g = new GameInitializer(1, 2);
        g.createAllGame(id2, null);
        players = new ArrayList<>();
        for (int j : id2) players.add(g.getPlayerById(j));

        for (Player p : players)
            assertTrue(p instanceof AdvancedPlayer, "test 2 - 2 player advanced mode");


        g = new GameInitializer(0, 3);
        g.createAllGame(id3, null);
        players = new ArrayList<>();
        for (int j : id3) players.add(g.getPlayerById(j));

        for (Player p : players) {
            assertFalse(p instanceof AdvancedPlayer, "test 3 - 3 player easy mode");
            assertNotNull(p, "test 3 - 3 player easy mode");
        }


        g = new GameInitializer(1, 3);
        g.createAllGame(id3, null);
        players = new ArrayList<>();
        for (int j : id3) players.add(g.getPlayerById(j));

        for (Player p : players)
            assertTrue(p instanceof AdvancedPlayer, "test 4 - 3 player advanced mode");


        g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);
        players = new ArrayList<>();
        for (int j : id4) players.add(g.getPlayerById(j));

        for (Player p : players) {
            assertFalse(p instanceof AdvancedPlayer, "test 5 - 4 player easy mode");
            assertNotNull(p, "test 5 - 4 player easy mode");
        }


        g = new GameInitializer(1, 4);
        g.createAllGame(id4, null);
        players = new ArrayList<>();
        for (int j : id4) players.add(g.getPlayerById(j));

        for (Player p : players)
            assertTrue(p instanceof AdvancedPlayer, "test 6 - 4 player advanced mode");


        g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);
        players = new ArrayList<>();
        for (int j : id4) players.add(g.getPlayerById(j));

        Player p1 = null, p2 = null, p3 = null, p4 = null;
        for (Player p : players) {
            switch (p.getId()) {
                case 1 -> p1 = p;
                case 2 -> p2 = p;
                case 3 -> p3 = p;
                case 4 -> p4 = p;
            }
        }
        p1.receiveTowerFromIsland(2);
        p4.moveTowerToIsland(2);
        assertEquals(p1.getTowers(), p3.getTowers());
        assertEquals(10, p3.getTowers());
        assertEquals(p2.getTowers(), p4.getTowers());
        assertEquals(6, p2.getTowers());
    }
}