package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static it.polimi.ingsw.Server.Model.GameInitializerTest.setGameInitializer;
import static org.junit.jupiter.api.Assertions.*;

class IslandsTest {

    @Test
    void Islands (){
        GameInitializer g = setGameInitializer(4, 1);
        //all the islands exist
        boolean allExists = true;
        for (int i = 0; i < 12; i++)
            if (!g.getIslands().existsIsland(i)){
                allExists = false;
                break;
            }
        assertTrue(allExists);

        //students are 10 and 2 for color
        int[] x = new int[Color.getNumberOfColors()];
        Arrays.fill(x, 0);
        for (int i = 0; i < 12; i++){
            int[] temp = g.getIslands().getIslandFromId(i).getStudents();
            for (Color c : Color.values())
                x[c.getIndex()] += temp[c.getIndex()];
        }

        int sum = 0;
        for (Color c : Color.values()){
            assertEquals(2, x[c.getIndex()]);
            sum += x[c.getIndex()];
        }
        assertEquals(10, sum);
    }

    @Test
    void getIslandsNumber() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getMotherNature() {
        //trivial
        assertTrue(true);
    }

    @Test
    void existsIsland() {
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        assertFalse(g.getIslands().existsIsland(-1));
        for (int i = 0; i < 12; i++){
            assertTrue(g.getIslands().existsIsland(i));
        }
        assertFalse(g.getIslands().existsIsland(12));
    }

    @Test
    void getIslandFromId() {
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        for (int i = -1; i < 20; i++) {
            if (g.getIslands().existsIsland(i))
                assertNotNull(g.getIslands().getIslandFromId(i));
            else
                assertNull(g.getIslands().getIslandFromId(i));
        }
    }

    @Test
    void addStudentToIsland() {
        //done in island
        assertTrue(true);
    }

    @Test
    void nextMotherNature() {
        GameInitializer g = setGameInitializer(4, 1);

        Island x = g.getIslands().getMotherNature();
        assertEquals(0, x.getId());

        g.getIslands().nextMotherNature(8);

        x = g.getIslands().getMotherNature();
        assertEquals(8, x.getId());

        g.getIslands().nextMotherNature(12);

        x = g.getIslands().getMotherNature();
        assertEquals(8, x.getId());

        g.getIslands().nextMotherNature(19);

        x = g.getIslands().getMotherNature();
        assertEquals(3, x.getId());

        //test after some aggregation

        Island isPrec = g.getIslands().getIslandFromId(7);
        Island curr = g.getIslands().getIslandFromId(8);
        Island isNext = g.getIslands().getIslandFromId(9);

        isPrec.setTowerColor(1);
        curr.setTowerColor(1);
        isNext.setTowerColor(1);

        g.getIslands().aggregateIsland(curr);

        g.getIslands().nextMotherNature(4);

        x = g.getIslands().getMotherNature();
        assertEquals(8, x.getId());

        g.getIslands().nextMotherNature(4);

        x = g.getIslands().getMotherNature();
        assertEquals(1, x.getId());
    }

    @Test
    void aggregateIsland() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 0);
        //at the start all the island are in order of their id
        Island isPrec = g.getIslands().getIslandFromId(7);
        Island curr = g.getIslands().getIslandFromId(8);
        Island isNext = g.getIslands().getIslandFromId(9);

        //set some attribute to test
        isPrec.setTowerColor(1);
        curr.setTowerColor(1);
        isNext.setTowerColor(1);

        isPrec.setTowerCount(2);
        isNext.addStudent(Color.Red);
        isNext.addStudent(Color.Red);

        g.getIslands().aggregateIsland(curr);

        assertTrue(g.getIslands().existsIsland(curr.getId()));
        assertEquals(1, curr.getTowerColor());

        //students
        assertEquals(5, Arrays.stream(curr.getStudents()).sum());
        assertTrue(curr.getStudents()[Color.Red.getIndex()] >= 2);

        //bancard
        assertEquals(0, curr.getBanCard());

        //towers
        assertEquals(2, curr.getTowerCount());

        //other island
        assertFalse(g.getIslands().existsIsland(isNext.getId()));
        assertFalse(g.getIslands().existsIsland(isPrec.getId()));
    }
}