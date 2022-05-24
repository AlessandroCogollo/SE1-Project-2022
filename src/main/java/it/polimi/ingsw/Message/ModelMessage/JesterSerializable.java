package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Characters.Jester;

public final class JesterSerializable extends CharacterSerializable {
    private final int[] students;

    JesterSerializable(Jester c) {
        super(c);
        this.students = c.getStudentsCopy();
    }

    public int[] getStudents() {
        return students;
    }
}
