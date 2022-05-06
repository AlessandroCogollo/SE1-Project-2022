package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

import java.util.Arrays;

//The school class implements a container for all the stuffs of the player during the game and can only be modified from the class Student itself
public class School {

    private final int[] entrance;
    private final int[] room;
    private int towers;

    int[] getCopyOfEntrance (){
        return Arrays.copyOf(entrance, entrance.length);
    }
    int[] getCopyOfRoom (){
        return Arrays.copyOf(room, room.length);
    }
    School(int towers, int entranceStudent, Bag bag) {
        this.towers = towers;
        this.room = new int[Color.getNumberOfColors()];
        Arrays.fill(this.room, 0);
        this.entrance = bag.drawStudents(entranceStudent);
    }

    int getTowers() {
        return towers;
    }

    boolean hasStudentInEntrance (Color color){
        return entrance[color.getIndex()] > 0;
    }

    public int getNumberOfStudentInRoomByColor (Color c){
        return room[c.getIndex()];
    }

    //return true if the player will get a coin
    public boolean moveStudentToRoom (Color color){
        int index = color.getIndex();
        entrance[index]--;
        room[index]++;
        return room[index] % 3 == 0;
    }

    // used for characters' effects
    public Color removeStudentFromRoom(Color color) {
        room[color.getIndex()]--;
        return color;
    }

    void addStudentFromCloud(Cloud c) {
        int[] students = c.getStudents();
        for (Color color : Color.values()){
            int index = color.getIndex();
            entrance[index] += students[index];
        }
    }

    void addTowers(int number) {
        towers += number;
    }

    boolean removeTowers(int number) {

        for (int i = 0; i < number && towers > 0; i++)
            towers--;

        //win
        return towers == 0;
    }

    public Color moveStudentFromEntrance(Color color) {
        entrance[color.getIndex()]--;
        return color;
    }

    public void moveStudentToEntrance(Color color) {
        entrance[color.getIndex()]++;
    }
}
