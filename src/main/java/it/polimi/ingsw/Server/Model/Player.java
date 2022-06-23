package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Message.ModelMessage.PlayerSerializable;

import java.util.*;

//player class that implements the real methods that changes the model
public class Player implements Iterable<Assistant>{

    private final int id;
    private final int towerColor; // 1 black, 2 white, 3 gray
    private final Optional<Player> mate;

    protected final School school;
    private final Collection<Assistant> deck;

    protected final GameInitializer gameInitializer;

    private Assistant activeAssistant = null;

    //not public used only by factory
    Player (int id, int towerColor, Player mate, GameInitializer gameInitializer, School school) {
        this.gameInitializer = gameInitializer;
        this.mate = Optional.ofNullable(mate);

        this.id = id;
        this.towerColor = towerColor;
        this.school = school;
        this.deck = Assistant.getNewAssistantDeck();
    }

    public Player(GameInitializer gameInitializer, PlayerSerializable p, Player mate) {
        this.gameInitializer = gameInitializer;
        this.mate = Optional.ofNullable(mate);

        this.id = p.getId();
        this.towerColor = p.getTowerColor();
        this.school = p.getSchool();
        this.activeAssistant = Assistant.getAssistantByValue(p.getActiveAssistant());
        this.deck = Assistant.getResumedAssistantDeck(p.getAssistantDeck());
    }

    public int getId() {
        return id;
    }

    public int getTowerColor() {
        return towerColor;
    }

    public School getSchool() {return school;}

    public Assistant getActiveAssistant() {
        return activeAssistant;
    }

    public boolean hasAssistant (Assistant x){
        return deck.contains(x);
    }

    public boolean hasStudent (Color color){
        return school.hasStudentInEntrance(color);
    }

    // play assistant will remove the assistant from the student's deck and set it as is active assistant, that will be changed only in the next planning phase
    public void playAssistant (Assistant x){
        if (x != null)
            deck.remove(x);
        activeAssistant = x;
        if (deck.size() == 0) {
            System.out.println("\n\n Assistant finished \n");
            gameInitializer.getRoundHandler().setFinalRound();
        }
    }

    // check if the movement is towards his room or to an island
    public void moveStudent (Color c, int destinationId){
        if (destinationId != -1){
            Color student = school.removeStudentFromEntrance(c);
            gameInitializer.getIslands().addStudentToIsland(student, destinationId);
        }
        else {
            school.moveStudentToRoom(c);
            gameInitializer.getProfessors().updateProfessors();
        }
    }

    // move mother nature calls the appropriate method in board
    public void moveMotherNature (int position){
        gameInitializer.getBoard().moveMotherNature(position);
    }

    // choose cloud call the method in school for add all the students
    public void chooseCloud (Cloud c){
        school.addStudentFromCloud(c);
    }

    //methods for the board to manage the tower, if it's a game with 4 players there will be some players with the mate attribute not null
    public int getTowers (){
        return mate.map(Player::getTowers).orElseGet(school::getTowers);
    }

    public void moveTowerToIsland (int number){
        if (mate.isPresent()){
            mate.get().moveTowerToIsland(number);
        }
        else if (school.removeTowers(number)){
            System.out.println("\n\n Player : " + id + "has finished the towers \n");
            gameInitializer.checkWin(this);
        }
    }

    public void receiveTowerFromIsland (int number){
        if (mate.isPresent()){
            mate.get().receiveTowerFromIsland(number);
        }
        else
            school.addTowers(number);
    }

    public int getNumberOfStudentInRoomByColor (Color c){
        return school.getNumberOfStudentInRoomByColor(c);
    }

    @Override
    public Iterator<Assistant> iterator() {
        return deck.iterator();
    }


    //factory method for generate all player or advance player if it is an advanced game, with the exception of 4 players
    public static Collection<Player> factoryPlayers (int[] ids, GameInitializer gI) {

        Bag bag = gI.getBag();
        int gameMode = gI.getGameMode();

        //generate the collection with the length needed
        Collection<Player> p = new ArrayList<>(ids.length);

        //switch case for number of player that changes the school and color of tower; in each case there's the check for the game mode for create a normal player or an advanced one
        switch (ids.length) {
            case 2 -> {
                School school2_1 = new School(8, 7, bag);
                School school2_2 = new School(8, 7, bag);
                if (gameMode == 0) {
                    p.add(new Player(ids[0], 1, null, gI, school2_1));
                    p.add(new Player(ids[1], 2, null, gI, school2_2));
                } else {
                    p.add(new AdvancedPlayer(ids[0], 1, null, gI, school2_1));
                    p.add(new AdvancedPlayer(ids[1], 2, null, gI, school2_2));
                }
            }
            case 3 -> {
                School school3_1 = new School(6, 9, bag);
                School school3_2 = new School(6, 9, bag);
                School school3_3 = new School(6, 9, bag);
                if (gameMode == 0) {
                    p.add(new Player(ids[0], 1, null, gI, school3_1));
                    p.add(new Player(ids[1], 2, null, gI, school3_2));
                    p.add(new Player(ids[2], 3, null, gI, school3_3));
                } else {
                    p.add(new AdvancedPlayer(ids[0], 1, null, gI, school3_1));
                    p.add(new AdvancedPlayer(ids[1], 2, null, gI, school3_2));
                    p.add(new AdvancedPlayer(ids[2], 3, null, gI, school3_3));
                }
            }

            // for the case 4 there are 2 types of player, 1 like a normal player in a 2players Game and 1 with no towers, in that case i've added a mate tha can manage the tower
            case 4 -> {
                School school4_1 = new School(8, 7, bag);
                School school4_2 = new School(8, 7, bag);
                School school4s_1 = new School(0, 7, bag); //special school with no tower
                School school4s_2 = new School(0, 7, bag); //special school with no tower
                int iA = 0, iB = 0;
                Player tempA = null, tempB = null;
                if (gameMode == 0) {

                    for (int x : ids) {

                        //that's why the method need that the teammate need the same final bit
                        if (x % 2 == 0) {

                            // the first one of the team get the normal school
                            if (iA == 0) {
                                tempA = new Player(x, 1, null, gI, school4_1); // we save the pointer to that player for the next team member
                                p.add(tempA);
                                iA++;
                            } else {
                                p.add(new Player(x, 1, tempA, gI, school4s_1)); //here we set the mate with the pointer saved
                            }
                        } else {

                            // the same for the other team
                            if (iB == 0) {
                                tempB = new Player(x, 2, null, gI, school4_2);
                                p.add(tempB);
                                iB++;
                            } else {
                                p.add(new Player(x, 2, tempB, gI, school4s_2));
                            }
                        }
                    }
                }

                //exactly the same but with advanced player instead of a normal player
                else {
                    for (int x : ids) {
                        if (x % 2 == 0) {
                            if (iA == 0) {
                                tempA = new AdvancedPlayer(x, 1, null, gI, school4_1);
                                p.add(tempA);
                                iA++;
                            } else {
                                p.add(new AdvancedPlayer(x, 1, tempA, gI, school4s_1));
                            }
                        } else {
                            if (iB == 0) {
                                tempB = new AdvancedPlayer(x, 2, null, gI, school4_2);
                                p.add(tempB);
                                iB++;
                            } else {
                                p.add(new AdvancedPlayer(x, 2, tempB, gI, school4s_2));
                            }
                        }
                    }
                }
            }
        }
        return p;
    }

    public static Collection<Player> factoryPlayers (GameInitializer gI, List<PlayerSerializable> playerList) {

        int gameMode = gI.getGameMode();

        //generate the collection with the length needed
        Collection<Player> players = new ArrayList<>(playerList.size());

        if (gameMode == 0){
            if (playerList.size() != 4) {
                for (PlayerSerializable p: playerList){
                    players.add(new Player(gI, p, null));
                }
            }
            else {
                Player p0 = null, p1 = null;
                List<PlayerSerializable> remaining = new ArrayList<>(2);
                for (PlayerSerializable p: playerList){
                    if (p.getSchool().getTowers() != 0) {
                        Player temp = new Player(gI, p, null);
                        players.add(temp);
                        if (temp.getId() % 2 == 0)
                            p0 = temp;
                        else
                            p1 = temp;
                    }
                    else {
                        remaining.add(p);
                    }
                }

                for (PlayerSerializable p: remaining){
                    if (p.getId() % 2 == 0){
                        players.add(new Player(gI, p, p0));
                    }
                    else {
                        players.add(new Player(gI, p, p1));
                    }
                }
            }
        }
        else {
            if (playerList.size() != 4) {
                for (PlayerSerializable p: playerList){
                    players.add(new AdvancedPlayer(gI, p, null));
                }
            }
            else {
                Player p0 = null, p1 = null;
                List<PlayerSerializable> remaining = new ArrayList<>(2);
                for (PlayerSerializable p: playerList){
                    if (p.getSchool().getTowers() != 0) {
                        Player temp = new AdvancedPlayer(gI, p, null);
                        players.add(temp);
                        if (temp.getId() % 2 == 0)
                            p0 = temp;
                        else
                            p1 = temp;
                    }
                    else {
                        remaining.add(p);
                    }
                }

                for (PlayerSerializable p: remaining){
                    if (p.getId() % 2 == 0){
                        players.add(new AdvancedPlayer(gI, p, p0));
                    }
                    else {
                        players.add(new AdvancedPlayer(gI, p, p1));
                    }
                }
            }
        }
        return players;
    }
}
