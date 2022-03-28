package it.polimi.ingsw.Server.Model;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class StudentsMovements implements Iterable{
    private final Collection<Movement> moves;

    StudentsMovements() {
        this.moves = new ArrayList<>();
    }

    int getNumberOfMoves(){
        return moves.size();
    }

    void addMovement(Color x, Optional<Island> c){
        moves.add(new Movement(x, c));
    }

    @Override
    public Iterator iterator() {
        return moves.iterator();
    }


    public class Movement {
        private final Color x;
        private final Optional<Island> destination;

        private Movement(Color x, Optional<Island> destination) {
            this.x = x;
            this.destination = destination;
        }

        public Color getColor() {
            return x;
        }

        public Optional<Island> getDestination() {
            return destination;
        }
    }
}
