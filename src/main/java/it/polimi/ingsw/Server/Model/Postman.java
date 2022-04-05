package it.polimi.ingsw.Server.Model;

public class Postman extends Character {

    Postman() {
        super.id = 9;
        super.isChangingMethods = true;
        super.cost = 1;
    }

    @Override
    public void activateEffect(Object object) {
        System.out.println("Postman");
    }

    public int getMaxMovement() {
        return 0;
    }

}
