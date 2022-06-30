package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.GameInitializer;

public class ModelMessageBuilder {

    private static ModelMessageBuilder builder = null;

    private GameInitializer gameInitializer;

    public void setGameInitializer(GameInitializer gameInitializer) {
        this.gameInitializer = gameInitializer;
    }

    public void resetGameInitializer (){
        this.gameInitializer = null;
    }

    private ModelMessageBuilder() {
        this.gameInitializer = null;
    }

    public static ModelMessageBuilder getModelMessageBuilder (){
        if (builder == null)
            builder = new ModelMessageBuilder();

        return builder;
    }

    /**
     *     requires GameInitializer not null.
     *     builds the model message with the game initializer in this class
     */
    public ModelMessage buildModelMessage (Errors er){
        if (this.gameInitializer == null)
            return null;
        return new ModelMessage(this.gameInitializer, er);
    }
}
