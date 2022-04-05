package it.polimi.ingsw.Server.Model;
import java.util.Optional;

public class Minotaur extends Character {

    Minotaur() {
        super.id = 8;
        super.isChangingMethods = true;
        super.cost = 3;
    }

    @Override
    public void activateEffect(Object object) {
        System.out.println("Minotaur");
    }

    /*
    public Optional<Player> calcInfluence() {

    }
     */
}
