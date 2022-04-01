package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Island {
    private int id;
    private int towerColor;
    private final int[] students;
    private Optional<Boolean> banCard;
    private int towerCount;
    private GameInitializer gInit;

    public Island(GameInitializer gInit, int id){
        this.gInit = gInit;
        this.students = new int[Color.getNumberOfColors()];
        for (int i : this.students) {
            this.students[i] = 0;
        }
        this.towerColor = -1;
        this.banCard = Optional.empty();
        this.towerCount = 0;

    }

    public int getId() {
        return id;
    }

    public void setBanCard(Optional<Boolean> banCard) {
        this.banCard = banCard;
    }

    protected void setTowerColor(int towerColor) {
        this.towerColor = towerColor;
    }

    protected void setTowerCount(int towerCount) {
        this.towerCount = towerCount;
    }

    public int getTowerCount() {
        return towerCount;
    }

    public int[] getStudents() {
        return students;
    }

    public Optional<Integer> getTowerColor() {
        return Optional.of(towerColor);
    }

    public Optional<Boolean> getBanCard() {
        return banCard;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public void CalcInfluence() {
        ArrayList<Player> players = gInit.getPlayers();
        ArrayList<Integer> playersInfluence = new ArrayList<>(5);
        Player p;
        int p_index;
        //fill the players influence array
        for (int i=0; i < this.students.length; i++){
            p = Professors.getPlayerWithProfessor(Color.getColorById(i)); // find who owns the i-color
            p_index = playersInfluence.indexOf(p); //find the index of that player in playersInfluence
            playersInfluence.set(p_index, playersInfluence.get(p_index) + students[i]); // sum the influence of the player
            }
        //creating blackinfluence, whiteinfluence and greyinfluence
        int blackInfluence = 0, whiteInfluence = 0, greyInfluence = 0;
        for (int k=0; k<players.size(); k++) {
            if (players.get(k).getTowerColor() == 1){ //if the k-player's color is black
                blackInfluence = blackInfluence + playersInfluence.get(k);
            }
            else if (players.get(k).getTowerColor() == 2){ //if the k-player's color is white
                whiteInfluence = whiteInfluence + playersInfluence.get(k);
            }
            else if (players.get(k).getTowerColor() == 3){ //if the k-player's color is grey
                greyInfluence = greyInfluence + playersInfluence.get(k);
            }
        }

        //if there are towers, add them to the influence
        if (towerCount>0){
            if (this.towerColor == 1) blackInfluence = blackInfluence + towerCount;
            else if (this.towerColor == 2) whiteInfluence = whiteInfluence + towerCount;
            else if (this.towerColor == 3) greyInfluence = greyInfluence + towerCount;
        }

        //finding the max
        ArrayList<Integer> max = findMax(blackInfluence, whiteInfluence, greyInfluence);

        //in case of withdraw nothing changes
        //else
        if (max.size() == 1){
            if(towerCount == 0){
                //if there is no tower, add it with the right color
                this.towerColor = max.get(0);
                this.towerCount = 1;
            }
            else {
                //else if there are towers
                if (max.get(0) != this.towerColor) {
                    //if the max influence is different from the previous one, change the tower color, and the tower count remains the same
                    this.towerColor = max.get(0);
                }
                //else if the max influence remains the same nothing changes
            }
        }
    }

    public void AddStudent(Color color){
        students[color.getIndex()] = students[color.getIndex()] + 1;
        CalcInfluence();
    }
    //returns the higher TowerColor
     ArrayList<Integer> findMax(int black, int white, int grey){
        int max = 0;
        ArrayList<Integer> temp = new ArrayList<>();
        if (black>max) {
            max = black;
            temp.add(1);
        }
        if (white>max) {
            max = white;
            temp.clear();
            temp.add(2);
        }
        else if (white == max){
            temp.add(2);
        }
        if (grey>max) {
            temp.clear();
            temp.add(3);
        }
        else if (grey == max){
            temp.add(3);
        }
        return temp;
     }
}
