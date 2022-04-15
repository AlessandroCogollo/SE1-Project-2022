package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Objects;

final class Cleric extends Character{

    private final int[] students;

    Cleric(GameInitializer gameInitializer) {
        super (2, 1, gameInitializer);
        this.students = super.gameInitializer.getBag().drawStudents(4);

        System.out.println("Built Cleric");
        System.out.println("Initialized Cleric with 4 students");
    }

    @Override
    void activateEffect(Object obj) {

        // color number is given by zero position of array, island id is given by first position of array
        int[] studentToIsland = (int[])obj;

        Color c = Color.getColorById(studentToIsland[0]);
        Island i = gameInitializer.getIslands().getIslandFromId(studentToIsland[1]);

        this.students[Objects.requireNonNull(c).getIndex()]--;
        i.addStudent(c);
        int[] newStudent= super.gameInitializer.getBag().drawStudents(1);
        for (Color color: Color.values())
            this.students[color.getIndex()] += newStudent[color.getIndex()];
    }

    @Override
    Errors canActivateEffect(Object obj) {

        int[] studentToIsland = (int[])obj;

        int colorId = studentToIsland[0];
        int islandId = studentToIsland[1];

        if (studentToIsland.length > 3)
            return Errors.ILLEGAL_INPUT;
        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;
        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;
        if (this.students[colorId] <= 0)
            return Errors.NO_STUDENT;

        return Errors.NO_ERROR;
    }
}
