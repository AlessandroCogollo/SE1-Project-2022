package it.polimi.ingsw.Server.Model;

import java.util.Arrays;
import java.util.Random;

public class Bag {

    private final int[] students;
    private final Random rand;
    private final GameInitializer gameInitializer;

    public Bag(GameInitializer gameInitializer){
        this.students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++){
            students[i] = (24); //from the rules
        }
        this.rand = new Random(System.currentTimeMillis());
        this.gameInitializer = gameInitializer;
    }

    public int[] getStudentsCopy(){
        return Arrays.copyOf(students, students.length);
    }

    public void addStudents(Color color) {
        this.students[color.getIndex()]++;
    }

    public int[] drawStudents(int count){
        int[] drawnStudents = new int[Color.getNumberOfColors()];
        Arrays.fill(drawnStudents, 0);

        int remainingStudent = Arrays.stream(students).sum();
        if (count >= remainingStudent) {
            count = remainingStudent;
            System.out.println("Bag is empty!");
            gameInitializer.getRoundHandler().setFinalRound();
        }

        int index;
        int i = 0;
        while (i < count){
            index = rand.nextInt(Color.getNumberOfColors());
            if(students[index] > 0){
                drawnStudents[index]++;
                students[index]--;
                i++;
            }
        }

        return drawnStudents;
    }
}
