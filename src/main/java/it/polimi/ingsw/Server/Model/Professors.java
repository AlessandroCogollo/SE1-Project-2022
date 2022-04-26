package it.polimi.ingsw.Server.Model;

import java.util.Arrays;

class Professors {

    private final int[] professors;
    private final GameInitializer gInit;

    int[] getProfessorsCopy(){
        return Arrays.copyOf(professors, professors.length);
    }

    Professors(GameInitializer gInit){
        this.professors = new int[Color.getNumberOfColors()];
        for(int i=0; i<Color.getNumberOfColors(); i++){
            professors[i] = -1;
        }
        this.gInit = gInit;
    }

    void updateProfessors(){
        //check all professor color
        for (Color c: Color.values()){

            //initialise temp variables
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
            //update the professor if only if there aren't equals
            if (!equal && maxP != null)
                professors[c.getIndex()] = maxP.getId();
        }
    }

    Player getPlayerWithProfessor (Color color){
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
