package it.polimi.ingsw.Server.Model;

import java.util.Arrays;
import java.util.Random;

class Bag {
    /*not the real one only a definition for the test

    public int[] drawStudents(int entranceStudent) {
        int[] x = new int[Color.getNumberOfColors()];
        Arrays.fill(x, 0);
        return x;
    } */

    private int[] students; //5
    private Random rand;

    public Bag(){}
    public static int[] DrawStudents(int entranceStudents){
        Random rand = new Random(System.currentTimeMillis());
        int[] student = new int[Color.getNumberOfColors()];
        for (int i = 0; i < entranceStudents; i++) {
            student[rand.nextInt(Color.getNumberOfColors())]++;
        }
        return student;
    }
}
