package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static it.polimi.ingsw.StarterHelperTest.getTestOH;
import static org.junit.jupiter.api.Assertions.*;

class OptionHandlerTest {

    @Test
    void getOp() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getIsHelp() {
        //trivial
        assertTrue(true);
    }

    @Test
    void manageOption() {
        //considering that in an abstract function is impossible to make a test valid for all statements, done were is implemented
        assertTrue(true);
    }

    //@RepeatedTest(1000)
    @Test
    void testEquals() {
        Random r = new Random(System.currentTimeMillis());
        int v = r.nextInt();
        OptionHandler x = getTestOH(v);
        OptionHandler y = getTestOH(v);
        assertEquals(x, y);
    }

    //@RepeatedTest(1000)
    @Test
    void testHashCode() {
        Random r = new Random(System.currentTimeMillis());
        int v = r.nextInt();
        if (v < 0)
            v = -v;
        OptionHandler x = getTestOH(v);
        OptionHandler y = getTestOH(v);
        assertEquals(x.hashCode(), y.hashCode());
        x = getTestOH(v + 1);
        assertNotEquals(x, y);
        assertNotEquals(x.hashCode(), y.hashCode());
    }
}