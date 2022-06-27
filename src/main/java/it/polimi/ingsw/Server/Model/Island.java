package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

public class Island {

    private final int id;
    private final int[] students;
    private int towerColor;
    private int banCard;
    private int towerCount;
    private boolean merged = false;

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

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public boolean isMerged() {
        return merged;
    }

    public int getId() {
        return id;
    }

    public int getTowerCount() {
        return towerCount;
    }

    public int[] getStudents() {
        return students;
    }

    public int getTowerColor() {
        return towerColor;
    }

    public int getBanCard() {
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
