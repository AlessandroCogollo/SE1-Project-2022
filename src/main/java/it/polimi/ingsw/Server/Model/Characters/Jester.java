package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;
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

        School currSchool =  gameInitializer.getRoundHandler().getCurrent().getSchool();

        for (int i = 0; i < chosenColor.length; i++) {
            Color c = Objects.requireNonNull(Color.getColorById(chosenColor[i]));
            if (i % 2 == 0) {
                // even elements are those to be removed from this card
                this.students[c.getIndex()]--;
                currSchool.addStudentToEntrance(c);
            } else {
                // odd elements are those to be added to this card
                this.students[c.getIndex()]++;
                currSchool.removeStudentFromEntrance(c);
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

        //System.out.println("received" + Arrays.toString(obj));

        int length = obj.length;

        if(length != 2 && length != 4 && length != 6)
            return Errors.NOT_RIGHT_PARAMETER;

        for (int i : obj) {
            if (!Color.isColorIdValid(i)) {
                return Errors.NOT_RIGHT_PARAMETER;
            }
        }

        int[] tempStudents = Arrays.copyOf(this.students, this.students.length);

        //System.out.println("Jester" + Arrays.toString(tempStudents));

        for (int i = 0; i < obj.length; i++) {
            Color c = Objects.requireNonNull(Color.getColorById(obj[i]));
            if (i % 2 == 0) {
                // even elements are those to be removed from this card
                tempStudents[c.getIndex()]--;
            } else {
                // odd elements are those to be added to this card
                tempStudents[c.getIndex()]++;
            }
        }

        for (int i : tempStudents) {
            if (i < 0) {
                return Errors.NOT_ENOUGH_TOKEN;
            }
        }

        int[] entrance = gameInitializer.getRoundHandler().getCurrent().getSchool().getCopyOfEntrance();

        int index = obj[1];
        if (entrance[index] < 1)
            return Errors.NOT_ENOUGH_TOKEN;
        else
            entrance[index]--;

        if (length > 2){
            index = obj[3];
            if (entrance[index] < 1)
                return Errors.NOT_ENOUGH_TOKEN;
            else
                entrance[index]--;
        }

        if (length > 4){
            index = obj[5];
            if (entrance[index] < 1)
                return Errors.NOT_ENOUGH_TOKEN;
        }


        return Errors.NO_ERROR;
    }
}
