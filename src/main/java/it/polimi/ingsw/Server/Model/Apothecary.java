package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Apothecary extends Character {

    private int id;
    private int banCard;
    private boolean isChangingMethods;
    //private Game game;

    public Apothecary () {
        this.banCard = 4;
        this.id = 1;
        this.isChangingMethods = false;
        //this.game = game;
    }

    @Override
    public void activateEffect(Optional<Object> island) {
        // temporary, used to check if factory method is working
        System.out.println("Apothecary");
    }
}