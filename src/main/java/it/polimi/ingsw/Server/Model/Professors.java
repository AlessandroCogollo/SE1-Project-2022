package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Professors {
    private int[] professors;
    private GameBoard board;
    private ArrayList<Player> players;
    private ArrayList<School> schools;

    public Professors(GameBoard board){
        this.professors = new int[Color.getNumberOfColors()];
        for(int i=0; i<Color.getNumberOfColors(); i++){
            professors[i] = -1;
        }
        this.board = board;
    }

    public void updateProfessors(){
        this.players = board.getgInit().getPlayers();
        schools = new ArrayList<>();
        for (Player p : players){
            schools.add(p.getSchool());
        }
        int maxp = -1;
        for (int i=0; i<Color.getNumberOfColors(); i++){
            int max = 0;
            for (int j=0; j<players.size(); j++){
                if (schools.get(j).getNumberOfStudentInRoomByColor(Color.getColorById(i)) > max) {
                    max = schools.get(j).getNumberOfStudentInRoomByColor(Color.getColorById(i));
                    maxp = players.get(j).getId();
                }
            }
            if(maxp != -1){
                this.professors[i] = maxp;
            }
        }

    }

    public int[] getProfessors() {
        return professors;
    }

    public void setProfessors(Color color, Player player){
        this.professors[color.getIndex()] = player.getId();
    }

    public Collection<Color> getControlledProfessors(Player player){
        List<Color> temp = new ArrayList<Color>();
        for(int i=0; i<Color.getNumberOfColors(); i++){
            if (professors[i] == player.getId())
                temp.add(Color.getColorById(i));
        }
        return temp;
    }

    public Player getPlayerWithProfessor(Color color){
        int pId = professors[color.getIndex()];
        if(pId != -1) {
            return board.getgInit().getPlayerById(pId);
        }
        else return null;
    }
}
