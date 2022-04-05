package it.polimi.ingsw.Server.Model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Cleric extends Character{

    private int[] students;
    //private Game game;

    //todo character creation in gameboard
    public Cleric() {
        super.id = 4;
        super.isChangingMethods = false;
        // this.game = game;
        //students = Bag.DrawStudents(4);
        // used for testing
        System.out.println("Built Cleric");
        System.out.println("Initialized Cleric with 4 students on him");
    }

    @Override
    public void activateEffect(Object island) {

        // choose a color & check if present
        // todo implement actual color choosing
        int color = 2;
        if (this.students[color] > 0) {
            // remove a student from this card
            this.students[color]--;
            System.out.println("N° of students before: " + Arrays.toString(((Island) island).getStudents()));
            ((Island) island).AddStudent(Objects.requireNonNull(Color.getColorById(color)));
            // used for testing
            System.out.println("N° of students after: " + Arrays.toString(((Island) island).getStudents()));
            // draw a new student and put it on this card
            Random rand = new Random(System.currentTimeMillis());
            students[rand.nextInt(Color.getNumberOfColors())]++;
        } else {
            // throw exception
        }
    }
}
