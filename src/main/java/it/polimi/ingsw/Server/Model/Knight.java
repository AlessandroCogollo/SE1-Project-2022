package it.polimi.ingsw.Server.Model;
import java.util.Optional;

public class Knight extends Character {

    private int id;
    private boolean isChangingMethod;
    //private Game game;

    Knight() {
        this.id = 7;
        this.isChangingMethod = true;
    }

    /*
    public Optional<Player> calcInfluence() {
        return Player;
    }
    */

    @Override
    public void activateEffect(Optional<Object> object) {
        System.out.println("Knight");
    }
}
