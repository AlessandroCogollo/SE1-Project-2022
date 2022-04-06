package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

final class Cook extends Character {

    private Optional<Color> color;

    Cook(GameInitializer gameInitializer) {
        super (3, 3, gameInitializer);
        this.color = Optional.empty();
    }

    @Override
    void activateEffect(Object color) {
        int colorId = (Integer) color;
        this.color = Optional.of(Color.getColorById(colorId));
        //todo set of method
        System.out.println("Color choosen for cook: " + this.color);
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


    //todo splitting calc influence

}
