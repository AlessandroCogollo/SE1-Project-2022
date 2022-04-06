package it.polimi.ingsw.Server.Model;

import java.util.Arrays;
import java.util.Random;

class Bag {

    private final int[] students;
    private final Random rand;
    private final GameInitializer gameInitializer;

    Bag(GameInitializer gameInitializer){
        this.students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++){
            students[i] = (24); //from the rules
        }
        this.rand = new Random(System.currentTimeMillis());
        this.gameInitializer = gameInitializer;
    }

    int[] drawStudents(int count){
        int[] drawnStudents = new int[Color.getNumberOfColors()];
        Arrays.fill(drawnStudents, 0);
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
                gameInitializer.getRoundHandler().setFinalRound();
                break;
            }
        }
        return drawnStudents;
    }
}
