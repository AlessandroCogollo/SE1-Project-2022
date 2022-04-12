package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

final class Cleric extends Character{

    private int[] students;

    Cleric(GameInitializer gameInitializer) {
        super (2, 1, gameInitializer);
        this.students = super.gameInitializer.getBag().drawStudents(4);

        // used for testing
        System.out.println("Built Cleric");
        System.out.println("Initialized Cleric with 4 students on him");
    }

    @Override
    void activateEffect(Object obj) {

        // todo implement actual color choosing

        int colorId = 0;
        int islandId = 0;

        Color c = Color.getColorById(colorId);
        Island i = gameInitializer.getIslands().getIslandFromId(islandId);

        this.students[c.getIndex()]--;
        i.addStudent(c);
        int[] newStudent= super.gameInitializer.getBag().drawStudents(1);
        for (Color color: Color.values())
            this.students[color.getIndex()] += newStudent[color.getIndex()];
    }

    @Override
    Errors canActivateEffect(Object obj) {

        // todo implement actual color choosing
        int colorId = 0;
        int islandId = 0;

        if (colorId < 0 || colorId > 4)
            return Errors.NOT_VALID_COLOR;
        if (!gameInitializer.getIslands().existsIsland(islandId))
            return Errors.NOT_VALID_DESTINATION;
        if (this.students[colorId] <= 0)
            return Errors.NO_STUDENT;

        return Errors.NO_ERROR;
    }
}
