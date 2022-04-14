package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Drunkard extends Character  {

    Drunkard(GameInitializer gameInitializer) {
        super (4, 2, gameInitializer);
    }

    @Override
    void activateEffect(Object object) {
        //todo method changing
        System.out.println("Drunkard");
    }
    //todo getColor method and choosing method
    Color getColor(){
        return Color.Blue;
    }

    @Override
    Errors canActivateEffect(Object obj) {
        return Errors.NO_ERROR;
    }
}
