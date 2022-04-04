package it.polimi.ingsw.Server.Model;

import java.util.Arrays;
import java.util.Random;

class Bag {

    private int[] students; //5
    private Random rand;

    //all'inizio metti 2 stud per ogni isola in cui non ce madre o nella opposta --OK
    //gli altri nella bag e controllo se ci sono ancora, altrimenti checkwin --OK

    public Bag(){
        students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++){
            students[i] = (100/Color.getNumberOfColors());
        }
    }
    public int[] DrawStudents(int count){
        Random rand = new Random(System.currentTimeMillis());
        int[] drawnStudents = new int[Color.getNumberOfColors()];
        int index;
        for (int i = 0; i < count; i++) {
            if (Arrays.stream(students).sum() > 0){
                index = rand.nextInt(Color.getNumberOfColors());
                if(students[index] > 0){
                    drawnStudents[index]++;
                    students[index]--;
                }
                else {
                    i--;
                }
            }
            else{
                System.out.println("Bag is empty!");
                // CheckWin() ------------------
                return null;
            }

        }
        return drawnStudents;
    }
}
