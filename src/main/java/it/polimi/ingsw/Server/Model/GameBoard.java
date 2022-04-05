package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;

class GameBoard {

    private final ArrayList<Cloud> clouds;
    private final Islands islands;
    private final Professors professors;
    private final GameInitializer gInit;
    private final Bag bag;

    GameBoard (GameInitializer gInit, int numOfPlayer){

        this.gInit = gInit;
        this.bag = new Bag();

        this.clouds = new ArrayList<>(numOfPlayer);
        for(int i = 0; i < numOfPlayer; i++){
            clouds.add(new Cloud( i, numOfPlayer, bag));
        }

        //instantiating islands
        this.islands = new Islands(this, gInit);

        //instantiating professors
        this.professors = new Professors(gInit);
    }

    void getCloud(int cloudId){
        //todo
    }

    Bag getBag() {
        return bag;
    }

    Professors getProfessors() {
        return professors;
    }

    void addStudentToIsland(Color color, int id){
        this.islands.AddStudentToIsland(color, id);
    }

    void moveMotherNature(int count){
        this.islands.MoveMotherNature(count);
    }

    void populateClouds(){
        for(Cloud cloud : clouds){
            cloud.setStudents();
        }
    }

    Collection<Player> getPlayerFromTowerColor (int TowerColor){
        Collection<Player> temp = new ArrayList<>(4);
        for (Player p : gInit){
            if (p.getTowerColor() == TowerColor) temp.add(p);
        }
        return temp;
    }


    //only for tests
    GameInitializer getgInit() {
        return gInit;
    }
    ArrayList<Cloud> getClouds() {
        return clouds;
    }
    Islands getIslands() {
        return islands;
    }
}
