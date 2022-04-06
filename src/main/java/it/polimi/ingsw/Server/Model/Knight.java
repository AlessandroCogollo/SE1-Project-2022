package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Knight extends Character {

    //private Game game;

    Knight(GameInitializer gameInitializer) {
        super (7, 2, gameInitializer);
    }

    /*
    public Optional<Player> calcInfluence() {
        return Player;
    }
    */

    @Override
    void activateEffect(Object object) {
        //todo
        System.out.println("Knight");
    }

    @Override
    Errors canActivateEffect(Object obj) {
        //todo
        return Errors.NO_ERROR;
    }
}
