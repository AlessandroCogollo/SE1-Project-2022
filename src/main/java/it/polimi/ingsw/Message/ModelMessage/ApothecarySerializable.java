package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Characters.Apothecary;

public final class ApothecarySerializable extends CharacterSerializable {

    private final int banCard;

    ApothecarySerializable(Apothecary c) {
        super(c);
        this.banCard = c.getBanCard();
    }

    public int getBanCard() {
        return banCard;
    }
}