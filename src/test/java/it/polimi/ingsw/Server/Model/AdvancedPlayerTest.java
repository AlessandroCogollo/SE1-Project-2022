package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedPlayerTest extends PlayerTest{

    @Test   //only testing the coin
    void moveStudent() {
        Bag bag = new Bag(null);
        School s = new School(8, 7, bag);
        AdvancedPlayer p = (AdvancedPlayer) getPlayer(1, s, null, 1);
        assertEquals(1, p.getCoins());
        p.moveStudent(Color.getColorById(0), -1);
        p.moveStudent(Color.getColorById(0), -1);
        p.moveStudent(Color.getColorById(0), -1);
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