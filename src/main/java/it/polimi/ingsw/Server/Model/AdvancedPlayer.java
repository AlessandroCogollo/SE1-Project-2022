package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Characters.Character;

// sub class of player that add the play character methods and the management of characters
public class AdvancedPlayer extends Player {

    private int coins;

    //not public only created in factory in Player class
    AdvancedPlayer(int id, int towerColor, Player mate, GameInitializer gameInitializer, School school) {
        super(id, towerColor, mate, gameInitializer, school);
        this.coins = 1;
    }

    public int getCoins() {
        return coins;
    }

    public void playCharacter(Character c, Object obj) {
        if (c != null)
            coins -= c.getCost();
        gameInitializer.getBoard().playCharacter(c, obj);
    }

    @Override //override for coins
    public void moveStudent (Color c, int destinationId){
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
