package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Cook extends Character {

    private Optional<Color> color;

    Cook() {
        super.isChangingMethods = true;
        this.color = Optional.empty();
    }

    @Override
    public void activateEffect(Object color) {
        this.color = Optional.ofNullable((Color)color);
        System.out.println("Color choosen for cook: " + this.color);
    }

    public Optional<Player> calcInfluence() {
        return null;
    }

}
