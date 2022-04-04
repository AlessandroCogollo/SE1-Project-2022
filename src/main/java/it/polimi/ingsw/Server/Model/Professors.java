package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Professors {
    private int[] professors;
    private GameInitializer gInit;
    private ArrayList<School> schools;

    Professors(GameInitializer gInit){
        this.professors = new int[Color.getNumberOfColors()];
        for(int i=0; i<Color.getNumberOfColors(); i++){
            professors[i] = -1;
        }
        this.gInit = gInit;
    }

    void updateProfessors(){
        schools = new ArrayList<>();
        for (Player p : gInit){
            schools.add(p.getSchool());
        }
        int maxp = -1;
        for (int i=0; i<Color.getNumberOfColors(); i++){
            int max = 0;
            for (Player p : gInit){
                if (p.getNumberOfStudentInRoomByColor(Color.getColorById(i)) > max) {
                    max = p.getNumberOfStudentInRoomByColor(Color.getColorById(i));
                    maxp = p.getId();
                }
            }
            if(maxp != -1){
                this.professors[i] = maxp;
            }
        }

    }

    int[] getProfessors() {
        return professors;
    }

    void setProfessors(Color color, Player player){
        this.professors[color.getIndex()] = player.getId();
    }

    Collection<Color> getControlledProfessors(Player player){
        List<Color> temp = new ArrayList<Color>();
        for(int i=0; i<Color.getNumberOfColors(); i++){
            if (professors[i] == player.getId())
                temp.add(Color.getColorById(i));
        }
        return temp;
    }

    Player getPlayerWithProfessor(Color color){
        int pId = professors[color.getIndex()];
        if(pId != -1) {
            return gInit.getPlayerById(pId);
        }
        else return null;
    }
}
