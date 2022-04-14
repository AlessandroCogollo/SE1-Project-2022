package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Knight extends Character {

    Knight(GameInitializer gameInitializer) {
        super (7, 2, gameInitializer);
        System.out.println("Built Knight");
    }

    @Override
    void activateEffect(Object object) {}

    @Override
    Errors canActivateEffect(Object obj) {
        return Errors.NO_ERROR;
    }
}
