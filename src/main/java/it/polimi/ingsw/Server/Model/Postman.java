package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Postman extends Character {

    Postman(GameInitializer gameInitializer) {
        super (9, 1, gameInitializer);
        System.out.println("Built Postman");
    }

    @Override
    void activateEffect(Object object) {
        // method already implemented in AdvancedGame
    }

    @Override
    Errors canActivateEffect(Object obj) {
        return Errors.NO_ERROR;
    }
}
