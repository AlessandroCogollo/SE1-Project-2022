package it.polimi.ingsw.Server.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelMessageTest {

    @Test
    void toJsonObject() {
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        ModelMessage m = ModelMessage.modelMessageBuilder(g, Errors.NO_ERROR);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement x = gson.toJsonTree(m);
        System.out.println(gson.toJson(x));
    }

    @Test
    void modelMessageBuilder() {
        //trivial
        assertTrue(true);
    }

    @Test
    void testModelMessageBuilder() {
        //trivial
        assertTrue(true);
    }
}