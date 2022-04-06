package it.polimi.ingsw.Server.Model;

import java.util.Arrays;

class Cloud {
    private final int id;
    private final Bag bag;
    private final int numOfPlayer;
    private int[] drawnStudents;


    Cloud(int id, GameInitializer gameInitializer){
        this.id = id;
        this.bag = gameInitializer.getBag();
        this.numOfPlayer = gameInitializer.getPlayerNumber();
        setStudents();
    }

    public int getId() {
        return id;
    }

    void setStudents(){
        if (numOfPlayer == 3)
            this.drawnStudents = bag.drawStudents(4);
        else
            this.drawnStudents = bag.drawStudents(3);
    }

    //return a copy of the students in the cloud and reset them to 0
    int[] getStudents() {
        int[] students = new int[Color.getNumberOfColors()];
        for (Color c: Color.values())
            students[c.getIndex()] = drawnStudents[c.getIndex()];
        Arrays.fill(drawnStudents, 0);
        return students;
    }
}
