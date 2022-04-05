package it.polimi.ingsw.Server.Model;

public class Thief extends Character {

    Thief() {
        super.id = 11;
        super.cost = 3;
        super.isChangingMethods = false;
        System.out.println("Thief");
    }

    public void activateEffect(Object color) {
        // every player has to move 3 students from his room
    }
}
