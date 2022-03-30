package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public class Movement {
    private final Color x;
    private final Optional<Island> destination;

    public Movement(Color x, Island destination) {
        this.x = x;
        this.destination = Optional.ofNullable(destination);
    }

    public Color getColor() {
            return x;
    }
    public Optional<Island> getDestination() {
        return destination;
    }
}
