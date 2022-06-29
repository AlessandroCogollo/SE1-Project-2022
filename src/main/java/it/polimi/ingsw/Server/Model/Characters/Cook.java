package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;

import java.util.Objects;

final public class Cook extends Character {

    private Color color;

    Cook(GameInitializer gameInitializer) {
        super (3, 3, gameInitializer, "Cook");
        this.color = null;
    }

    Cook(GameInitializer gameInitializer, CharacterSerializable character) {
        super (gameInitializer, character);
        this.color = Color.getColorById(character.getColorId());
    }

    public Color getColor (){
        return color;
    }

    @Override
    protected void activateEffect(int[] color) {
        int colorId = color[0];
        this.color = Objects.requireNonNull(Color.getColorById(colorId));
    }

    @Override
    public Errors canActivateEffect(int[] color) {
        if (color.length != 1)
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = color[0];

        if (!Color.isColorIdValid(colorId))
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
