package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    //todo change when board is implemented, need GameBoard class
    protected static Player getPlayer(int gameMode, School school, Player mate, int towerColor){
        if (gameMode == 0)
            return new Player(1, towerColor, mate, null, school);
        else
            return new AdvancedPlayer(1, towerColor, mate, null, school);
    }

    @Test
    void hasStudent() {

        //done in school test
        assertTrue(true);
    }

    @Test
    void moveStudent() {

        //done in school and cloud
        assertTrue(true);
    }

    @Test
    void moveMotherNature() {

        //todo, need GameBoard class
    }

    @Test
    void chooseCloud() {

        //todo, need GameBoard class
    }

    @Test //testing only the mate possibility
    void getTowers() {
        School s1 = new School(8, 0, new Bag());
        School s2 = new School(0, 0, new Bag());
        Player p1 = getPlayer(0, s1, null, 1);
        Player p2 = getPlayer(0, s2, p1, 1);

        assertEquals(p1.getTowers(), p2.getTowers(), "Test 1");
    }

    @Test //testing only the mate possibility
    void moveTowerToIsland() {
        School s1 = new School(8, 0, new Bag());
        School s2 = new School(0, 0, new Bag());
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

        //todo test win case, need Gameboard
    }

    @Test //testing only the mate possibility
    void receiveTowerFromIsland() {
        School s1 = new School(8, 0, new Bag());
        School s2 = new School(0, 0, new Bag());
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
    void getPlayers (){

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

        GameBoard board = null;
        Bag bag = new Bag();


        Collection<Player> players = Player.factoryPlayers(id2, 0, board, bag);
        for (Player p: players){
            assertEquals(false, p instanceof AdvancedPlayer, "test 1 - 2 player easy mode");
            assertEquals(true, p instanceof Player, "test 1 - 2 player easy mode");
        }

        players = Player.factoryPlayers(id2, 1, board, bag);
        for (Player p: players)
            assertEquals(true, p instanceof AdvancedPlayer, "test 2 - 2 player advanced mode");

        players = Player.factoryPlayers(id3, 0, board, bag);
        for (Player p: players){
            assertEquals(false, p instanceof AdvancedPlayer, "test 3 - 3 player easy mode");
            assertEquals(true, p instanceof Player, "test 3 - 3 player easy mode");
        }

        players = Player.factoryPlayers(id3, 1, board, bag);
        for (Player p: players)
            assertEquals(true, p instanceof AdvancedPlayer, "test 4 - 3 player advanced mode");

        players = Player.factoryPlayers(id4, 0, board, bag);
        for (Player p: players){
            assertEquals(false, p instanceof AdvancedPlayer, "test 5 - 4 player easy mode");
            assertEquals(true, p instanceof Player, "test 5 - 4 player easy mode");
        }

        players = Player.factoryPlayers(id4, 1, board, bag);
        for (Player p: players)
            assertEquals(true, p instanceof AdvancedPlayer, "test 6 - 4 player advanced mode");

        players = Player.factoryPlayers(id4, 0, board, bag);
        Player p1 = null, p2 = null, p3 = null, p4 = null;
        for (Player p: players){
            switch (p.getId()){
                case 1:
                    p1 = p;
                    break;
                case 2:
                    p2 = p;
                    break;
                case 3:
                    p3 = p;
                    break;
                case 4:
                    p4 = p;
                    break;
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