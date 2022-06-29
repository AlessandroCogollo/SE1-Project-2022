package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MessageTest {

    @Test
    void getTime() {
        Message m = new Message(Errors.NO_ERROR, "TEST");
        assertNotNull(m.getTime());
    }

    @Test
    void getError() {
        Message m = new Message(Errors.NO_ERROR, "TEST");
        assertEquals(Errors.NO_ERROR, m.getError());
    }

    @Test
    void getMessage() {
        Message m = new Message(Errors.NO_ERROR, "TEST");
        assertEquals("TEST", m.getMessage());
    }
}