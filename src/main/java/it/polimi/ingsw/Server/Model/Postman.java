package it.polimi.ingsw.Server.Model;
import java.util.Optional;

public class Postman extends Character {
    private boolean isChangingMethod;

    @Override
    public void activateEffect(Optional<Object> object) {
        System.out.println("Postman");
    }

    /*
    public Optional<Player> calcInfluence() {}
    */
}
