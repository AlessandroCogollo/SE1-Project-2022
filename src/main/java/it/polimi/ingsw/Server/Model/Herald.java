package it.polimi.ingsw.Server.Model;

public class Herald extends Character {

    Herald() {
        super.id = 5;
        super.isChangingMethods = false;
        super.cost = 3;
    }

    @Override
    public void activateEffect(Object island) {
        ((Island) island).CalcInfluence();
        System.out.println("Herald set influence on this island: "
                + ((Island)island).getTowerCount()
                + ((Island) island).getTowerColor()
        );
    }
}
