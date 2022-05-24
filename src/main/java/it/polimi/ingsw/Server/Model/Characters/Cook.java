package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;

import java.util.Optional;

final public class Cook extends Character {

    private Optional<Color> color;

    Cook(GameInitializer gameInitializer) {
        super (3, 3, gameInitializer, "Cook");
        this.color = Optional.empty();
    }

    public Color getColor (){
        return color.orElse(null);
    }

    public Optional<Color> getProfessor(){
        return this.color;
    }

    @Override
    protected void activateEffect(int[] color) {
        int colorId = color[0];
        this.color = Optional.ofNullable(Color.getColorById(colorId));
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
