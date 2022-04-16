package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Herald extends Character {

    Herald(GameInitializer gameInitializer) {
        super (5, 3, gameInitializer);
    }

    @Override
    void activateEffect(Object obj) {

        int islandId = (Integer)obj;
        Island i = gameInitializer.getIslands().getIslandFromId(islandId);

        gameInitializer.getBoard().calcInfluence(i);

        System.out.println("Herald set influence on island N° : " + i.getId());
        System.out.println("Tower Count: " + i.getTowerCount());
        System.out.println("Tower Color: " + i.getTowerColor());
    }

    @Override
    Errors canActivateEffect(Object island) {
        if (!(island instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int islandId = (Integer)island;

        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;

        return Errors.NO_ERROR;
    }
}
