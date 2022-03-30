package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedPlayerTest extends PlayerTest{

    @Test   //only testing the coin
    void moveStudent() {
        Bag bag = new Bag() {
            @Override
            public int[] drawStudents(int entranceStudent) {
                int[] student = new int[Color.getNumberOfColors()];
                for (int i = 0; i < entranceStudent; i++) {
                    student[0]++;
                }
                return student;
            }
        };
        School s = new School(8, 7, bag);
        AdvancedPlayer p = (AdvancedPlayer) getPlayer(1, s, null, 1);
        assertEquals(1, p.getCoins());
        Movement m = new Movement(Color.getColorById(0), null);
        p.moveStudent(m);
        p.moveStudent(m);
        p.moveStudent(m);
        assertEquals(2, p.getCoins());
    }

    @Test
    void moveMotherNature() {
        //todo, need GameBoard
    }

    @Test
    void playCharacter() {
        //todo need Character
    }
}