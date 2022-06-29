package it.polimi.ingsw.Enum.Phases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionPhaseTest {

    @Test
    void values() {
        ActionPhase.values();
        assertTrue(true);
    }

    @Test
    void valueOf() {
        ActionPhase.valueOf("NotActionPhase");
        assertTrue(true);
    }
}