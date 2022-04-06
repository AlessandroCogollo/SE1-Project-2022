package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Postman extends Character {

    Postman(GameInitializer gameInitializer) {
        super (9, 1, gameInitializer);
    }

    @Override
    void activateEffect(Object object) {
        System.out.println("Postman");
    }

    @Override
    Errors canActivateEffect(Object obj) {
        return Errors.NO_ERROR;
    }
}
