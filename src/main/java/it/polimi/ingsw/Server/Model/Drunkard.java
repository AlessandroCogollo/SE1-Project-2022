package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Drunkard extends Character  {

    Drunkard(GameInitializer gameInitializer) {
        super (4, 2, gameInitializer);
        System.out.println("Built Drunkard");
    }

    @Override
    void activateEffect(Object object) {
        // TODO: with calcInfluence()
    }
    //todo getColor method and choosing method
    Color getColor(){
        return Color.Blue;
    }

    @Override
    Errors canActivateEffect(Object obj) {
        // TODO: with calcInfluence()
        return Errors.NO_ERROR;
    }
}
