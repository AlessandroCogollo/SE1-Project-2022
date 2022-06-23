package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Player;

import java.util.Objects;

final public class Thief extends Character {

    Thief(GameInitializer gameInitializer) {
        super (11, 3, gameInitializer, "Thief");
    }

    public Thief(GameInitializer gameInitializer, CharacterSerializable character) {
        super(gameInitializer, character);
    }

    @Override
    protected void activateEffect(int[] obj) {

        int colorId = obj[0];
        Color c = Color.getColorById(colorId);

        for (Player p : gameInitializer) {
            int tempCount = 3;
            while ((p.getSchool().getNumberOfStudentInRoomByColor(Objects.requireNonNull(c)) > 0) && (tempCount > 0)) {
                p.getSchool().removeStudentFromRoom(c);
                super.gameInitializer.getBag().addStudents(c);
                tempCount--;
            }
        }
    }

    @Override
    public Errors canActivateEffect(int[] obj) {
        if (obj.length != 1)
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = obj[0];

        if (!Color.isColorIdValid(colorId))
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
