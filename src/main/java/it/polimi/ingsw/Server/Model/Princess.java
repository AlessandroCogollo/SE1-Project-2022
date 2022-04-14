package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Princess extends Character {
    private final int[] students;

    Princess(GameInitializer gameInitializer) {
        super (10, 2, gameInitializer);
        students = gameInitializer.getBag().drawStudents(4);
    }

    void activateEffect(Object object) {

        // todo implement actual color and player choosing

        int colorId = 0;
        int playerId = 0;

        Color c = Color.getColorById(colorId);
        Player p = super.gameInitializer.getPlayerById(playerId);

        this.students[c.getIndex()]--;

        p.getSchool().moveStudentToRoom(c);

        int[] newStudent= super.gameInitializer.getBag().drawStudents(1);
        for (Color color: Color.values())
            this.students[color.getIndex()] += newStudent[color.getIndex()];
    }

    @Override
    Errors canActivateEffect(Object obj) {
        // todo implement actual color and player choosing

        int colorId = 0;
        int playerId = 0;

        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;
        if (!super.gameInitializer.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST;
        if (this.students[colorId] <= 0)
            return Errors.NO_STUDENT;

        Color c = Color.getColorById(colorId);
        Player p = super.gameInitializer.getPlayerById(playerId);

        if (p.getNumberOfStudentInRoomByColor(c) == 10)
            return Errors.NO_MORE_MOVEMENT;

        return Errors.NO_ERROR;
    }
}
