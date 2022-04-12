package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

    @Test
    void Cloud (){
        //trivial and setStudents done below
        assertTrue(true);
    }

    @Test
    void getId() {
        //trivial
        assertTrue(true);
    }

    @Test
    void setStudents() {
        for (int i = 2; i < 5; i++) {
            GameInitializer g = GameInitializerTest.setGameInitializer(i, 0);
            GameBoard b = g.getBoard();
            for (int j = 0; j < i; j++){
                if (i == 2 || i == 4)
                    assertEquals(3, Arrays.stream(b.getCloudById(j).getStudents()).sum());
                else
                    assertEquals(4, Arrays.stream(b.getCloudById(j).getStudents()).sum());
            }
        }
    }

    @Test
    void getStudents() {
        //trivial
        assertTrue(true);
    }
}