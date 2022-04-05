package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Bard extends Character {

    public Bard() {
        super.id = 1;
        super.isChangingMethods = false;
        super.cost = 1;
        // todo temporary, add Game

        System.out.println("Built Bard");
    }

    @Override
    public void activateEffect(Object object) {
        // if Player has students in entrance
        // game.round.getCurrent();
        // remove max 2 students from entrance
        // put them in room
        // remove max 2 students from room
        // put them in entrance
    }
}
