package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Optional;

final class Cook extends Character {

    private Optional<Color> color;

    Cook(GameInitializer gameInitializer) {
        super (3, 3, gameInitializer);
        this.color = Optional.empty();
    }

    Color getColor (){
        return color.orElse(null);
    }

    Optional<Color> getProfessor(){
        return this.color;
    }

    @Override
    void activateEffect(Object color) {
        int colorId = (Integer) color;
        this.color = Optional.ofNullable(Color.getColorById(colorId));
    }

    @Override
    Errors canActivateEffect(Object color) {
        if (!(color instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = (Integer) color;

        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
