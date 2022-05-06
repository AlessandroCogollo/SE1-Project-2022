package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

public class Island {

    private final int id;
    private final int[] students;
    private int towerColor;
    private int banCard;
    private int towerCount;

    public Island(int id){
        this.id = id;

        this.students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++) {
            this.students[i] = 0;
        }

        this.towerColor = -1;
        this.towerCount = 0;
        this.banCard = 0;
    }

    public int getId() {
        return id;
    }

    public int getTowerCount() {
        return towerCount;
    }

    int[] getStudents() {
        return students;
    }

    int getTowerColor() {
        return towerColor;
    }

    int getBanCard() {
        return banCard;
    }

    //adds the student to the island
    public void addStudent(Color color){
        students[color.getIndex()]++;
    }

    public void setBanCard() {
        this.banCard++;
    }

    void removeBanCard() {
        this.banCard--;
    }

    void setTowerColor(int towerColor) {
        this.towerColor = towerColor;
    }

    void setTowerCount(int towerCount) {
        this.towerCount = towerCount;
    }



}
