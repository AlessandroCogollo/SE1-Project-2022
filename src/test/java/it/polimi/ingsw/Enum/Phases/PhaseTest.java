package it.polimi.ingsw.Enum.Phases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PhaseTest {

    @Test
    void values() {
        Phase.values();
        assertTrue(true);
    }

    @Test
    void valueOf() {
        Phase.valueOf("Planning");
        assertTrue(true);
    }
}