package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Bard extends Character {

    private int id = 1;
    private boolean isChangingMethods = false;

    public Bard() {

    }

    @Override
    public void activateEffect(Optional<Object> object) {
        System.out.println("Bard");
    }
}
