package it.polimi.ingsw.Server.Model;

public class Drunkard extends Character  {

    Drunkard() {
        super.isChangingMethods = true;
    }

    @Override
    public void activateEffect(Object object) {
        System.out.println("Drunkard");
    }

    /*
    public Optional<Player> calcInfluence() {
        return null;
    }
    */
}
