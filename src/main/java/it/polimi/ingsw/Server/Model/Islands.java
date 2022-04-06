package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Random;

class Islands {
    private final ArrayList<Island> islands;
    private Island motherNature;

    Islands (){
        this.islands = new ArrayList<>(12);

        Island island = new Island(0);
        this.islands.add(island);
        this.motherNature = island;

        for (int i = 1; i < 12; i++){
            this.islands.add(new Island(i));
        }

        //set all students in island
        Color[] firstStudents = new Color[Color.getNumberOfColors() * 2];
        int i = 0;
        for (Color c : Color.values()){
            firstStudents[i++] = c;
        }
        for (Color c : Color.values()){
            firstStudents[i++] = c;
        }

        rand(firstStudents);

        i = 0;
        for (Island is : this.islands)
            if (is.getId() != this.islands.size() / 2 && is.getId() != 0)
                is.AddStudent(firstStudents[i++]);
    }

    int getIslandsNumber (){
        return this.islands.size();
    }

    Island getMotherNature() {
        return motherNature;
    }

    boolean existsIsland(int destinationId) {
        boolean found = false;
        for (Island i : islands)
            if (i.getId() == destinationId){
                found = true;
                break;
            }
        return found;
    }

    Island getIslandFromId(int id){
        for (Island i : this.islands)
            if (i.getId() == id)
                return i;
        return null;
    }

    void addStudentToIsland(Color color, int id){
        Island island = getIslandFromId(id);
        island.AddStudent(color);
    }

    void nextMotherNature() {
        //todo cycle throw island
    }

    //todo check if the near island have the same tower color and then aggregate them
    void aggregateIsland(Island currIsland, Island nearIsland){
        //add the towers
        currIsland.setTowerCount(currIsland.getTowerCount() + nearIsland.getTowerCount());
        //add the students
        for (int i=0; i<Color.getNumberOfColors(); i++) {
            for(int j=0; j<nearIsland.getStudents()[i]; j++){
                currIsland.AddStudent(Color.getColorById(i));
            }
        }
    }

    static private <T> void rand( T[] array) {
        // Creating object for Random class
        Random rd = new Random(System.currentTimeMillis());

        // Starting from the last element and swapping one by one.
        for (int i = array.length - 1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = rd.nextInt(i + 1);

            // Swap array[i] with the element at random index
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
