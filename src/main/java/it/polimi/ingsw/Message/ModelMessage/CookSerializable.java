package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.Characters.Cook;

public final class CookSerializable extends CharacterSerializable {
    private final int colorId;

    public CookSerializable(Cook c) {
        super(c);
        Color temp = c.getColor();
        if (temp == null)
            this.colorId = -1;
        else
            this.colorId = temp.getIndex();
    }

    public int getColorId() {
        return colorId;
    }
}
