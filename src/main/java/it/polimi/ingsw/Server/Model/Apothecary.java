package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Apothecary extends Character {

    private int banCard;

    Apothecary(GameInitializer gameInitializer) {
        super (0, 2, gameInitializer);
        this.banCard = 4;

        System.out.println("Built Apothecary");
    }



    // used to add a BanCard "token" to this card, after being removed from an island
    void addBanCard() { this.banCard += 1; }

    // used to remove a BanCard "token" from this card, and to add it to an island
    void removeBanCard() { this.banCard -= 1; }

    @Override
    void activateEffect(Object obj) {

        int islandId = (Integer)obj;

        Island i = gameInitializer.getIslands().getIslandFromId(islandId);
        // add banCard to island
        i.setBanCard();

        // remove banCard from this
        removeBanCard();

        // used for debug
        System.out.println("BanCard on this: " + this.getBanCard());
        System.out.println("BanCard on Island N°" + i.getId() + ": " + i.getBanCard());
    }

    @Override
    Errors canActivateEffect(Object obj) {
        if (!(obj instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int islandId = (Integer)obj;

        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;
        if (this.banCard <= 0)
            return Errors.NO_MORE_BANCARD;
        return Errors.NO_ERROR;
    }

    // return number of banCard "token" on this card
    int getBanCard() { return this.banCard; }
}