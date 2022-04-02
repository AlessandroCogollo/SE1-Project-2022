package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class Islands {
    private final ArrayList<Island> islands;
    private Island MotherNature;

    public Islands(GameInitializer gInit){
        this.islands = new ArrayList<>();
        for (int i=0; i<12; i++){
            this.islands.add(new Island(gInit, i));
        }
        this.MotherNature = this.islands.get(0);
    }

    private void AggregateIsland(Island currIsland, Island nearIsland){
        //add the towers
        currIsland.setTowerCount(currIsland.getTowerCount() + nearIsland.getTowerCount());
        //add the students
        for (int i=0; i<Color.getNumberOfColors(); i++) {
            for(int j=0; j<nearIsland.getStudents()[i]; j++){
                currIsland.AddStudent(Color.getColorById(i));
            }
        }
    }

    public void MoveMotherNature(int count){
        for (int i=0; i<this.islands.size(); i++){
            if (MotherNature.equals(this.islands.get(i))){
                if(i+count > islands.size()){
                    MotherNature = this.islands.get(count + i - this.islands.size());
                }
                else MotherNature = this.islands.get(i + count);
            }
        }
    }

    public Island getIslandFromId(int id){
        for (Island i : this.islands){
            if (i.getId() == id) return i;
        }
        return null;
    }

    public void AddStudentToIsland(Color color, int id){
        Island island = getIslandFromId(id);
        island.AddStudent(color);
    }

    private void NextMotherNature(){}
}
