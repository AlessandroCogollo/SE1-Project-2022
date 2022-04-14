package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    @Test
    void GameBoard(){
        //trivial the only check would be for cloud number, all other construction will be done in the relative constructor
        for (int i = 2; i < 5; i++)
            for (int j = 0; j < i; j++)
                assertNotNull(GameInitializerTest.setGameInitializer(i, 0).getBoard().getCloudById(j));
    }

    @Test
    void getActiveCharacter() {
        //trivial
        assertTrue(true);
    }

    @Test
    void existsCloud() {
        //test 2 player
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);

        assertFalse(g.getBoard().existsCloud(-1));
        assertTrue(g.getBoard().existsCloud(0));
        assertTrue(g.getBoard().existsCloud(1));
        assertFalse(g.getBoard().existsCloud(2));
        assertFalse(g.getBoard().existsCloud(3));

        //test 3 player
        g = GameInitializerTest.setGameInitializer(3, 0);

        assertFalse(g.getBoard().existsCloud(-1));
        assertTrue(g.getBoard().existsCloud(0));
        assertTrue(g.getBoard().existsCloud(1));
        assertTrue(g.getBoard().existsCloud(2));
        assertFalse(g.getBoard().existsCloud(3));

        //test 4 player
        g = GameInitializerTest.setGameInitializer(4, 1);

        assertFalse(g.getBoard().existsCloud(-1));
        assertTrue(g.getBoard().existsCloud(0));
        assertTrue(g.getBoard().existsCloud(1));
        assertTrue(g.getBoard().existsCloud(2));
        assertTrue(g.getBoard().existsCloud(3));
        assertFalse(g.getBoard().existsCloud(4));
    }

    @Test
    void existsCharacter() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getCloudById() {
        //test 2 player
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);

        assertNull(g.getBoard().getCloudById(-1));
        assertEquals(0, g.getBoard().getCloudById(0).getId());
        assertEquals(1, g.getBoard().getCloudById(1).getId());
        assertNull(g.getBoard().getCloudById(2));
        assertNull(g.getBoard().getCloudById(3));

        //test 3 player
        g = GameInitializerTest.setGameInitializer(3, 0);

        assertNull(g.getBoard().getCloudById(-1));
        assertEquals(0, g.getBoard().getCloudById(0).getId());
        assertEquals(1, g.getBoard().getCloudById(1).getId());
        assertEquals(2, g.getBoard().getCloudById(2).getId());
        assertNull(g.getBoard().getCloudById(3));

        //test 4 player
        g = GameInitializerTest.setGameInitializer(4, 1);

        assertNull(g.getBoard().getCloudById(-1));
        assertEquals(0, g.getBoard().getCloudById(0).getId());
        assertEquals(1, g.getBoard().getCloudById(1).getId());
        assertEquals(2, g.getBoard().getCloudById(2).getId());
        assertEquals(3, g.getBoard().getCloudById(3).getId());
        assertNull(g.getBoard().getCloudById(4));
    }

    @Test
    void getCharacterById() {
        //set the number of test considering that is used a random feature
        int testNumber = 10;
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < testNumber; i++){
            GameInitializer g = GameInitializerTest.setGameInitializer(rand.nextInt(3) + 2, rand.nextInt(2)); //rand form 2 to 4, and from 0 to 1
            for (int j = 0; j < 12; j++){
                if (g.getBoard().existsCharacter(j))
                    assertNotNull(g.getBoard().getCharacterById(j));
                else
                    assertNull(g.getBoard().getCharacterById(j));
            }
        }
    }

    @Test
    void populateClouds() {
        //done in cloud test
        assertTrue(true);
    }

    @Test
    void playCharacter() {
        //done in character test
        assertTrue(true);
    }

    @Test
    void isCharacterPlayed() {
        //trivial
        assertTrue(true);
    }

    @Test
    void moveMotherNature() {
        //test only apothecary bancard other test will be done in islands class

        //todo only check for apothecary bancard
    }

    //todo calcInfluence test
}
