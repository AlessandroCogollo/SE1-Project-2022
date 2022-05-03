package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;

final public class Drunkard extends Character  {

    Drunkard(GameInitializer gameInitializer) {
        super (4, 2, gameInitializer);
    }

    @Override
    protected void activateEffect(Object object) {
        // calcInfluence() method already implemented in GameBoard
    }

    public Color getColor(){
        return Color.Blue;
    }

    @Override
    public Errors canActivateEffect(Object obj) {
        // no further checks needed
        return Errors.NO_ERROR;
    }
}
