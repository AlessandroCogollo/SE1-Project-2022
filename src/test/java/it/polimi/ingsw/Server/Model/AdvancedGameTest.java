package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.Phases.ActionPhase;
import it.polimi.ingsw.Server.Model.Phases.Phase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedGameTest extends GameTest {

    @Test
    @DisplayName("Testing playAssistant() Black Box")
    void playAssistantBlackBox() {

        Queue<Player> tempOrder = new LinkedList<>();

        int[][] ids = {
                {1, 2},             // check two players
                {1, 2, 3},          // check three players
                {1, 2, 3, 4}        // check four players
        };

        for (int i = 0; i < ids.length; i++) {

            GameInitializer g = new GameInitializer(1, i+2);
            RoundHandler r = new RoundHandler(g);

            g.createAllGame(ids[i], r);
            r.start();

            AdvancedGame game = new AdvancedGame(ids.length, g, r);

            // check if planning order is correct && correct assistant played
            for (int j = 0; j < ids[i].length; j++) {
                Player p = r.getCurrent();
                tempOrder.add(p);
                assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), j+1));
                assertFalse(p.hasAssistant(Assistant.getAssistantByValue(j+1)));
            }
            for (Player p: r.getPlanningOrder()) {
                assertEquals(p, tempOrder.poll());
            }
        }
    }

    @Test
    @DisplayName("Testing moveStudent() Black Box")
    void moveStudentBlackBox() {
        int[][] ids = {
                {1, 2},             // check two players
                {1, 2, 3},          // check three players
                {1, 2, 3, 4}        // check four players
        };

        for (int i = 0; i < ids.length; i++) {

            System.out.println("TestSet N°"+ i);

            for (int l = 0; l < 2; l++) {

                System.out.println("Test N°" + l);

                GameInitializer g = new GameInitializer(1, i+2);
                RoundHandler r = new RoundHandler(g);

                g.createAllGame(ids[i], r);
                r.start();

                AdvancedGame game = new AdvancedGame(ids.length, g, r);

                // init actionPhase order
                for (int j = 0; j < ids[i].length; j++) {
                    Player p = r.getCurrent();
                    game.playAssistant(p.getId(), j+1);
                }

                // action phase
                for (int j = 0; j < ids[i].length; j++) {

                    Player p = r.getCurrent();
                    System.out.println("Entrance Of Player " + p.getId() + " : " + Arrays.toString(p.getSchool().getCopyOfEntrance()));

                    for (int m = 0; m < 3; m++) {

                        int k = 0;
                        int IslandId = (int)(Math.random()*(g.getIslands().getIslandsNumber())-1);
                        System.out.println("Island ID: " + IslandId);
                        int StudentsInEntrance;

                        StudentsInEntrance = 0;
                        for (int z = 0; z < p.getSchool().getCopyOfEntrance().length; z++) {
                            StudentsInEntrance += p.getSchool().getCopyOfEntrance()[z];
                        }

                        if (StudentsInEntrance > 0) {
                            do {
                                k = (int)(Math.random()*5);
                            } while ((p.getSchool().getCopyOfEntrance())[k] <= 0);
                        }

                        System.out.println("ColorID " + k + "- Number Of Students Of This Color: " + (p.getSchool().getCopyOfEntrance())[k]);

                        if (l == 0) {
                            int[] OldNumberOfStudentsInIsland = g.getIslands().getIslandFromId(IslandId).getStudents();
                            p.moveStudent(Color.getColorById(k), IslandId);
                            assertEquals((OldNumberOfStudentsInIsland[k]), g.getIslands().getIslandFromId(IslandId).getStudents()[k]);
                        } else {
                            int OldNumberOfStudentsInRoom = p.getSchool().getNumberOfStudentInRoomByColor(Objects.requireNonNull(Color.getColorById(k)));
                            p.moveStudent(Color.getColorById(k), -1);
                            assertEquals((OldNumberOfStudentsInRoom + 1), p.getSchool().getNumberOfStudentInRoomByColor(Objects.requireNonNull(Color.getColorById(k))));
                        }
                    }
                    p.moveMotherNature(p.getActiveAssistant().getMaxMovement());
                    int cloudId = (int)(Math.random()*ids[i].length);
                    p.chooseCloud(g.getBoard().getCloudById(cloudId));
                }
            }
        }
    }

    @Test
    void moveMotherNatureBlackBox() {

        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(1, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        AdvancedGame game = new AdvancedGame(id2.length, g, r);

        Player p = r.getCurrent();
        game.playAssistant(p.getId(), 1);

        p = r.getCurrent();
        game.playAssistant(p.getId(), 2);

        p = r.getCurrent();

        //Color[] colorPicked = new Color[3];
        Random rand = new Random(System.currentTimeMillis());
        //move 3 students
        int i = 0;
        while (i < 3) {
            Color c = Color.getColorById(rand.nextInt(Color.getNumberOfColors())); // get a random Color
            if (p.hasStudent(c)) {
                game.moveStudent(p.getId(), c.getIndex(), -1);
                //colorPicked[i] = c;
                i++;
            }
        }

        game.moveMotherNature(p.getId(), p.getActiveAssistant().getMaxMovement());

        Island mn = g.getIslands().getMotherNature();

        int[] mnStudents = mn.getStudents();

        int[] professor = g.getProfessors().getProfessorsCopy();


        boolean towerBuild = false;
        for (i = 0; i < Color.getNumberOfColors() && !towerBuild; i++){
            if (p.getId() == professor[i] && mnStudents[i] > 0) //the player has the professor and in the island there a student of that color
                towerBuild = true;
        }

        if (towerBuild)
            assertEquals(1, mn.getTowerCount());
        else
            assertEquals(0, mn.getTowerCount());
    }

    @Test
    void playCharacterBlackBox() {

        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(1, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        AdvancedGame game = new AdvancedGame(id2.length, g, r);

        Player p = r.getCurrent();

        //resetting until there is postman
        while (!g.getBoard().existsCharacter(9)){
            g = new GameInitializer(1, 2);
            r = new RoundHandler(g);

            g.createAllGame(id2, r);
            r.start();

            game = new AdvancedGame(id2.length, g, r);
        }

        game.playAssistant(p.getId(), 1);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 2);
        p = r.getCurrent();
        game.playCharacter(p.getId(), 9, null);
        assertEquals(g.getBoard().getActiveCharacter().getId(), 9);


    }


    //@RepeatedTest(1000)
    @Test //black box
    void completeGame(){
        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(0, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        Game game = new Game(id2.length, g, r);
        int i = 0;
        while(g.getWinningPlayerId() == -1){
            System.out.println("-------------------------- Turn : " + i++ + "---------------------------------");

            completeRound(g, r, game);

        }
        System.out.println("---------------------- Winner : player # " + g.getWinningPlayerId() + "-----------------------------");

    }


    void completeRound(GameInitializer g, RoundHandler r, Game game){



        //All the players play an assistant

        System.out.println(r.getPhase() + " " + r.getActionPhase());
        assertEquals(r.getPhase(), Phase.Planning);
        assertEquals(r.getActionPhase(), ActionPhase.NotActionPhase);

        Player p;
        Random rand = new Random(System.currentTimeMillis());
        int k;
        for (int i = 0; i < g.getPlayersNumber(); ){
            p = r.getCurrent();
            Assistant randAssistant = Assistant.getAssistantByValue(rand.nextInt(Assistant.getNumberOfAssistants()) + 1);
            if (r.canPLayAssistant(p, randAssistant) && p.hasAssistant(randAssistant)){
                int er = game.playAssistant(p.getId(), randAssistant.getValue());
                assertEquals(Errors.NO_ERROR.getCode(), er, Errors.getErrorsByCode(er).getDescription() + " with assistant value : " + randAssistant.getValue());
                System.out.println("#" + p.getId() + " -> " + "Play Assistant " + randAssistant.getValue() + " With Max Movements : " + randAssistant.getMaxMovement());
                i++;
            }
        }

        //randomize a list of clouds for players
        HashSet<Integer> clouds = new HashSet<>(g.getPlayersNumber());
        for (int i = 0; i < g.getPlayersNumber(); i++){
            int cloudId = rand.nextInt(g.getPlayersNumber());
            while (!g.getBoard().existsCloud(cloudId) || !clouds.add(cloudId)){ //linked hashset doesn't have duplicates
                cloudId = rand.nextInt(g.getPlayersNumber());
            }
        }
        int[] cloudIdRandom = new int[g.getPlayersNumber()];
        int z = 0;
        for (Integer c: clouds){
            cloudIdRandom[z++] = c;
        }

        //action phase for every player
        for(int t=0; t<g.getPlayersNumber(); t++){


            System.out.print("\n\n");

            //print game information
            System.out.println("############################## ISLANDS ##############################");
            //printing islands
            String towerColor = null;
            for(Island is : g.getIslands()){
                if (is.getTowerColor() == -1) {
                    towerColor = " ";
                }
                else if (is.getTowerColor() == 1){
                    towerColor = " Black";
                }
                else if (is.getTowerColor() == 2){
                    towerColor = " White";
                }
                else if (is.getTowerColor() == 3){
                    towerColor = " Grey";
                }
                System.out.println(
                        "{ID: " + is.getId() +
                        " , STUDENTS: " + Arrays.toString(is.getStudents()) +
                        " , TOWERS: " + is.getTowerCount()  + towerColor +
                        " , BAN CARD: " + is.getBanCard() +
                        "}" +
                        (g.getIslands().getMotherNature().equals(is) ? "  <-- MOTHER NATURE" : ""));

            }
            System.out.println("#####################################################################");
            System.out.println("###### PROFESSORS ######");
            System.out.println(Arrays.toString(g.getProfessors().getProfessorsCopy()));
            System.out.println("########################");
            System.out.println("###### BAG ######");
            System.out.println("Students in bag: " + Arrays.stream(g.getBag().getStudentsCopy()).sum()  + " -> " + Arrays.toString(g.getBag().getStudentsCopy()));
            System.out.println("########################");

            System.out.println("###### SCHOOL ######");
            for (Player pl : g){
                int tColor = pl.getTowerColor();
                if (tColor == -1) {
                    towerColor = " ";
                }
                else if (tColor == 1){
                    towerColor = " Black";
                }
                else if (tColor == 2){
                    towerColor = " White";
                }
                else if (tColor == 3){
                    towerColor = " Grey";
                }
                System.out.println("#" + pl.getId() + " with " + pl.getSchool().getTowers() + towerColor + " towers");

                System.out.println("# ENTRANCE -> " + Arrays.toString(pl.getSchool().getCopyOfEntrance()));
                System.out.println("# ROOM -> " + Arrays.toString(pl.getSchool().getCopyOfRoom()));
            }
            System.out.println("########################");


            System.out.println();
            System.out.println();
            //start the true action phase

            p = r.getCurrent();
            //move 2 students to the island k
            System.out.println("############ Current player : " + p.getId() + " -> " + r.getPhase() + " " + r.getActionPhase() + "########");
            assertEquals(r.getPhase(), Phase.Action);
            assertEquals(r.getActionPhase(), ActionPhase.MoveStudent);
            System.out.println("#" + p.getId() + " -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()) + " -- Initial");
            int errorCode;
            List<Integer> islandModified = new ArrayList<>();
            for (int i = 0; i < 2; ) {
                Color randColor = Color.getColorById(rand.nextInt(Color.getNumberOfColors()));
                    if (p.hasStudent(randColor)) {
                        do{
                            k = rand.nextInt(0, 12);
                            System.out.println("#" + p.getId() + " -> " + "Moving student to " + k);
                            errorCode = game.moveStudent(p.getId(), randColor.getIndex(), k);
                            System.out.println("#" + p.getId() + " -> " + Errors.getErrorsByCode(errorCode).getDescription());
                        } while (errorCode != 0);
                        System.out.println("#" + p.getId() + " -> " + "Moved student to " + k + "!");
                        islandModified.add(k);
                        assertEquals(Errors.NO_ERROR.getCode(), errorCode);
                        i++;
                    }
            }

            //move one student to the room, or, if not possible, to an island
            boolean movedToRoom = false;
            for (int i = 0; i < 1; ) {
                Color randColor = Color.getColorById(rand.nextInt(Color.getNumberOfColors()));
                if (p.hasStudent(randColor)) {
                    //if there's place for another student of that color in the room
                    System.out.println("#" + p.getId() + " -> " + "Moving student to his room");
                    errorCode = game.moveStudent(p.getId(), randColor.getIndex(), -1);
                    System.out.println("#" + p.getId() + " -> " + Errors.getErrorsByCode(errorCode).getDescription());
                    if (Errors.NO_ERROR.getCode() == errorCode) {
                        System.out.println("#" + p.getId() + " -> " + "Moved students to his room!");
                        assertEquals(Errors.NO_ERROR.getCode(), errorCode);
                        movedToRoom = true;
                    }
                    i++;
                }
            }
            if(!movedToRoom){
                for (int i = 0; i < 1; ){
                    Color randColor = Color.getColorById(rand.nextInt(Color.getNumberOfColors()));
                    if (p.hasStudent(randColor)){
                        do{
                            k = rand.nextInt(0, 12);
                            System.out.println("#" + p.getId() + " -> " + "Moving student to " + k);
                            errorCode = game.moveStudent(p.getId(), randColor.getIndex(), k);
                            System.out.println(Errors.getErrorsByCode(errorCode).getDescription());
                        } while (errorCode != 0);
                        System.out.println("#" + p.getId() + " -> " + "Moved student to " + k + "!");
                        System.out.println("# NEW ROOM OF " + p.getId() + " -> " + Arrays.toString(p.getSchool().getCopyOfRoom()));
                        assertEquals(Errors.NO_ERROR.getCode(), errorCode);
                        islandModified.add(k);
                        i++;
                    }
                }
            }

            System.out.println("# " + p.getId() + " ENTRANCE -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()) + " -- After Moving");
            System.out.println("# " + p.getId() + " ROOM -> " + Arrays.toString(p.getSchool().getCopyOfRoom()) + " -- After Moving");
            for (Integer i : islandModified) {
                Island is = g.getIslands().getIslandFromId(i);
                System.out.println(
                        "{ID: " + is.getId() +
                                " , STUDENTS: " + Arrays.toString(is.getStudents()) + " -- After Moving" +
                                (g.getIslands().getMotherNature().equals(is) ? "  <-- MOTHER NATURE" : ""));
            }
            //move mother nature
            System.out.println("############ Current player : " + p.getId() + " -> " + r.getPhase() + " " + r.getActionPhase() + "########");
            System.out.println("# Current Assistant " + p.getActiveAssistant().getValue() + " With Max Movements : " + p.getActiveAssistant().getMaxMovement());
            assertEquals(r.getPhase(), Phase.Action);
            assertEquals(r.getActionPhase(), ActionPhase.MoveMotherNature);
            int randomMovement = rand.nextInt(p.getActiveAssistant().getMaxMovement()) + 1;
            assertEquals(Errors.NO_ERROR.getCode(), game.moveMotherNature(p.getId(), randomMovement));
            System.out.println("# Moved mother nature by " + randomMovement + " position, new mother nature : " + g.getIslands().getMotherNature().getId());

            //win condition
            if (g.getWinningPlayerId() != -1){
                break;
            }

            //choose cloud and get students
            System.out.println("############ Current player : " + p.getId() + " -> " + r.getPhase() + " " + r.getActionPhase() + "########");
            assertEquals(r.getPhase(), Phase.Action);
            assertEquals(r.getActionPhase(), ActionPhase.ChooseCloud);


            /*int i;
            boolean found = false;
            int[] students;
            for(i=0; i<g.getPlayersNumber(); i++) {
                students = g.getBoard().getCloudById(i).getCopyOfDrawnStudents();
                for (int student : students) {
                    if (student > 0) {
                        System.out.println("#" + p.getId() + " -> " + Arrays.toString(students) + " -- Chosen Cloud");
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found) System.out.println("#" + p.getId() + " -> " + "All the clouds are empty");*/

            int cloudId = cloudIdRandom[t]; // t is a incremental value for players
            assertEquals(r.getCurrent(), p); //still the same player turn
            assertEquals(Errors.NO_ERROR.getCode(),game.chooseCloud(p.getId(), cloudId));

            System.out.println("# " + p.getId() + " ENTRANCE -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()) + " -- After Choosing Cloud");
        }

        if(g.getWinningPlayerId() != -1){
            //print game information
            System.out.println("############################## ISLANDS ##############################");
            //printing islands
            String towerColor = null;
            for(Island is : g.getIslands()){
                if (is.getTowerColor() == -1) {
                    towerColor = " ";
                }
                else if (is.getTowerColor() == 1){
                    towerColor = " Black";
                }
                else if (is.getTowerColor() == 2){
                    towerColor = " White";
                }
                else if (is.getTowerColor() == 3){
                    towerColor = " Grey";
                }
                System.out.println(
                        "{ID: " + is.getId() +
                                " , STUDENTS: " + Arrays.toString(is.getStudents()) +
                                " , TOWERS: " + is.getTowerCount()  + towerColor +
                                " , BAN CARD: " + is.getBanCard() +
                                "}" +
                                (g.getIslands().getMotherNature().equals(is) ? "  <-- MOTHER NATURE" : ""));

            }
            System.out.println("#####################################################################");
            System.out.println("###### PROFESSORS ######");
            System.out.println(Arrays.toString(g.getProfessors().getProfessorsCopy()));
            System.out.println("########################");

            System.out.println("###### SCHOOL ######");
            for (Player pl : g){
                int tColor = pl.getTowerColor();
                if (tColor == -1) {
                    towerColor = " ";
                }
                else if (tColor == 1){
                    towerColor = " Black";
                }
                else if (tColor == 2){
                    towerColor = " White";
                }
                else if (tColor == 3){
                    towerColor = " Grey";
                }
                System.out.println("#" + pl.getId() + " with " + pl.getSchool().getTowers() + towerColor + " towers");

                System.out.println("# ENTRANCE -> " + Arrays.toString(pl.getSchool().getCopyOfEntrance()));
                System.out.println("# ROOM -> " + Arrays.toString(pl.getSchool().getCopyOfRoom()));
            }
            System.out.println("########################");
        }


    }

    @Test //test only for error
    void playCharacter() {

        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(1, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        AdvancedGame game = new AdvancedGame(id2.length, g, r);

        Player p = r.getCurrent();

        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.playCharacter(1543, 1, null), "player not exists");
        assertEquals(Errors.NO_SUCH_CHARACTER.getCode(), game.playCharacter(p.getId(), 300, null), "character not exists");

        //resetting until there is postman
        while (!g.getBoard().existsCharacter(9)){
            g = new GameInitializer(1, 2);
            r = new RoundHandler(g);

            g.createAllGame(id2, r);
            r.start();

            game = new AdvancedGame(id2.length, g, r);
        }

        game.playAssistant(p.getId(), 1);
        assertEquals(Errors.NOT_CURRENT_PLAYER.getCode(), game.playCharacter(p.getId(), 9, null), "not current player");

        p = r.getCurrent();

        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.playCharacter(p.getId(), 9, null));

        game.playAssistant(p.getId(), 1);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 2);

        p = r.getCurrent();
        assertEquals(Errors.NO_ERROR.getCode(), game.playCharacter(p.getId(), 9, null));

        assertEquals(Errors.CHARACTER_YET_PLAYED.getCode(), game.playCharacter(p.getId(), 9, null), "already played a character");
    }

    @Test //test only for error
    void moveMotherNature() {

        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(1, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        AdvancedGame game = new AdvancedGame(id2.length, g, r);

        while (!g.getBoard().existsCharacter(9)){
            g = new GameInitializer(1, 2);
            r = new RoundHandler(g);

            g.createAllGame(id2, r);
            r.start();

            game = new AdvancedGame(id2.length, g, r);
        }

        AdvancedPlayer p = (AdvancedPlayer) r.getCurrent();
        game.playAssistant(p.getId(), 1);

        p = (AdvancedPlayer) r.getCurrent();
        game.playAssistant(p.getId(), 2);

        //action phase

        p = (AdvancedPlayer) r.getCurrent();
        p.playCharacter(g.getBoard().getCharacterById(9), null);

        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values())
                if (p.hasStudent(c)) {
                    game.moveStudent(p.getId(), c.getIndex(), -1);
                    break;
                }
        }

        int max = p.getActiveAssistant().getMaxMovement();

        assertEquals(Errors.MOVEMENTS_TOO_HIGH.getCode(), game.moveMotherNature(p.getId(), max + 3), "movement too high");
        assertEquals(Errors.NO_ERROR.getCode(), game.moveMotherNature(p.getId(), max + 2));
    }
}