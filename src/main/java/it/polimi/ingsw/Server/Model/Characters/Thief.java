package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Player;

final public class Thief extends Character {

    Thief(GameInitializer gameInitializer) {
        super (11, 3, gameInitializer);
    }

    @Override
    protected void activateEffect(Object obj) {

        int colorId = (Integer) obj;
        Color c = Color.getColorById(colorId);

        for (Player p : gameInitializer) {
            int tempCount = 3;
            while ((p.getSchool().getNumberOfStudentInRoomByColor(c) > 0) && (tempCount > 0)) {
                p.getSchool().removeStudentFromRoom(c);
                super.gameInitializer.getBag().addStudents(c);
                tempCount--;
            }
        }
    }

    @Override
    public Errors canActivateEffect(Object obj) {
        if (!(obj instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = (Integer) obj;

        if (!Color.isColorIdValid(colorId))
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
