package it.polimi.ingsw.Server.Model;

import java.util.Optional;

// sub class of player that add the play character methods and the management of characters
class AdvancedPlayer extends Player {

    private int coins;

    AdvancedPlayer(int id, int towerColor, Player mate, GameInitializer gameInitializer, School school) {
        super(id, towerColor, mate, gameInitializer, school);
        this.coins = 1;
    }

    int getCoins() {
        return coins;
    }

    void playCharacter(Character c, Object obj) {
        if (c != null)
            coins -= c.getCost();
        gameInitializer.getBoard().playCharacter(c, obj);
    }

    @Override //override for coins
    void moveStudent (Color c, int destinationId){
        if (destinationId != -1){
            Color student = school.moveStudentFromEntrance(c);
            gameInitializer.getIslands().addStudentToIsland(student, destinationId);
        }
        else {
            if (school.moveStudentToRoom(c))
                coins++;
            gameInitializer.getProfessors().updateProfessors();
        }
    }
}
