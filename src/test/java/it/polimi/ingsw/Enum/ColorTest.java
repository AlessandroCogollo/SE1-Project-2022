package it.polimi.ingsw.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void getIndex() {
        for (Color c: Color.values())
            assertTrue(c.getIndex() >= 0);
    }

    @Test
    void getNumberOfColors() {
        assertEquals(Color.values().length, Color.getNumberOfColors());
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

    @Test
    void values() {
        // enum specific
        Color.values();
        assertTrue(true);
    }

    @Test
    void valueOf() {
        // enum specific
        Color.valueOf("Yellow");
        assertTrue(true);
    }
}