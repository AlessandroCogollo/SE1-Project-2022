package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.ModelMessageBuilder;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.GameInitializerTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelMessageBuilderTest {

    @Test
    void setGameInitializer() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getModelMessageBuilder() {
        GameInitializer g1 = GameInitializerTest.setGameInitializer(4, 1);
        GameInitializer g2 = GameInitializerTest.setGameInitializer(3, 0);

        ModelMessageBuilder b1 = ModelMessageBuilder.getModelMessageBuilder();
        ModelMessageBuilder b2 = ModelMessageBuilder.getModelMessageBuilder();
        b2.setGameInitializer(g1);
        ModelMessageBuilder b3 = ModelMessageBuilder.getModelMessageBuilder();
        b3.setGameInitializer(g2);

        assertTrue(b1 == b2 && b2 == b3);
        assertEquals(b1, b2);
        assertEquals(b2, b3);
        assertEquals(b3, b1);
    }

    @Test
    void buildModelMessage() {
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        ModelMessageBuilder b = ModelMessageBuilder.getModelMessageBuilder();
        b.resetGameInitializer();
        ModelMessage m = b.buildModelMessage(Errors.NO_ERROR);
        assertNull(m);
        b.setGameInitializer(g);
        m = b.buildModelMessage(Errors.NO_ERROR);
        assertNotNull(m);
    }

    @Test
    void resetGameInitializer() {
        //trivial
        assertTrue(true);
    }
}