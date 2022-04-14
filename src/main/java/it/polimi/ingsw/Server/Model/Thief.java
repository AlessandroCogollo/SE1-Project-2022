package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Thief extends Character {

    Thief(GameInitializer gameInitializer) {
        super (11, 3, gameInitializer);

        System.out.println("Thief");
    }

    @Override
    void activateEffect(Object obj) {

        int colorId = (Integer) obj;

        Color c = Color.getColorById(colorId);

        //todo method implementation

        // every player has to move 3 students from his room
    }

    @Override
    Errors canActivateEffect(Object obj) {
        if (!(obj instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = (Integer) obj;

        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
