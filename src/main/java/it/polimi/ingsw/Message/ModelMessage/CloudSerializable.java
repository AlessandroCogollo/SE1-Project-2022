package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Cloud;

/**
 * class that contains the info of one cloud, but serializable
 */
public class CloudSerializable {

    private final int id;
    private final int[] drawnStudents;

    CloudSerializable(Cloud c) {
        this.id = c.getId();
        this.drawnStudents = c.getCopyOfDrawnStudents();
    }

    public int getId() {
        return id;
    }

    public int[] getDrawnStudents() {
        return drawnStudents;
    }
}
