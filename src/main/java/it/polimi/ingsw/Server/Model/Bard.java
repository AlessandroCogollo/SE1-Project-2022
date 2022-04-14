package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Bard extends Character {

    Bard(GameInitializer gameInitializer) {
        super (1, 1, gameInitializer);

        System.out.println("Built Bard");
    }

    @Override
    void activateEffect(Object obj) {
        //todo
    }

    @Override
    Errors canActivateEffect(Object obj) {
        //todo
        return Errors.NO_ERROR;
    }
}
