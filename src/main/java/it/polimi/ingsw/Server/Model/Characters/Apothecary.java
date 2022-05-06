package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.*;

final public class Apothecary extends Character {

    private int banCard;

    //not public only created in factory in Character class
    Apothecary(GameInitializer gameInitializer) {
        super (0, 2, gameInitializer);
        this.banCard = 4;
    }

    // used to add a BanCard "token" to this card (after being removed from an island)
    public void addBanCard() { this.banCard += 1; }

    // used to remove a BanCard "token" from this card (and to add it to an island)
    public void removeBanCard() { this.banCard -= 1; }

    // return number of banCard "token" on this card
    public int getBanCard() { return this.banCard; }

    @Override
    public void activateEffect(Object obj) {

        int islandId = (Integer)obj;

        Island i = gameInitializer.getIslands().getIslandFromId(islandId);

        // remove banCard from this
        removeBanCard();

        // add banCard to island
        i.setBanCard();

    }

    @Override
    public Errors canActivateEffect(Object obj) {
        if (!(obj instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int islandId = (Integer)obj;

        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;
        if (this.banCard <= 0)
            return Errors.NOT_ENOUGH_TOKEN;
        return Errors.NO_ERROR;
    }

}