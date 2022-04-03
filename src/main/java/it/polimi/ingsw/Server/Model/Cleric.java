package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Cleric extends Character{

    private int[] students;

    private int id;
    private boolean isChangingMethod;
    //private Game game;

    public Cleric() {
        this.id = 4;
        this.isChangingMethod = true;
        //this.game = game;
    }

    @Override
    public void activateEffect(Optional<Object> object) {
        System.out.println("Cleric");
    }
}
