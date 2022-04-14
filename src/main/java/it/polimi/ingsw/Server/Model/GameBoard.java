package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

class GameBoard {

    private final GameInitializer gInit;
    private final ArrayList<Cloud> clouds;
    private final Collection<Character> charactersDeck;
    private final Islands islands;
    private final Professors professors;

    private Character activeCharacter;


    GameBoard (GameInitializer gInit){

        this.gInit = gInit;

        int numOfPlayer = gInit.getPlayersNumber();
        this.clouds = new ArrayList<>(numOfPlayer);
        for(int i = 0; i < numOfPlayer; i++){
            clouds.add(new Cloud( i, gInit));
        }
        this.charactersDeck = CharacterFactory.getNewGameDeck(gInit);

        this.islands = gInit.getIslands();
        this.professors = gInit.getProfessors();
        this.activeCharacter = null;
    }


    Character getActiveCharacter() {
        return activeCharacter;
    }

    boolean existsCloud (int cloudId){
        boolean exist = false;
        for (Cloud c: clouds)
            if (c.getId() == cloudId){
                exist = true;
                break;
            }
        return exist;
    }

    boolean existsCharacter (int characterId){
        boolean exists = false;
        for (Character c : charactersDeck)
            if (c.getId() == characterId){
                exists = true;
                break;
            }
        return  exists;
    }

    Cloud getCloudById(int cloudId) {
        for (Cloud c: clouds)
            if (c.getId() == cloudId)
                return c;
        return null;
    }

    Character getCharacterById(int characterId) {
        for (Character x: charactersDeck)
            if (x.getId() == characterId)
                return x;
        return null;
    }

    //reset all cloud at the end of a round
    void populateClouds(){
        for(Cloud cloud : clouds){
            cloud.setStudents();
        }
    }

    void playCharacter(Character c, Object obj) {
        this.activeCharacter = c;
        if (c != null)
            c.use(obj);
    }

    boolean isCharacterPlayed (){
        return this.activeCharacter != null;
    }

    void moveMotherNature(int count){

        //move mother nature of count position
        islands.nextMotherNature(count);


        Island mN = islands.getMotherNature();


        if (mN.getBanCard() == 0){
            //if there isn't the ban card

            //this modify the island leaving it with the right tower in the end
            calcInfluence(mN);

            //now we check if there are some aggregation to do and we will do it
            islands.aggregateIsland(mN);
        }
        else {
            //else only remove the bancard and return it to the apothecary
            mN.removeBanCard();
            for (Character c: charactersDeck)
                if (c.getId() == 0)
                    ((Apothecary) c).addBanCard();
        }

        if (islands.getIslandsNumber() <= 3)
            gInit.checkWin();
    }

    //todo divide in smaller function
    void calcInfluence(Island c) {

        HashMap<Player, Integer> playersInfluence = new HashMap<>(gInit.getPlayersNumber()); // map of the influences

        //initializing playersInfluence
        for(Player p: gInit){
            playersInfluence.put(p, 0);
        }
        int [] students = c.getStudents();
        Player p;
        //fill the players influence array
        int temp = 0;
        for (int i = 0; i < Color.getNumberOfColors(); i++){
            p = this.professors.getPlayerWithProfessor(Color.getColorById(i)); // find who owns the i-color
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

        int towerCount = c.getTowerCount();
        int towerColor = c.getTowerColor();
        //if there are towers, add them to the influence
        if (towerCount>0){
            if (towerColor == 1) blackInfluence = blackInfluence + towerCount;
            else if (towerColor == 2) whiteInfluence = whiteInfluence + towerCount;
            else if (towerColor == 3) greyInfluence = greyInfluence + towerCount;
        }

        //finding the max
        ArrayList<Integer> max = findMax(blackInfluence, whiteInfluence, greyInfluence);

        //in case of withdraw nothing changes
        //else
        //todo moving the tower between players
        if (max.size() == 1){
            if(towerCount == 0){
                //if there is no tower, add it with the right color
                towerColor = max.get(0);
                towerCount = 1;
            }
            else {
                //else if there are towers
                if (max.get(0) != towerColor) {
                    //if the max influence is different from the previous one, change the tower color, and the tower count remains the same
                    towerColor = max.get(0);
                }
                //else if the max influence remains the same nothing changes
            }
        }
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
