package it.polimi.ingsw.Server.Model.Characters;
import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.GameInitializer;

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
