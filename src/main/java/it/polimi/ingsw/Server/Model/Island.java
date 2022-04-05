package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class Island {
    private final int id;
    private final int[] students;
    private int towerColor;
    private boolean banCard;
    private int towerCount;
    private GameInitializer gInit;
    private GameBoard board;

    public Island(GameBoard board, int id, GameInitializer gInit){
        this.id = id;

        this.students = new int[Color.getNumberOfColors()];
        for (int i=0; i<Color.getNumberOfColors(); i++) {
            this.students[i] = 0;
        }

        this.gInit = gInit;
        this.board = board;


        this.towerColor = -1;
        this.towerCount = 0;
        this.banCard = false;
    }

    int getId() {
        return id;
    }

    int getTowerCount() {
        return towerCount;
    }

    int[] getStudents() {
        return students;
    }

    int getTowerColor() {
        return towerColor;
    }

    boolean getBanCard() {
        return banCard;
    }

    void setBanCard() {
        this.banCard = true;
    }

    void setBanCard(boolean value) {
        this.banCard = value;
    }

    void setTowerColor(int towerColor) {
        this.towerColor = towerColor;
    }

    void setTowerCount(int towerCount) {
        this.towerCount = towerCount;
    }


    //todo, possible to move it in islands class for don't propagate the pointer to game init and board to all the island
    void CalcInfluence() {

        HashMap<Player, Integer> playersInfluence = new HashMap<>(gInit.getNumberOfPlayers()); // map of the influences

        //initializating playersInfluence
        for(Player p: gInit){
            playersInfluence.put(p, 0);
        }

        Player p;
        //fill the players influence array
        int temp = 0;
        for (int i = 0; i < Color.getNumberOfColors(); i++){
            p = board.getProfessors().getPlayerWithProfessor(Color.getColorById(i)); // find who owns the i-color
            if (p != null) { //if somebody owns the professor of the i-color
                temp = playersInfluence.get(p);
                playersInfluence.replace(p, temp + students[i]); // sum the influence of the player
            }
        }

        //creating blackinfluence, whiteinfluence and greyinfluence
        int blackInfluence = 0, whiteInfluence = 0, greyInfluence = 0;

        for (Player k : gInit) {
            if (k.getTowerColor() == 1){ //if the k-player's color is black
                blackInfluence = blackInfluence + playersInfluence.get(k);
            }
            else if (k.getTowerColor() == 2){ //if the k-player's color is white
                whiteInfluence = whiteInfluence + playersInfluence.get(k);
            }
            else if (k.getTowerColor() == 3){ //if the k-player's color is grey
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
        //todo moving the tower between players
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

    //adds the student to the island
    void AddStudent(Color color){
        students[color.getIndex()] = students[color.getIndex()] + 1;
    }

    //returns the higher TowerColor
    private static ArrayList<Integer> findMax (int black, int white, int grey){
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
