package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.GameInitializer;

abstract public class Character {

    protected final int id;
    protected final GameInitializer gameInitializer;
    protected final String name;
    protected int cost;
    protected boolean used;

    public Character(int id, int cost, GameInitializer gameInitializer, String name) {
        this.id = id;
        this.cost = cost;
        this.gameInitializer = gameInitializer;
        this.used = false;
        this.name = name;
    }

    public int getId() { return this.id; }

    public int getCost() {
        if (this.used)
            return this.cost + 1;
        return this.cost;
    }

    public String getName() {
        return name;
    }

    public boolean getUsed() {
        return this.used;
    }

    public void use (int[] obj){
        activateEffect(obj);
        this.used = true;
    }

    abstract protected void activateEffect(int[] obj);

    abstract public Errors canActivateEffect (int[] obj);
}