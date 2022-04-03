package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public abstract class Character {

    private int cost;
    private boolean isChangingMethods;

    public boolean getIsChangingMethods() { return this.isChangingMethods; }

    public int getCost() { return this.cost; }

    public void setCost() { this.cost += 1; }

    public abstract void activateEffect(Optional<Object> object);
}