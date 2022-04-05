package it.polimi.ingsw.Server.Model;

import java.util.Arrays;
import java.util.Random;

class Bag {

    private final int[] students;
    private final Random rand;

    public Bag(){
        this.students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++){
            students[i] = (24); //from the rules
        }
        this.rand = new Random(System.currentTimeMillis());
    }
    public int[] DrawStudents(int count){
        int[] drawnStudents = new int[Color.getNumberOfColors()];
        int index;
        int i = 0;
        while (i < count) {
            if (Arrays.stream(students).sum() > 0){
                index = rand.nextInt(Color.getNumberOfColors());
                if(students[index] > 0){
                    drawnStudents[index]++;
                    students[index]--;
                    i++;
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
