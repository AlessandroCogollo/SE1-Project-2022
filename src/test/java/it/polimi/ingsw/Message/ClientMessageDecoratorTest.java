package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientMessageDecoratorTest {

    @Test
    void message() {
        ClientMessageDecorator r = new ClientMessageDecorator(new PlayAssistantMessage(Errors.NO_ERROR, "TEST", 1), 0);
        assertTrue(r.message() instanceof PlayAssistantMessage);
    }

    @Test
    void playerId() {
        ClientMessageDecorator r = new ClientMessageDecorator(new PlayAssistantMessage(Errors.NO_ERROR, "TEST", 1), 0);
        assertEquals(0, r.playerId());
    }
}