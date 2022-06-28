package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

import java.util.Arrays;
import java.util.Random;

/**
 * Bag class to draw the students
 */
public class Bag {

    private final int[] students;
    private final Random rand = new Random(System.currentTimeMillis());
    private final GameInitializer gameInitializer;

    public Bag(GameInitializer gameInitializer){
        this.students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++){
            students[i] = (24); //from the rules
        }
        this.gameInitializer = gameInitializer;
    }

    public Bag(GameInitializer gameInitializer, int[] bag) {
        this.students = Arrays.copyOf(bag, bag.length);
        this.gameInitializer = gameInitializer;
    }

    /**
     *
     * @return the array of the left students
     */
    public int[] getStudentsCopy(){
        return Arrays.copyOf(students, students.length);
    }

    public void addStudents(Color color) {
        this.students[color.getIndex()]++;
    }

    /**
     * draw student(s), that will be removed from the bag
     * @param count: the number of students to be drawn
     * @return an array representing the students drawn:
     * the index is the corresponding color, while the element is the number of students drawn of that color
     */
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
