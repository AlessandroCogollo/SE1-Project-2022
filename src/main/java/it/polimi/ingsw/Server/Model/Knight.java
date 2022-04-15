package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Knight extends Character {

    Knight(GameInitializer gameInitializer) {
        super (7, 2, gameInitializer);
        System.out.println("Built Knight");
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
