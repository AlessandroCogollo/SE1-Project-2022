package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.Model.Characters.Character;

import java.util.Arrays;

public class Professors {

    private final int[] professors;
    private final GameInitializer gInit;


    public Professors(GameInitializer gInit){
        this.professors = new int[Color.getNumberOfColors()];
        for(int i=0; i<Color.getNumberOfColors(); i++){
            professors[i] = -1;
        }
        this.gInit = gInit;
    }

    public Professors(GameInitializer gInit, int[] professorsList) {
        this.gInit = gInit;
        this.professors = Arrays.copyOf(professorsList, professorsList.length);
    }

    public int[] getProfessorsCopy(){
        return Arrays.copyOf(professors, professors.length);
    }

    void updateProfessors(){
        //check all professor color
        for (Color c: Color.values()){

            //initialize temp variables
            Player maxP = null;
            int max = -1;
            boolean equal = false;

            //check the number of students for all the player
            for (Player p: gInit){
                int studentCount = p.getNumberOfStudentInRoomByColor(c);

                //if is the first
                if (maxP == null) {
                    maxP = p;
                    max = studentCount;
                }
                else{
                    if (max < studentCount){
                        max = studentCount;
                        maxP = p;
                        equal = false;
                    }
                    else if (max == studentCount)
                        equal = true;
                }
            }

            Character activeCharacter = gInit.getBoard().getActiveCharacter();
            RoundHandler r = gInit.getRoundHandler();
            Player current = null;
            if (r != null){
                current = r.getCurrent();
            }

            /*DrunkHard effect*/
            if (activeCharacter != null && activeCharacter.getId() == 4 && current != null && current.getSchool().getNumberOfStudentInRoomByColor(c) == max && max != 0){
                professors[c.getIndex()] = current.getId();
                continue;
            }

            //update the professor if only if there aren't equals
            if (!equal && maxP != null)
                professors[c.getIndex()] = maxP.getId();
        }
    }

    Player getPlayerWithProfessor (Color color){
        if (color == null)
            return null;
        int pId = professors[color.getIndex()];
        if (pId != -1)
            return gInit.getPlayerById(pId);
        return null;
    }

    int getNumberOfProfessorOfPlayer (Player p){
        int i = 0;
        int id = p.getId();
        for (Color c: Color.values()){
            if (professors[c.getIndex()] == id)
                i++;
        }
        return i;
    }
}
