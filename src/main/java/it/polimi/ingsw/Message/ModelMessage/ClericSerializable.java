package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Characters.Cleric;

public final class ClericSerializable extends CharacterSerializable {
    private final int[] students;

    ClericSerializable(Cleric c) {
        super(c);
        this.students = c.getStudentsCopy();
    }

    public int[] getStudents() {
        return students;
    }
}
