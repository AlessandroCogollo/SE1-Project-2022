package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final public class Knight extends Character {

    Knight(GameInitializer gameInitializer) {
        super (7, 2, gameInitializer);
    }

    @Override
    protected void activateEffect(Object object) {
        // calcInfluence() method already implemented in GameBoard
    }

    @Override
    public Errors canActivateEffect(Object obj) {
        // no further checks needed
        return Errors.NO_ERROR;
    }
}
