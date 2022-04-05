package it.polimi.ingsw.Server.Model;

public class Jester extends Character {
    private int[] students;

    //todo character creation in gameboard
    Jester() {
        //students = Bag.DrawStudents(6);
        super.isChangingMethods = false;
        System.out.println("Jester has " + students.length + " students");
    }
    public void activateEffect(Object object) {
        // swap max 3 students from this card with as many students in your entrance
    }
}
