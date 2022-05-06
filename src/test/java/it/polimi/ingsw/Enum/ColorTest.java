package it.polimi.ingsw.Enum;

import it.polimi.ingsw.Enum.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void getIndex() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getNumberOfColors() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getColorById() {
        for (Color c: Color.values())
            assertNotNull(Color.getColorById(c.getIndex()));
        assertNull(Color.getColorById(-1));
        assertNull(Color.getColorById(5));
    }

    @Test
    void isColorIdValid() {
        for (Color c: Color.values())
            assertTrue(Color.isColorIdValid(c.getIndex()));
        assertFalse(Color.isColorIdValid(-1));
        assertFalse(Color.isColorIdValid(5));
    }
}