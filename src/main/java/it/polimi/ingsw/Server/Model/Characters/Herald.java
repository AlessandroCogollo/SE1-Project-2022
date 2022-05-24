package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.*;

final public class Herald extends Character {

    Herald(GameInitializer gameInitializer) {
        super (5, 3, gameInitializer, "Herald");
    }

    @Override
    protected void activateEffect(Object obj) {

        int islandId = (Integer)obj;
        Island i = gameInitializer.getIslands().getIslandFromId(islandId);

        gameInitializer.getBoard().calcInfluence(i);
    }

    @Override
    public Errors canActivateEffect(Object island) {
        if (!(island instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int islandId = (Integer)island;

        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;

        return Errors.NO_ERROR;
    }
}
