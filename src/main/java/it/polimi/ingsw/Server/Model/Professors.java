package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Professors {
    private static List<Player> professors;

    public Professors(){
        this.professors = new ArrayList<>(5);
    }

    public void setProfessors(Color color, Player player){
        this.professors.set(color.getIndex(), player);
    }

    public static Collection<Color> getControlledProfessors(Player player){
        List<Color> temp = new ArrayList<Color>();
        for(int i=0; i<5; i++){
            if (professors.get(i) == player)
                temp.add(Color.getColorById(i));
        }
        return temp;
    }

    public static Player getPlayerWithProfessor(Color color){
        return professors.get(color.getIndex());
    }
}
