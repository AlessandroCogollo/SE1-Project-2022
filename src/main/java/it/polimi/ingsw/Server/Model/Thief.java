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
    Errors canActivateEffect(Object obj) {
        if (!(obj instanceof Integer))
            return Errors.NOT_RIGHT_PARAMETER;

        int colorId = (Integer) obj;

        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;

        return Errors.NO_ERROR;
    }
}
