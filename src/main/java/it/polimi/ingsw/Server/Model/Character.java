package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

abstract public class Character {

    protected final int id;
    protected final GameInitializer gameInitializer;
    protected int cost;
    protected boolean used;

    protected Character(int id, int cost, GameInitializer gameInitializer) {
        this.id = id;
        this.cost = cost;
        this.gameInitializer = gameInitializer;
        this.used = false;
    }

    public int getId() { return this.id; }

    public int getCost() {
        if (this.used)
            return this.cost + 1;
        return this.cost;
    }

    public void use (Object obj){
        activateEffect(obj);
        this.used = true;
    }

    abstract protected void activateEffect(Object obj);

    abstract public Errors canActivateEffect (Object obj);
}