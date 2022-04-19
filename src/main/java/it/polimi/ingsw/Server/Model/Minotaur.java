package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Minotaur extends Character {

    Minotaur(GameInitializer gameInitializer) {
        super (8, 3, gameInitializer);
    }

    @Override
    void activateEffect(Object object) {
        // calcInfluence() method already implemented in GameBoard
    }

    @Override
    Errors canActivateEffect(Object obj) {
        // no further checks needed
        return Errors.NO_ERROR;
    }

}
