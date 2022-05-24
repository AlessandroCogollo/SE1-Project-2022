package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Characters.Princess;

public final class PrincessSerializable extends CharacterSerializable {
    private final int[] students;

    PrincessSerializable(Princess c) {
        super(c);
        this.students = c.getStudentsCopy();
    }

    public int[] getStudents() {
        return students;
    }
}
