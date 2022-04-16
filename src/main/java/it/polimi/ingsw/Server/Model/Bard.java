package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Objects;

final class Bard extends Character {

    Bard(GameInitializer gameInitializer) {
        super (1, 1, gameInitializer);
        System.out.println("Built Bard");
    }

    @Override
    void activateEffect(Object obj) {
        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        // ie: chosenColors = { removeFromEntrance1, removeFromRoom1, removeFromEntrance2, removeFromRoom2 }

        int[] chosenColors = (int[])obj;
        for (int i = 0; i < chosenColors.length; i++) {
            if (i%2 == 0) {
                // even indexes: used to remove a student from entrance & move it to room
                currSchool.moveStudentToRoom(currSchool.moveStudentFromEntrance(Objects.requireNonNull(Color.getColorById(chosenColors[i]))));
            } else {
                // odd indexes: used to remove a student from room & move it to entrance
                currSchool.moveStudentToEntrance(currSchool.removeStudentFromRoom(Objects.requireNonNull(Color.getColorById(chosenColors[i]))));
            }
        }
    }

    @Override
    Errors canActivateEffect(Object obj) {

        if (!(obj instanceof int[] chosenColors))
            return Errors.NOT_RIGHT_PARAMETER;

        if (chosenColors.length == 0 || chosenColors.length >= 4 || chosenColors.length%2 == 1) {
            return Errors.ILLEGAL_INPUT;
        }

        return Errors.NO_ERROR;
    }
}
