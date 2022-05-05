package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;

import java.util.Optional;

final public class Cook extends Character {

    private Optional<Color> color;

    Cook(GameInitializer gameInitializer) {
        super (3, 3, gameInitializer);
        this.color = Optional.empty();
    }

    public Color getColor (){
        return color.orElse(null);
    }

    public Optional<Color> getProfessor(){
        return this.color;
    }

    @Override
    protected void activateEffect(Object color) {
        int colorId = (Integer) color;
        this.color = Optional.ofNullable(Color.getColorById(colorId));
    }

    @Override
    public Errors canActivateEffect(Object color) {
        if (!(color instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = (Integer) color;

        if (!Color.isColorIdValid(colorId))
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
