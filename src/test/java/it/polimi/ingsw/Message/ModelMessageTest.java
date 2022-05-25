package it.polimi.ingsw.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.ModelMessageBuilder;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.GameInitializerTest;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
class ModelMessageTest {

    @RepeatedTest(1) //for test over more type of character
    void toJsonObject() {
        ModelMessageBuilder b = ModelMessageBuilder.getModelMessageBuilder();
        Gson gson = new GsonBuilder().create();

        GameInitializer g = GameInitializerTest.setGameInitializer(4, 1);
        b.setGameInitializer(g);

        ModelMessage m = b.buildModelMessage(Errors.NO_ERROR);
        System.out.println(gson.toJson(m));

        GameInitializer g2 = GameInitializerTest.setGameInitializer(4, 0);
        b.setGameInitializer(g2);

        ModelMessage m2 = b.buildModelMessage(Errors.NO_ERROR);
        System.out.println(gson.toJson(m2));

        String J = gson.toJson(m2);

        Message m3 = gson.fromJson(J, Message.class);
        ModelMessage m4 = gson.fromJson(J, ModelMessage.class);
        System.out.println(m4.gameIsOver());
        System.out.println(m3.getError());
    }
}