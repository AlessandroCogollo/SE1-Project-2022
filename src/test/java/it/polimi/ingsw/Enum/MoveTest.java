package it.polimi.ingsw.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveTest {

    @Test
    void getCode() {
        for (Move m: Move.values()){
            assertTrue(m.getCode() >= 0);
        }
    }

    @Test
    void getDescription() {
        for (Move m: Move.values()){
            assertNotNull(m.getDescription());
        }
    }

    @Test
    void values() {
        // enum specific
        Move.values();
        assertTrue(true);
    }

    @Test
    void valueOf() {
        // enum specific
        Move.valueOf("PLAY_ASSISTANT");
        assertTrue(true);
    }
}