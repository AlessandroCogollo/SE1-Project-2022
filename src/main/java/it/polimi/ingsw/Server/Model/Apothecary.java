package it.polimi.ingsw.Server.Model;

import java.util.Optional;

class Apothecary extends Character {

    private int banCard;
    //private Game game;

    Apothecary() {
        this.banCard = 4;
        super.id = 1;
        super.isChangingMethods = false;
        //this.game = game;
        System.out.println("Built Apothecary");
    }

    // return number of banCard "token" on this card
    public int getBanCard() { return this.banCard; }

    // used to add a BanCard "token" to this card, after being removed from an island
    public void addBanCard() { this.banCard += 1; }

    // used to remove a BanCard "token" from this card, and to add it to an island
    public void removeBanCard() { this.banCard -= 1; }

    @Override
    public void activateEffect(Object island) {

        // add banCard to island
        ((Island)island).setBanCard();

        // remove banCard from this
        this.removeBanCard();

        // used for debug
        System.out.println("BanCard on this: " + this.getBanCard());
        System.out.println("BanCard on Island N°" + ((Island)island).getId() + ": " + ((Island)island).getBanCard());
    }
}