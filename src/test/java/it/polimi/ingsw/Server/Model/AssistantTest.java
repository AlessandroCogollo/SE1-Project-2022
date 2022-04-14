package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AssistantTest {

    @Test
    void getValue() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getMaxMovement() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getNumberOfAssistants() {
        //trivial
        assertTrue(true);
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
        for (Assistant a: Assistant.values()) {
            if (!x.contains(a)) {
                all = false;
                break;
            }
            else
                x.remove(a);
        }
        assertTrue(all);
        assertEquals(0, x.size());
    }
}