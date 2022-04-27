package it.polimi.ingsw.Server.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//todo test black box
class AdvancedGameTest extends GameTest {

    @Test
    void playAssistantBlackBox() {

    }

    @Test
    void moveStudentBlackBox() {
    }

    @Test
    void moveMotherNatureBlackBox() {
        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(0, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        Game game = new Game(id2.length, g, r);

        Player p = r.getCurrent();
        game.playAssistant(p.getId(), 1);

        p = r.getCurrent();
        game.playAssistant(p.getId(), 2);

        p = r.getCurrent();




        Color[] colorPicked = new Color[3];
        Random rand = new Random(System.currentTimeMillis());
        //move 3 students
        int i = 0;
        while (i < 3) {
            Color c = Color.getColorById(rand.nextInt(Color.getNumberOfColors())); // get a random Color
            if (p.hasStudent(c)) {
                game.moveStudent(p.getId(), c.getIndex(), -1);
                colorPicked[i] = c;
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
    void chooseCloudBlackBox() {
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
    //todo checkwin (uncomment "break")
    //todo fix always 0 towers
    //todo fix sometimes loop (i believe because of checkwin)
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
        
        for(int i=0; i<6; i++){
            System.out.println("-------------------------- Turn : " + i + "---------------------------------");

            completeRound(g, r, game);
            /*
            if(g.getWinningPlayerId() != -1){
                System.out.println("---------------------- Winner : player # " + g.getWinningPlayerId() + "-----------------------------");
                //break;
            }
             */
            //todo remove this when check win fixed
            if(g.getIslands().getIslandsNumber() == 3){
                System.out.println("---------------------- Winner : player # " + g.getWinningPlayerId() + "-----------------------------");
                break;
            }
        }


    }


    void completeRound(GameInitializer g, RoundHandler r, Game game){



        //All the players play an assistant

        System.out.println(r.getPhase() + " " + r.getActionPhase());
        assertEquals(r.getPhase(), Phase.Planning);
        assertEquals(r.getActionPhase(), ActionPhase.NotActionPhase);

        Player p = null;
        Random rand = new Random(System.currentTimeMillis());
        int k;
        List<Integer> assistantsIds = new ArrayList<>(g.getPlayersNumber());
        for (int i=0; i<g.getPlayersNumber(); i++){

            for (Assistant a : p = r.getCurrent()){
                if (r.canPLayAssistant(p, a)){
                    assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), a.getValue()));
                    System.out.println("#" + p.getId() + " -> " + "Play Assistant With Max Movements : " + a.getMaxMovement());
                    break;
                }
            }

        }

        //action phase for every player
        for(int t=0; t<g.getPlayersNumber(); t++){

            System.out.println("############################## ISLANDS ##############################");
            //printing islands
            String towerColor = null;
            for(Island is : g.getIslands()){
                if (is.getTowerColor() == -1) {
                    towerColor = " ";
                }
                else if (is.getTowerColor() == 0){
                    towerColor = " Black";
                }
                else if (is.getTowerColor() == 1){
                    towerColor = " White";
                }
                else if (is.getTowerColor() == 2){
                    towerColor = " Grey";
                }
                System.out.println("{ID: " + is.getId() + " , STUDENTS: " + Arrays.toString(is.getStudents()) + " , TOWERS: " + is.getTowerCount()  + towerColor + " , BAN CARD: " + is.getBanCard() + "}");

            }
            System.out.println("#####################################################################");
            System.out.println("###### PROFESSORS ######");
            System.out.println(Arrays.toString(g.getProfessors().getProfessorsCopy()));
            System.out.println("########################");

            System.out.println("###### ENTRANCES ######");
            for (Player pl : g){
                System.out.println("#" + pl.getId() + " -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()));
            }
            System.out.println("########################");

            p = r.getCurrent();
            System.out.println("Current player : " + p.getId());

            System.out.println("#" + p.getId() + " -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()) + " -- Initial");

            //move 2 students to the island k
            System.out.println("#" + p.getId() + " -> " + r.getPhase() + " " + r.getActionPhase());
            assertEquals(r.getPhase(), Phase.Action);
            assertEquals(r.getActionPhase(), ActionPhase.MoveStudent);
            int errorCode;
            for (int i = 0; i < 2; i++) {
                for (Color c : Color.values())
                    if (p.hasStudent(c)) {
                        do{
                            k = rand.nextInt(0, 12);
                            System.out.println("#" + p.getId() + " -> " + "Moving student to " + k);
                            errorCode = game.moveStudent(p.getId(), c.getIndex(), k);
                            System.out.println("#" + p.getId() + " -> " + Errors.getErrorsByCode(errorCode).getDescription());
                        } while (errorCode != 0);
                        System.out.println("#" + p.getId() + " -> " + "Moved!");
                        assertEquals(Errors.NO_ERROR.getCode(), errorCode);
                        break;
                    }
            }

            //move one student to the room, or, if not possible, to an island
            boolean movedToRoom = false;
            for (Color c : Color.values()) {
                if (p.hasStudent(c)) {
                    //if there's place for another student of that color in the room
                    System.out.println("#" + p.getId() + " -> " + "Moving student to -1");
                    errorCode = game.moveStudent(p.getId(), c.getIndex(), -1);
                    System.out.println("#" + p.getId() + " -> " + Errors.getErrorsByCode(errorCode).getDescription());
                    if (Errors.NO_ERROR.getCode() == errorCode) {
                        System.out.println("#" + p.getId() + " -> " + "Moved!");
                        assertEquals(Errors.NO_ERROR.getCode(), errorCode);
                        movedToRoom = true;
                        break;
                    }

                }
            }
            if(!movedToRoom){
                for (Color c : Color.values()){
                    if (p.hasStudent(c)){
                        do{
                            k = rand.nextInt(0, 12);
                            System.out.println("#" + p.getId() + " -> " + "Moving student to " + k);
                            errorCode = game.moveStudent(p.getId(), c.getIndex(), k);
                            System.out.println(Errors.getErrorsByCode(errorCode).getDescription());
                        } while (errorCode != 0);
                        System.out.println("#" + p.getId() + " -> " + "Moved!");
                        assertEquals(Errors.NO_ERROR.getCode(), errorCode);
                        break;
                    }
                }
            }

            System.out.println("#" + p.getId() + " -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()) + " -- After Moving");

            //move mother nature
            System.out.println("#" + p.getId() + " -> " + r.getPhase() + " " + r.getActionPhase());
            assertEquals(r.getPhase(), Phase.Action);
            assertEquals(r.getActionPhase(), ActionPhase.MoveMotherNature);

            assertEquals(Errors.NO_ERROR.getCode(), game.moveMotherNature(p.getId(), p.getActiveAssistant().getMaxMovement()));

            //todo remove this when check win fixed
            if(g.getIslands().getIslandsNumber() == 3){
                System.out.println("---------------------- Winner : player # " + g.getWinningPlayerId() + "-----------------------------");
                break;
            }

            //choose cloud and get students
            System.out.println("#" + p.getId() + " -> " + r.getPhase() + " " + r.getActionPhase());
            assertEquals(r.getPhase(), Phase.Action);
            assertEquals(r.getActionPhase(), ActionPhase.ChooseCloud);


            int i;
            boolean found = false;
            int[] students = new int[Color.getNumberOfColors()];
            for(i=0; i<g.getPlayersNumber(); i++) {
                students = g.getBoard().getCloudById(i).getCopyOfDrawnStudents().clone();
                for (int student : students) {
                    if (student > 0) {
                        System.out.println("#" + p.getId() + " -> " + Arrays.toString(students) + " -- Chosen Cloud");
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found) System.out.println("#" + p.getId() + " -> " + "All the clouds are empty");
            p = r.getCurrent();
            assertEquals(Errors.NO_ERROR.getCode(),game.chooseCloud(p.getId(), i));

            System.out.println("#" + p.getId() + " -> " + Arrays.toString(p.getSchool().getCopyOfEntrance()) + " -- After Choosing Cloud");

            g.checkWin();

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