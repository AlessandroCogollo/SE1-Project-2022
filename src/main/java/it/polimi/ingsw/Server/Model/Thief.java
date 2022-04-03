package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Thief extends Character {
    private boolean isChangingMethod;
    public void activateEffect(Optional<Object> object) {
        System.out.println("Thief");
    }
}
