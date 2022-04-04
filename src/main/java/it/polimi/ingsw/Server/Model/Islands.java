package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Islands {
    private final ArrayList<Island> islands;
    private Island MotherNature;
    private int numOfIslands = 12;

    Islands(GameBoard board){
        this.islands = new ArrayList<>();

        int[] firstStudents = new int[Color.getNumberOfColors()];
        for(int i=0; i<Color.getNumberOfColors(); i++){
            firstStudents[i] = 4;
        }

        Random rand = new Random(System.currentTimeMillis());
        int index;

        for (int i=0; i<numOfIslands; i++){

            Island island = new Island(board, i);
            this.islands.add(island);

            if(i!=0 && i!=numOfIslands/2){

                for(int j=0; j<2; j++){

                    index = rand.nextInt(Color.getNumberOfColors());
                    while(firstStudents[index] == 0){
                        index = rand.nextInt(Color.getNumberOfColors());
                    }

                    island.AddStudent2(Color.getColorById(index));
                    firstStudents[index]--;
                }
            }

        }

        this.MotherNature = this.islands.get(0);
    }

    void AggregateIsland(Island currIsland, Island nearIsland){
        //add the towers
        currIsland.setTowerCount(currIsland.getTowerCount() + nearIsland.getTowerCount());
        //add the students
        for (int i=0; i<Color.getNumberOfColors(); i++) {
            for(int j=0; j<nearIsland.getStudents()[i]; j++){
                currIsland.AddStudent(Color.getColorById(i));
            }
        }
    }

    void MoveMotherNature(int count){
        for (int i=0; i<this.islands.size(); i++){
            if (MotherNature.equals(this.islands.get(i))){
                if(i+count > islands.size()){
                    MotherNature = this.islands.get(count + i - this.islands.size());
                }
                else MotherNature = this.islands.get(i + count);
            }
        }
    }

    Island getIslandFromId(int id){
        for (Island i : this.islands){
            if (i.getId() == id) return i;
        }
        return null;
    }

    void AddStudentToIsland(Color color, int id){
        Island island = getIslandFromId(id);
        island.AddStudent(color);
    }
}
