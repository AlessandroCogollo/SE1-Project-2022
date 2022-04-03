package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Cook extends Character {
    private boolean isChangingMethods;
    private Color color;

    public void activateEffect(Optional<Object> object) {
        System.out.println("Cook");
    }
}
