package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.School;

import java.util.Arrays;
import java.util.Objects;

final public class Jester extends Character {
    private final int[] students;

    Jester(GameInitializer gameInitializer) {
        super (6, 1, gameInitializer, "Jester");
        this.students = super.gameInitializer.getBag().drawStudents(6);
    }

    Jester(GameInitializer gameInitializer, CharacterSerializable character) {
        super (gameInitializer, character);
        this.students = character.getStudents();
    }

    @Override
    protected void activateEffect(int[] chosenColor) {

        Player currPlayer = gameInitializer.getRoundHandler().getCurrent();
        School currSchool = currPlayer.getSchool();

        for (int i = 0; i <= chosenColor.length; i++) {
            if (i%2==0) {
                // even elements are those to be removed from this card
                this.students[i]--;
                currSchool.addStudentToEntrance(Objects.requireNonNull(Color.getColorById(i)));
            } else {
                // odd elements are those to be added to this card
                this.students[i]++;
                currSchool.removeStudentFromEntrance(Objects.requireNonNull(Color.getColorById(i)));
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
    public Errors canActivateEffect(int[] obj) {

        System.err.println("received" + Arrays.toString(obj));

        int length = obj.length;

        if(length == 0 || length % 2 == 1 || length > 6)
            return Errors.NOT_RIGHT_PARAMETER;

        int[] tempStudents = Arrays.copyOf(this.students, this.students.length);

        System.err.println("Jester" + Arrays.toString(tempStudents));

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
