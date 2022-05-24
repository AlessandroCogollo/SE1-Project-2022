package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Server.Model.Characters.Character;

public class CharacterSerializable {

    private final int id;
    private final int cost;
    private final boolean used;
    private final String name;

    public CharacterSerializable(Character c) {
        this.id = c.getId();
        this.used = c.getUsed();
        this.cost = (this.used) ? (c.getCost() - 1) : c.getCost();
        this.name = c.getName();
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
}
