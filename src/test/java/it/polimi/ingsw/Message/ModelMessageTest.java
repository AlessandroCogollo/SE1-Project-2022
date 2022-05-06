package it.polimi.ingsw.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage;
import it.polimi.ingsw.Message.ModelMessageBuilder;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.GameInitializerTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

//todo
class ModelMessageTest {

    @RepeatedTest(10) //for test over more type of character
    void toJsonObject() {
        ModelMessageBuilder b = ModelMessageBuilder.getModelMessageBuilder();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        b.setGameInitializer(g);

        ModelMessage m = b.buildModelMessage(Errors.NO_ERROR);
        System.out.println(gson.toJson(m));

        GameInitializer g2 = GameInitializerTest.setGameInitializer(4, 0);
        b.setGameInitializer(g2);

        ModelMessage m2 = b.buildModelMessage(Errors.NO_ERROR);
        System.out.println(gson.toJson(m2));
    }
}