package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

import java.util.Arrays;

//The school class implements a container for all the stuffs of the player during the game and can only be modified from the class Student itself
public class School {

    private final int[] entrance;
    private final int[] room;
    private int towers;

    public int[] getCopyOfEntrance (){
        return Arrays.copyOf(entrance, entrance.length);
    }
    public int[] getCopyOfRoom (){
        return Arrays.copyOf(room, room.length);
    }
    School(int towers, int entranceStudent, Bag bag) {
        this.towers = towers;
        this.room = new int[Color.getNumberOfColors()];
        Arrays.fill(this.room, 0);
        this.entrance = bag.drawStudents(entranceStudent);
    }

    public int getTowers() {
        return towers;
    }

    boolean hasStudentInEntrance (Color color){
        return entrance[color.getIndex()] > 0;
    }

    public int getNumberOfStudentInRoomByColor (Color c){
        return room[c.getIndex()];
    }

    /**
     * Move a students from the entrance to the room
     * @param color color that has to be moved from the entrance
     * @return true if the player will have a bonus coin, false otherwise
     */
    public boolean moveStudentToRoom (Color color){
        int index = color.getIndex();
        entrance[index]--;
        room[index]++;
        return room[index] % 3 == 0;
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


    /**
     * Used only for characters effect
     * @param color color that will be removed
     * @return the color removed
     */
    public Color removeStudentFromEntrance(Color color) {
        entrance[color.getIndex()]--;
        return color;
    }

    /**
     * Used only for characters effect
     * @param color color that will be added
     * @return the color added
     */
    public Color addStudentToEntrance(Color color) {
        entrance[color.getIndex()]++;
        return color;
    }

    /**
     * Used only for characters effect
     * @param color color that will be removed
     * @return the color removed
     */
    public Color removeStudentFromRoom(Color color) {
        room[color.getIndex()]--;
        return color;
    }

    /**
     * Used only for characters effect
     * @param color color that will be added
     * @return the color added
     */
    public Color addStudentToRoom(Color color) {
        room[color.getIndex()]++;
        return color;
    }
}
