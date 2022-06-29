package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Island;

final public class Herald extends Character {

    Herald(GameInitializer gameInitializer) {
        super (5, 3, gameInitializer, "Herald");
    }

    public Herald(GameInitializer gameInitializer, CharacterSerializable character) {
        super(gameInitializer, character);
    }

    @Override
    protected void activateEffect(int[] obj) {

        int islandId = obj[0];
        Island i = gameInitializer.getIslands().getIslandFromId(islandId);

        gameInitializer.getBoard().calcInfluence(i);
    }

    @Override
    public Errors canActivateEffect(int[] island) {
        if (island.length != 1)
            return Errors.NOT_RIGHT_PARAMETER;

        int islandId = island[0];

        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;

        return Errors.NO_ERROR;
    }
}
