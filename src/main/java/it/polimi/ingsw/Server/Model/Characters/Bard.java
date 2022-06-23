package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
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
    Bard(GameInitializer gameInitializer, CharacterSerializable character) {
        super (gameInitializer, character);
    }

    @Override
    protected void activateEffect(int[] chosenColors) {
        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        // ie: chosenColors = { removeFromEntrance1, removeFromRoom1, removeFromEntrance2, removeFromRoom2 }

        for (int i = 0; i < chosenColors.length; i++) {
            if (i % 2 == 0) // even indexes: used to remove a student from entrance & move it to room
                currSchool.addStudentToRoom(currSchool.removeStudentFromEntrance(Objects.requireNonNull(Color.getColorById(chosenColors[i]))));
            else // odd indexes: used to remove a student from room & move it to entrance
                currSchool.addStudentToEntrance(currSchool.removeStudentFromRoom(Objects.requireNonNull(Color.getColorById(chosenColors[i]))));
        }
    }

    @Override
    public Errors canActivateEffect(int[] chosenColors) {

        if (chosenColors.length != 2 && chosenColors.length != 4)
            return Errors.NOT_RIGHT_PARAMETER;

        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        int[] room = currSchool.getCopyOfRoom();
        int[] entrance = currSchool.getCopyOfEntrance();

        int need;
        if (chosenColors.length == 2)
            need = 1;
        else
            need = 2;

        if (Arrays.stream(room).sum() < need || Arrays.stream(entrance).sum() < need)
            return Errors.NOT_ENOUGH_TOKEN;

        int en1 = chosenColors[0];
        int ro1 = chosenColors[1];

        if (entrance[en1] < 1 || room[ro1] < 1)
            return Errors.NOT_ENOUGH_TOKEN;


        if (chosenColors.length == 4){
            entrance[en1]--;
            room[ro1]--;

            int en2 = chosenColors[2];
            int ro2 = chosenColors[3];

            if (entrance[en2] < 1 || room[ro2] < 1)
                return Errors.NOT_ENOUGH_TOKEN;
        }


        return Errors.NO_ERROR;
    }
}
