package it.polimi.ingsw.Server.Model;
import java.util.Optional;

public class Minotaur extends Character {
    private boolean isChangingMethods;

    @Override
    public void activateEffect(Optional<Object> object) {
        System.out.println("Minotaur");
    }
    /*
    public Optional<Player> calcInfluence() {

    }
     */
}
