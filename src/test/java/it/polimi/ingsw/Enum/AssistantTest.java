package it.polimi.ingsw.Enum;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AssistantTest {

    @Test
    void getValue() {
        for (Assistant a : Assistant.values())
            assertTrue(a.getValue() > 0);
    }

    @Test
    void getMaxMovement() {
        for (Assistant a : Assistant.values())
            assertTrue(a.getMaxMovement() > 0);
    }

    @Test
    void getNumberOfAssistants() {
        assertEquals(Assistant.values().length, Assistant.getNumberOfAssistants());
    }

    @Test
    void getAssistantByValue() {
        for (Assistant a : Assistant.values())
            assertNotNull(Assistant.getAssistantByValue(a.getValue()));
        assertNull(Assistant.getAssistantByValue(0));
        assertNull(Assistant.getAssistantByValue(11));
    }

    @Test
    void getNewAssistantDeck() {
        Collection<Assistant> x = Assistant.getNewAssistantDeck();
        boolean all = true;
        for (Assistant a : Assistant.values()) {
            if (!x.contains(a)) {
                all = false;
                break;
            } else
                x.remove(a);
        }
        assertTrue(all);
        assertEquals(0, x.size());
    }

    @Test
    void values() {
        // enum specific
        Assistant.values();
        assertTrue(true);
    }

    @Test
    void valueOf() {
        // enum specific
        Assistant.valueOf("Lion");
        assertTrue(true);
    }
}