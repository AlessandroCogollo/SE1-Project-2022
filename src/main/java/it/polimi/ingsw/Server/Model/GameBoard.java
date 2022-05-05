package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Characters.*;
import it.polimi.ingsw.Server.Model.Characters.Character;

import java.util.*;

public class GameBoard implements Iterable<Cloud>{

    private final GameInitializer gInit;
    private final ArrayList<Cloud> clouds;
    private final Collection<Character> charactersDeck;
    private final Islands islands;
    private final Professors professors;

    private Character activeCharacter;


    public GameBoard (GameInitializer gInit){

        this.gInit = gInit;

        int numOfPlayer = gInit.getPlayersNumber();
        this.clouds = new ArrayList<>(numOfPlayer);
        for(int i = 0; i < numOfPlayer; i++){
            clouds.add(new Cloud(i, gInit));
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

            //this modifies the island leaving it with the right tower in the end
            calcInfluence(mN);

            //now we check if there are some aggregation to do, and we will do it
            islands.aggregateIsland(mN);
        }
        else {
            //else only remove the banCard and return it to the apothecary
            mN.removeBanCard();
            for (Character c: charactersDeck)
                if (c.getId() == 0)
                    ((Apothecary) c).addBanCard();
        }

        if (islands.getIslandsNumber() <= 3 && gInit.getWinningPlayerId() == -1) { //if a player has no more tower is the winner in any case
            System.out.println("\n\n Island <= 3 \n");
            gInit.checkWin();
        }
    }


    //knight: adds 2 points to the current player (id = 7) --- OK
    //drunkard: given a color, blocks the influence calc for those students (id = 4) --- OK
    //minotaur: blocks the influence calc for the towers (id = 8) --- OK
    //cook: obtains the professor even if in withdraw in number of students in the room (id = 3) --- OK


    //returns the influence of each player
    HashMap<Player, Integer> getInfluenceMap(Island c){

        HashMap<Player, Integer> playersInfluence = new HashMap<>(gInit.getPlayersNumber()); // map of the influences

        //initializing playersInfluence
        for(Player p: gInit){
            playersInfluence.put(p, 0);
        }

        /* Knight effect */
        if( activeCharacter != null && activeCharacter.getId()==7){
            Player p = gInit.getRoundHandler().getCurrent();
            playersInfluence.put(p, 2);
        }

        int [] students = c.getStudents();
        Player p;
        //fill the players influence array
        int temp = 0;
        for (int i = 0; i < Color.getNumberOfColors(); i++){

            /* Cook effect */
            if(activeCharacter != null && activeCharacter.getId() == 3 && ((Cook) getActiveCharacter()).getProfessor().isPresent() && Color.getColorById(i).equals(((Cook) getActiveCharacter()).getProfessor().get())){
                p = gInit.getRoundHandler().getCurrent(); //if i is the cook-color, p is the current player
            }

            else{
                p = this.professors.getPlayerWithProfessor(Color.getColorById(i)); // else p is who owns the i-color
            }
            if (p != null) { //if somebody owns the professor of the i-color
                playersInfluence.replace(p, playersInfluence.get(p) + students[i]); // sum the influence of the player
            }
        }

        /* Drunkard effect */
        if(activeCharacter != null && activeCharacter.getId() == 4){
            p = this.professors.getPlayerWithProfessor(((Drunkard) getActiveCharacter()).getColor()); // p owns the drunkard-color
            playersInfluence.replace(p, 0);
        }
        return playersInfluence;
    }

    // returns an ArrayList of int with the black, white and grey influences
    ArrayList<Integer> getInfluences(Island c){

        //get the influence of each player
        HashMap<Player, Integer> playersInfluence = getInfluenceMap(c);

        //merging influences of mates
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

        /*Minotaur effect*/
        if(activeCharacter != null && activeCharacter.getId() != 8) {
            //if there are towers, add them to the influence
            int towerCount = c.getTowerCount();
            int towerColor = c.getTowerColor();
            if (towerCount>0){
                if (towerColor == 1) blackInfluence = blackInfluence + towerCount;
                else if (towerColor == 2) whiteInfluence = whiteInfluence + towerCount;
                else if (towerColor == 3) greyInfluence = greyInfluence + towerCount;
            }
        }

        ArrayList<Integer> influences = new ArrayList<>(Color.getNumberOfColors());
        influences.add(blackInfluence);
        influences.add(whiteInfluence);
        influences.add(greyInfluence);
        return influences;
    }

    //replaces the current towers on the Island c with the ones of the player p
    void replaceTowers(Island c, Player p){
        int count = c.getTowerCount();
        //the old towers come back to the owner
        Player owner = getPlayerWithTower(c.getTowerColor()).get(0);
        owner.receiveTowerFromIsland(count);
        //put the new towers
        p.moveTowerToIsland(count);
        c.setTowerCount(count);
        c.setTowerColor(p.getTowerColor());
    }

    //check if there's a tower to add or all towers to be replaced, and in that case does that
    public void calcInfluence(Island c) {

        ArrayList<Integer> influences = getInfluences(c);
        ArrayList<Integer> max = findMax(influences);

        //in case of withdraw nothing changes, else:
        if (max.size() == 1){
            //find a player who owns the max-influence tower color
            Player p = getPlayerWithTower(max.get(0)).get(0);
            //if there is no tower, add it
            if(c.getTowerCount() == 0){
                //remove one tower from the player
                p.moveTowerToIsland(1);
                //and added to the island
                c.setTowerCount(1);
                c.setTowerColor(p.getTowerColor());
            }
            //else if there are towers
            else {
                //if the max influence is different from the previous one, replace all the towers, and the tower count remains the same
                if (max.get(0) != c.getTowerColor()) {
                    replaceTowers(c,p);
                }
                //else if the max influence remains the same nothing changes
            }
        }

    }
    //returns an ArrayList with the players that owns the given tower color
    ArrayList<Player> getPlayerWithTower(int towerColor){
        ArrayList<Player> list = new ArrayList<>();
        for (Player p : gInit){
            if (p.getTowerColor() == towerColor){
                list.add(p);
            }
        }
        return list;
    }

    //returns the higher TowerColor
    private static ArrayList<Integer> findMax (ArrayList<Integer> influences){
        int max = 0;
        int black = influences.get(0);
        int white = influences.get(1);
        int grey = influences.get(2);
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

    @Override
    public Iterator<Cloud> iterator() {
        return clouds.iterator();
    }
}
