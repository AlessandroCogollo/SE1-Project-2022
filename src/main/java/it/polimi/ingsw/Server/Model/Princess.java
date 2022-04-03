package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Princess extends Character {
    private boolean isChangingMethods;
    private int[] student;

    public void activateEffect(Optional<Object> object) {
        System.out.println("Princess");
    }
}
