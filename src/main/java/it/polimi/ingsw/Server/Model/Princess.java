package it.polimi.ingsw.Server.Model;

import java.util.Random;

public class Princess extends Character {
    private boolean isChangingMethods;
    private int[] students;

    //todo character creation in gameboard
    Princess() {
        super.id = 10;
        super.isChangingMethods = false;
        super.cost = 2;
        //students = Bag.DrawStudents(4);
    }

    public void activateEffect(Object object) {
        // todo move a student to the room

        // draw a new student and put it on this card
        Random rand = new Random(System.currentTimeMillis());
        students[rand.nextInt(Color.getNumberOfColors())]++;

        System.out.println("Princess");
    }
}
