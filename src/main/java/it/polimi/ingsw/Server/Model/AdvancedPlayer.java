package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Optional;

// sub class of player that add the play character methods and the management of characters
class AdvancedPlayer extends Player {

    private int coins;
    private final Collection<Character> cDeck;
    private Optional<Character> activeCharacter;

    AdvancedPlayer(int id, int towerColor, Optional<Player> mate, GameBoard board, School school) {
        super(id, towerColor, mate, board, school);
        this.coins = 1;

        //todo Character
        //this.cDeck = Character.getNewDeck();
        this.cDeck = null;

        activeCharacter = null;
    }

    int getCoins() {
        return coins;
    }

    //override for coins
    @Override
    void moveStudent(StudentsMovements.Movement move) {
        if (move.getDestination().isPresent()){
            //todo movement to cloud
        }
        else
            if (school.moveStudentToRoom(move.getColor()))
                coins++;
    }

    //override for character that changes the methods
    @Override
    void moveMotherNature(int position) {
        //todo
        super.moveMotherNature(position);
    }

    void playCharacter() {
        //coins -= x.getCost();
        //todo Character
    }

}
