package it.polimi.ingsw.Server.Model;

public abstract class Character {

    protected int id;
    protected boolean isChangingMethods;
    protected int cost;
    protected Game game;

    public int getId() { return this.id; }

    public boolean getIsChangingMethods() { return this.isChangingMethods; }

    public int getCost() { return this.cost; }

    public void setCost() { this.cost += 1; }

    public abstract void activateEffect(Object object);
}