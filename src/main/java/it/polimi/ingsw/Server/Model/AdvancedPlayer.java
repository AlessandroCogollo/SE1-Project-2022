package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Optional;

// sub class of player that add the play character methods and the management of characters
class AdvancedPlayer extends Player {

    private int coins;
    private final Collection<Character> cDeck;
    private Optional<Character> activeCharacter;

    AdvancedPlayer(int id, int towerColor, Player mate, GameBoard board, School school) {
        super(id, towerColor, mate, board, school);
        this.coins = 1;

        //todo right deck, need Character class
        //this.cDeck = Character.getNewDeck();
        this.cDeck = null;

        this.activeCharacter = Optional.empty();
    }

    int getCoins() {
        return coins;
    }

    public Optional<Character> getActiveCharacter() {
        return activeCharacter;
    }

    //override for coins
    @Override
    void moveStudent(Movement move) {
        if (move.getDestination().isPresent()){
            Color student = school.moveStudentFromEntrance(move.getColor());
            //todo movement to cloud, need GameBoard class
        }
        else
            if (school.moveStudentToRoom(move.getColor()))
                coins++;
    }

    //override for character that changes the methods
    @Override
    void moveMotherNature(int position) {
        //todo movement, need GameBoard
        super.moveMotherNature(position);
    }

    void playCharacter(Character c) {
        //coins -= x.getCost();
        activeCharacter = Optional.ofNullable(c);
        //todo actual implementation, need Character class

    }

}
