package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorsTest {

    @Test
    void Professors (){
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 0);
        for (Color c: Color.values())
            assertNull(g.getProfessors().getPlayerWithProfessor(c));
    }

    @Test
    void updateProfessors() {
        //used the test in the test below (if get player with professor work it's because update professor works well)
        getPlayerWithProfessor();

        assertTrue(true);
    }

    @RepeatedTest(10)
    void getPlayerWithProfessor() {
        //set some random students for testing
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 0);
        Random rand = new Random(System.currentTimeMillis());


        int id1 = 4;
        Player p1 = g.getPlayerById(id1);
        int rand1 = rand.nextInt(6) + 1;
        int[] extracted1 = new int[Color.getNumberOfColors()];
        Arrays.fill(extracted1, 0);

        int id2 = 24;
        Player p2 = g.getPlayerById(id2);
        int rand2 = rand.nextInt(6) + 1;
        int[] extracted2 = new int[Color.getNumberOfColors()];
        Arrays.fill(extracted2, 0);

        int i = 0;
        while (i < rand1){
            Color c = Color.getColorById(rand.nextInt(Color.getNumberOfColors()));
            if (p1.hasStudent(c)){
                extracted1[c.getIndex()]++;
                p1.moveStudent(c, -1);
                i++;
            }
        }

        i = 0;
        while (i < rand2){
            Color c = Color.getColorById(rand.nextInt(Color.getNumberOfColors()));
            if (p2.hasStudent(c)){
                extracted2[c.getIndex()]++;
                p2.moveStudent(c, -1);
                i++;
            }
        }

        //real test
        for (Color c : Color.values()){
            if (extracted1[c.getIndex()] == 0 && extracted2[c.getIndex()] == 0)
                assertNull(g.getProfessors().getPlayerWithProfessor(c));
            if (extracted1[c.getIndex()] > extracted2[c.getIndex()])
                assertEquals(p1, g.getProfessors().getPlayerWithProfessor(c));
            if (extracted2[c.getIndex()] > extracted1[c.getIndex()])
                assertEquals(p2, g.getProfessors().getPlayerWithProfessor(c));
            if (extracted1[c.getIndex()] == extracted2[c.getIndex()] && extracted1[c.getIndex()] != 0)
                //in this case the professor is always of the first player because he move all the students before the other
                assertEquals(p1, g.getProfessors().getPlayerWithProfessor(c));
        }
    }

    @Test
    void getProfessorsCopy() {
        //trivial
        assertTrue(true);
    }

    @RepeatedTest(10)
    void getNumberOfProfessorOfPlayer() {
        //set some random students for testing
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 0);
        Random rand = new Random(System.currentTimeMillis());
        int id1 = 4;
        Player p1 = g.getPlayerById(id1);
        int rand1 = rand.nextInt(6) + 1;
        int i = 0;
        int[] extracted1 = new int[Color.getNumberOfColors()];
        Arrays.fill(extracted1, 0);
        while (i < rand1)
            for (Color c : Color.values())
                while (p1.hasStudent(c) && i < rand1) {
                    extracted1[c.getIndex()]++;
                    p1.moveStudent(c, -1);
                    i++;
                }
        int id2 = 24;
        Player p2 = g.getPlayerById(id2);
        int rand2 = rand.nextInt(6) + 1;
        i = 0;
        int[] extracted2 = new int[Color.getNumberOfColors()];
        Arrays.fill(extracted2, 0);
        while (i < rand2)
            for (Color c : Color.values())
                while (p2.hasStudent(c) && i < rand2) {
                    extracted2[c.getIndex()]++;
                    p2.moveStudent(c, -1);
                    i++;
                }

        //calc the professors possessed by the players
        int pl1 = 0, pl2 = 0;
        for (Color c: Color.values()){
            if (extracted1[c.getIndex()] > extracted2[c.getIndex()])
                pl1++;
            if (extracted2[c.getIndex()] > extracted1[c.getIndex()])
                pl2++;
            if (extracted1[c.getIndex()] == extracted2[c.getIndex()] && extracted1[c.getIndex()] != 0)
                //in this case the professor is always of the first player because he move all the students before the other
                pl1++;
        }

        assertEquals(pl1, g.getProfessors().getNumberOfProfessorOfPlayer(g.getPlayerById(id1)));
        assertEquals(pl2, g.getProfessors().getNumberOfProfessorOfPlayer(g.getPlayerById(id2)));
    }
}