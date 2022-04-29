package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

//player class that implements the real methods that changes the model
class Player implements Iterable<Assistant>{

    private final int id;
    private final int towerColor; // 1 black, 2 white, 3 gray
    private final Optional<Player> mate;

    protected final School school;
    private final Collection<Assistant> deck;

    protected final GameInitializer gameInitializer;

    private Assistant activeAssistant;

    Player (int id, int towerColor, Player mate, GameInitializer gameInitializer, School school) {
        this.id = id;
        this.towerColor = towerColor;
        this.mate = Optional.ofNullable(mate);
        this.gameInitializer = gameInitializer;
        this.school = school;
        this.deck = Assistant.getNewAssistantDeck();
        this.activeAssistant = null;
    }

    int getId() {
        return id;
    }

    int getTowerColor() {
        return towerColor;
    }

    School getSchool() {return school;}

    Assistant getActiveAssistant() {
        return activeAssistant;
    }

    boolean hasAssistant (Assistant x){
        return deck.contains(x);
    }

    boolean hasStudent (Color color){
        return school.hasStudentInEntrance(color);
    }

    // play assistant will remove the assistant from the student's deck and set it as is active assistant, that will be changed only in the next planning phase
    void playAssistant (Assistant x){
        if (x != null)
            deck.remove(x);
        activeAssistant = x;
        if (deck.size() == 0) {
            System.out.println("\n\n Assistant finished \n");
            gameInitializer.getRoundHandler().setFinalRound();
        }
    }

    // check if the movement is towards his room or to an island
    void moveStudent (Color c, int destinationId){
        if (destinationId != -1){
            Color student = school.moveStudentFromEntrance(c);
            gameInitializer.getIslands().addStudentToIsland(student, destinationId);
        }
        else {
            school.moveStudentToRoom(c);
            gameInitializer.getProfessors().updateProfessors();
        }
    }

    // move mother nature calls the appropriate method in board
    void moveMotherNature (int position){
        gameInitializer.getBoard().moveMotherNature(position);
    }

    // choose cloud call the method in school for add all the students
    void chooseCloud (Cloud c){
        school.addStudentFromCloud(c);
    }

    //methods for the board to manage the tower, if it's a game with 4 players there will be some players with the mate attribute not null
    int getTowers (){
        return mate.map(Player::getTowers).orElseGet(school::getTowers);
    }

    void moveTowerToIsland (int number){
        if (mate.isPresent()){
            mate.get().moveTowerToIsland(number);
        }
        else if (school.removeTowers(number)){
            System.out.println("\n\n Player : " + id + "has finished the towers \n");
            gameInitializer.checkWin(this);
        }
    }

    void receiveTowerFromIsland (int number){
        if (mate.isPresent()){
            mate.get().receiveTowerFromIsland(number);
        }
        else
            school.addTowers(number);
    }

    int getNumberOfStudentInRoomByColor (Color c){
        return school.getNumberOfStudentInRoomByColor(c);
    }

    @Override
    public Iterator<Assistant> iterator() {
        return deck.iterator();
    }


    //factory method for generate all player or advance player if it is an advanced game, with the exception of 4 players
    static Collection<Player> factoryPlayers (int[] ids, GameInitializer gI) {

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
}
