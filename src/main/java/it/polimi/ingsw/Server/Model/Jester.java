package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Jester extends Character {
    private int[] students;

    //todo character creation in gameboard
    Jester(GameInitializer gameInitializer) {
        super (6, 1, gameInitializer);
        this.students = super.gameInitializer.getBag().drawStudents(6);
        System.out.println("Jester has " + students.length + " students");
    }

    @Override
    void activateEffect(Object obj) {
        //todo
        // swap max 3 students from this card with as many students in your entrance
    }

    @Override
    Errors canActivateEffect(Object obj) {
        //todo
        return Errors.NO_ERROR;
    }
}
