package it.polimi.ingsw.Server.Model;
import java.util.Optional;

public class Drunkard extends Character  {
    private boolean isChangingMethods;

    @Override
    public void activateEffect(Optional<Object> object) {
        System.out.println("Drunkard");
    }
    /*
    public Optional<Player> calcInfluence() {
        return ;
    }
     */
}
