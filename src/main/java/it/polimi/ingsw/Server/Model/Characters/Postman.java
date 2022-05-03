package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.GameInitializer;

final public class Postman extends Character {

    Postman(GameInitializer gameInitializer) {
        super (9, 1, gameInitializer);
    }

    @Override
    protected void activateEffect(Object object) {
        // method already implemented in AdvancedGame
    }

    @Override
    public Errors canActivateEffect(Object obj) {
        return Errors.NO_ERROR;
    }
}
