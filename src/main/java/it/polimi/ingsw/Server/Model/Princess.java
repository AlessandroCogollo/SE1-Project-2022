package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Objects;

final class Princess extends Character {
    private final int[] students;

    Princess(GameInitializer gameInitializer) {
        super (10, 2, gameInitializer);
        students = gameInitializer.getBag().drawStudents(4);
    }

    void activateEffect(Object obj) {

        int colorId = (int) obj;

        Color c = Color.getColorById(colorId);
        Player p = super.gameInitializer.getRoundHandler().getCurrent();

        this.students[colorId]--;

        p.getSchool().moveStudentToRoom(Objects.requireNonNull(c));

        int[] newStudent = super.gameInitializer.getBag().drawStudents(1);
        for (Color color: Color.values())
            this.students[color.getIndex()] += newStudent[color.getIndex()];
    }

    @Override
    Errors canActivateEffect(Object obj) {
        // todo implement actual color and player choosing

        int colorId = (int) obj;
        Player p = super.gameInitializer.getRoundHandler().getCurrent();

        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;
        if (!super.gameInitializer.existsPlayer(p.getId()))
            return Errors.PLAYER_NOT_EXIST;
        if (this.students[colorId] <= 0)
            return Errors.NO_STUDENT;

        Color c = Color.getColorById(colorId);

        if (p.getNumberOfStudentInRoomByColor(c) == 10)
            return Errors.NO_MORE_MOVEMENT;

        return Errors.NO_ERROR;
    }
}
