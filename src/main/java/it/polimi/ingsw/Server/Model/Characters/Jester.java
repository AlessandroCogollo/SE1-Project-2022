package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.School;

import java.util.Arrays;

final public class Jester extends Character {
    private final int[] students;

    Jester(GameInitializer gameInitializer) {
        super (6, 1, gameInitializer);
        this.students = super.gameInitializer.getBag().drawStudents(6);
    }

    @Override
    protected void activateEffect(Object obj) {

        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        int[] chosenColor = (int[]) obj;

        for (int i = 0; i <= chosenColor.length; i++) {
            if (i%2==0) {
                // even elements are those to be removed from this card
                this.students[i]--;
                currSchool.moveStudentToEntrance(Color.getColorById(i));
            } else {
                // odd elements are those to be added to this card
                this.students[i]++;
                currSchool.moveStudentFromEntrance(Color.getColorById(i));
            }
        }
    }

    // used for testing
    public int[] getStudents() {
        return this.students;
    }

    public int[] getStudentsCopy(){
        return Arrays.copyOf(students, students.length);
    }

    @Override
    public Errors canActivateEffect(Object obj) {

        if (!(obj instanceof int[]))
            return Errors.NOT_RIGHT_PARAMETER;

        int[] tempStudents = this.students;

        int length = ((int[]) obj).length;

        if(length%2 == 1) {
            return Errors.ILLEGAL_INPUT;
        }

        for (int i = 0; i <= length; i++) {
            if (i%2==0) {
                // even elements are those to be removed from this card
                tempStudents[i]--;
            } else {
                // odd elements are those to be added to this card
                tempStudents[i]++;
            }
        }

        for (int i : tempStudents) {
            if (i < 0) {
                return Errors.NOT_ENOUGH_TOKEN;
            }
        }

        return Errors.NO_ERROR;
    }
}
