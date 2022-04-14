package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Minotaur extends Character {

    Minotaur(GameInitializer gameInitializer) {
        super (8, 3, gameInitializer);
        System.out.println("Built Minotaur");
    }

    @Override
    void activateEffect(Object object) {
        //todo
    }

    @Override
    Errors canActivateEffect(Object obj) {
        //todo
        return Errors.NO_ERROR;
    }

    /*
    public Optional<Player> calcInfluence() {

    }
     */
}
