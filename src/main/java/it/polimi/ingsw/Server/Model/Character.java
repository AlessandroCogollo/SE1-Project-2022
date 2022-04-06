package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

abstract class Character {

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

    int getId() { return this.id; }

    int getCost() {
        if (this.used)
            return this.cost + 1;
        return this.cost;
    }

    void use (Object obj){
        activateEffect(obj);
        this.used = true;
    }

    abstract void activateEffect(Object obj);

    abstract Errors canActivateEffect (Object obj);
}