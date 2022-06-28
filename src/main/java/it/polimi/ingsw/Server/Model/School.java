package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

import java.util.Arrays;

/**
 * The school class implements a container for all the stuffs of the player during the game
 */
public class School {

    private final int[] entrance;
    private final int[] room;
    private int towers;

    /**
     * Contructor of school
     * @param towers towers of this school
     * @param entranceStudent the number of students in the entrance
     * @param bag the bag for get the students to add them in the entrance
     */
    School(int towers, int entranceStudent, Bag bag) {
        this.towers = towers;
        this.room = new int[Color.getNumberOfColors()];
        Arrays.fill(this.room, 0);
        this.entrance = bag.drawStudents(entranceStudent);
    }

    /**
     * Getter for the entrance
     * @return a copy of it for don't let modify it from outside the class
     */
    public int[] getCopyOfEntrance (){
        return Arrays.copyOf(entrance, entrance.length);
    }
    /**
     * Getter for the room
     * @return a copy of it for don't let modify it from outside the class
     */
    public int[] getCopyOfRoom (){
        return Arrays.copyOf(room, room.length);
    }

    /**
     * getter of tower number in this school
     * @return the number of tower in this school
     */
    public int getTowers() {
        return towers;
    }

    /**
     * check if the school has this students color in his entrance
     * @param color the color to check
     * @return true if the color is present false otherwise
     */
    boolean hasStudentInEntrance (Color color){
        return entrance[color.getIndex()] > 0;
    }

    /**
     * get the number of student in this room of the requested color
     * @param c color requested
     * @return the number of student in this room of the requested color
     */
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

    /**
     * Add all students from the cloud to the entrance
     * @param c the cloud with the students
     */
    void addStudentFromCloud(Cloud c) {
        int[] students = c.getStudents();
        for (Color color : Color.values()){
            int index = color.getIndex();
            entrance[index] += students[index];
        }
    }

    /**
     * Add the number passed of tower in this school
     * @param number number of towers to be added
     */
    void addTowers(int number) {
        towers += number;
    }

    /**
     * remove the number passed of tower from this school (cannot go below 0)
     * @param number number of towers to be removed
     * @return true if the school hasn't more towers
     */
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
