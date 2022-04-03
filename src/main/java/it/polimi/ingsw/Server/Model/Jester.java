package it.polimi.ingsw.Server.Model;
import java.util.Objects;
import java.util.Optional;

public class Jester extends Character {
    private int[] students;
    private boolean isChangingMethods;
    public void activateEffect(Optional<Object> object) {
        System.out.println("Jester");
    }
}
