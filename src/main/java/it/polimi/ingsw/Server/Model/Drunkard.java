package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Drunkard extends Character  {

    Drunkard(GameInitializer gameInitializer) {
        super (4, 2, gameInitializer);
    }

    @Override
    void activateEffect(Object object) {
        // calcInfluence() method already implemented in GameBoard
    }

    Color getColor(){
        return Color.Blue;
    }

    @Override
    Errors canActivateEffect(Object obj) {
        // no further checks needed
        return Errors.NO_ERROR;
    }
}
