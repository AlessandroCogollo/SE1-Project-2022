package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.Characters.Character;
import it.polimi.ingsw.Server.Model.Characters.*;

import java.util.Arrays;

public class CharacterSerializable {

    private final int id;
    private final int cost;
    private final boolean used;
    private final String name;
    private final int[] students;
    private final int banCard;
    private final int colorId;

    public CharacterSerializable(Character c) {
        this.id = c.getId();
        this.used = c.getUsed();
        this.cost = (this.used) ? (c.getCost() - 1) : c.getCost();
        this.name = c.getName();

        //apothecary
        if (this.id == 0)
            this.banCard = ((Apothecary) c).getBanCard();
        else
            this.banCard = -1;

        //cleric, jester, princess
        switch (this.id) {
            case 2 -> this.students = ((Cleric) c).getStudentsCopy();
            case 6 -> this.students = ((Jester) c).getStudentsCopy();
            case 10 -> this.students = ((Princess) c).getStudentsCopy();
            default -> this.students = null;
        }

        //cook
        if (this.id == 3) {
            Color col = ((Cook) c).getColor();
            if (col != null)
                this.colorId = col.getIndex();
            else
                this.colorId = -1;
        }
        else
            this.colorId = -1;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public boolean isUsed() {
        return used;
    }

    public String getName() {
        return name;
    }

    public int[] getStudents() {
        return Arrays.copyOf(this.students, this.students.length);
    }

    public int getBanCard() {
        return banCard;
    }

    public int getColorId() {
        return colorId;
    }
}
