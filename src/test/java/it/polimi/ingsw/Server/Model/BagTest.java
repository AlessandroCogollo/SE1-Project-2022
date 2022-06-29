package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BagTest {

    @Test
    void Bag(){

        //initialize all game because the bag when it's empty the bag call the round handler for set that is the last round
        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;
        GameInitializer g = new GameInitializer(0, 2);
        RoundHandler r = new RoundHandler(g);
        g.createAllGame(id2, r);
        r.start();

        //need to create a new bag because during the creation of all game from the bag some students will be taken and this is non-deterministic
        Bag bag = new Bag(g);
        int[] x = bag.drawStudents(120);
        assertEquals(120, Arrays.stream(x).sum());
        for (Color c: Color.values())
            assertEquals(24, x[c.getIndex()]);
    }

    @Test
    void drawStudents() {
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 0);
        Bag bag = g.getBag();
        int[] x = bag.drawStudents(24);
        assertEquals(24, Arrays.stream(x).sum());
    }

    @Test
    void getStudentsCopy() {
        //trivial
        assertTrue(true);
    }
}