package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;

import java.util.Arrays;

/**
 * cloud class, representing the clouds to be chosen at the end of the turn
 */
public class Cloud {
    private final int id;
    private final Bag bag;
    private final int numOfPlayer;
    private int[] drawnStudents;
    public Cloud(int id, GameInitializer gameInitializer){
        this.id = id;
        this.bag = gameInitializer.getBag();
        this.numOfPlayer = gameInitializer.getPlayersNumber();
        setStudents();
    }

    public Cloud(GameInitializer gameInitializer, CloudSerializable cloud){
        this.id = cloud.getId();
        this.bag = gameInitializer.getBag();
        this.numOfPlayer = gameInitializer.getPlayersNumber();
        this.drawnStudents = cloud.getDrawnStudents();
    }

    /**
     * @return only a copy of drawnStudents, used only in message creations
     */
    public int[] getCopyOfDrawnStudents (){
        return Arrays.copyOf(drawnStudents, drawnStudents.length);
    }

    public int getId() {
        return id;
    }

    public void setStudents(){
        if (numOfPlayer == 3)
            this.drawnStudents = bag.drawStudents(4);
        else
            this.drawnStudents = bag.drawStudents(3);
    }

    /**
     *
     * @return a copy of the students in the cloud and reset them to 0
     */
    public int[] getStudents() {
        //create a copy of drawstudents
        int[] students = Arrays.copyOf(drawnStudents, drawnStudents.length);

        //delete all the students from the cloud
        Arrays.fill(drawnStudents, 0);

        //return the copy
        return students;
    }
}
