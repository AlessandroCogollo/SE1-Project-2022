package it.polimi.ingsw.Server.Model;
import java.util.Optional;

public class Knight extends Character {

    //private Game game;

    Knight() {
        this.id = 7;
        super.isChangingMethods = true;
    }

    /*
    public Optional<Player> calcInfluence() {
        return Player;
    }
    */

    @Override
    public void activateEffect(Object object) {
        System.out.println("Knight");
    }
}
