package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.School;

import java.util.Arrays;
import java.util.Objects;

final public class Bard extends Character {

    //not public created only by factory in character class
    Bard(GameInitializer gameInitializer) {
        super (1, 1, gameInitializer, "Bard");
    }

    @Override
    protected void activateEffect(int[] chosenColors) {
        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        // ie: chosenColors = { removeFromEntrance1, removeFromRoom1, removeFromEntrance2, removeFromRoom2 }

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
    public Errors canActivateEffect(int[] chosenColors) {

        if (chosenColors.length == 0 || chosenColors.length > 4 || chosenColors.length%2 == 1)
            return Errors.NOT_RIGHT_PARAMETER;

        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        int[] room = currSchool.getCopyOfRoom();

        if (Arrays.stream(room).sum() < 2)
            return Errors.NOT_ENOUGH_TOKEN;


        return Errors.NO_ERROR;
    }
}
