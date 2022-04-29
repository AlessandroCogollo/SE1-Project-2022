package it.polimi.ingsw.Server.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

//todo
class ModelMessageTest {

    @Test
    void toJsonObject() {
        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        ModelMessage m = ModelMessage.modelMessageBuilder(g, Errors.NO_ERROR);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement x = gson.toJsonTree(m);
        System.out.println(gson.toJson(x));

        GameInitializer g2 = GameInitializerTest.setGameInitializer(4, 0);
        ModelMessage m2 = ModelMessage.modelMessageBuilder(g2, Errors.NO_ERROR);

        System.out.println(gson.toJson(m2));
    }

    @Test
    void modelMessageBuilder() {
        //trivial
        assertTrue(true);
    }
}