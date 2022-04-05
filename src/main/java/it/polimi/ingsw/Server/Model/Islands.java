package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Random;

public class Islands {
    private final ArrayList<Island> islands;
    private Island MotherNature;

    Islands (GameBoard board, GameInitializer gInit){
        this.islands = new ArrayList<>(12);

        //set initial student in island
        int[] firstStudents = new int[Color.getNumberOfColors()];
        for(int i = 0; i < Color.getNumberOfColors(); i++){
            firstStudents[i] = 2;
        }

        Random rand = new Random(System.currentTimeMillis());

        int index;
        Island island = new Island(board, 0, gInit);
        this.islands.add(island);
        this.MotherNature = island;

        for (int i = 1; i < this.islands.size(); i++){

            island = new Island(board, i, gInit);
            this.islands.add(island);

            //todo change because the last one can call a lot of time next int to find the remaining
            if(i != (this.islands.size() / 2)){
                int j = 0;
                while (j < 1){
                    index = rand.nextInt(Color.getNumberOfColors());
                    if (firstStudents[index] > 0){
                        island.AddStudent(Color.getColorById(index));
                        firstStudents[index]--;
                        j++;
                    }
                }
            }
        }
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

        for (int i = 0; i < this.islands.size(); i++){
            if (MotherNature.equals(this.islands.get(i))){
                if(i + count > this.islands.size()){
                    this.MotherNature = this.islands.get(count + i - this.islands.size());
                }
                else
                    this.MotherNature = this.islands.get(i + count);
            }
        }
        //if there isn't the ban card
        if (!this.MotherNature.getBanCard())
            this.MotherNature.CalcInfluence();
        //todo check aggregate island
    }

    Island getIslandFromId(int id){
        for (Island i : this.islands){
            if (i.getId() == id) return i;
        }
        return null;
    }

    void AddStudentToIsland (Color color, int id){
        Island island = getIslandFromId(id);
        island.AddStudent(color);
    }
}
