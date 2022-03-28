package it.polimi.ingsw.Server.Model;

//The school class implements a container for all the stuffs of the player during the game and can only be modified from the class Student itself
class School {

    private int[] entrance;
    private int[] room;
    private int towers;


    /*
    @requires (towers == 6 || towers == 8 || towers == 0) && (entranceStudent == 7 || entranceStudent == 9) && bag != null
    @ensures (*the school is ready for the start of the game*)
     */
    School(int towers, int entranceStudent, Bag bag) {
        this.towers = towers;
        this.room = new int[Color.getNumberOfColors()];
        for (int i = 0; i < this.room.length; i++)
            this.room[i] = 0;
        //todo bag
        //this.entrance = bag.drawStudents(entranceStudent);
    }

    public int getTowers() {
        return towers;
    }

    boolean hasStudent (Color color){
        return entrance[color.getIndex()] > 0;
    }

    //return true if the player will get a coin
    boolean moveStudentToRoom (Color color){
        int index = color.getIndex();
        entrance[index]--;
        room[index]++;
        if (room[index] % 3 == 0)
            return true;
        return false;
    }

    void addStudentFromCloud(Cloud c) {
        //todo cloud
        /*
        Collection<Color> students = c.getStudents();
        for (Color x: students){
            entrance[x.getIndex()]++;
        }
        */
    }

    void addTowers(int number) {
        towers += number;
    }

    boolean removeTowers(int number) {
        for (int i = 0; i < number; i++){
            towers--;
            if (towers == 0) //win
                return true;
        }
        return false;
    }

    Color moveStudentFromEntrance(Color color) {
        entrance[color.getIndex()]--;
        return color;
    }
}
