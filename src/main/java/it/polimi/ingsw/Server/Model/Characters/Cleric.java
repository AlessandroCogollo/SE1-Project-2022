package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Island;

import java.util.Arrays;
import java.util.Objects;

final public class Cleric extends Character{

    private final int[] students;

    Cleric(GameInitializer gameInitializer) {
        super (2, 1, gameInitializer, "Cleric");
        this.students = super.gameInitializer.getBag().drawStudents(4);
    }

    public int[] getStudentsCopy(){
        return Arrays.copyOf(students, students.length);
    }
    // used for testing
    public int[] getStudentsNumber() {
        return this.students;
    }

    @Override
    protected void activateEffect(Object obj) {

        // color number is given by zero position of array, island id is given by first position of array
        int[] studentToIsland = (int[]) obj;

        Color c = Color.getColorById(studentToIsland[0]);
        Island i = gameInitializer.getIslands().getIslandFromId(studentToIsland[1]);

        this.students[Objects.requireNonNull(c).getIndex()]--;
        i.addStudent(c);
        int[] newStudent= super.gameInitializer.getBag().drawStudents(1);
        for (Color color: Color.values())
            this.students[color.getIndex()] += newStudent[color.getIndex()];
    }

    @Override
    public Errors canActivateEffect(Object obj) {

        if (! (obj instanceof int[] studentToIsland))
            return Errors.ILLEGAL_INPUT;

        if (studentToIsland.length != 2)
            return Errors.ILLEGAL_INPUT;

        int colorId = studentToIsland[0];
        int islandId = studentToIsland[1];

        if (!Color.isColorIdValid(colorId))
            return Errors.NOT_VALID_COLOR;
        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;
        if (this.students[colorId] <= 0)
            return Errors.NO_STUDENT;

        return Errors.NO_ERROR;
    }
}
