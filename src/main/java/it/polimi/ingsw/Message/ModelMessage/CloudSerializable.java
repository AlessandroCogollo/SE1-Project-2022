package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Cloud;

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
