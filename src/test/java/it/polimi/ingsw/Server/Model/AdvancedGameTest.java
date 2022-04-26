package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

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

        //move 3 students
        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values())
                if (p.hasStudent(c)) {
                    game.moveStudent(p.getId(), c.getIndex(), -1);
                    break;
                }
        }

        game.moveMotherNature(p.getId(), p.getActiveAssistant().getMaxMovement());

        assertEquals(g.getIslands().getMotherNature().getId(), p.getActiveAssistant().getMaxMovement());
        assertEquals(g.getIslands().getMotherNature().getTowerCount(), 0);

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
            System.out.println("Turn : " + i);
            completeRound(g, r, game);
        }

        Player p = r.getCurrent();
        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 1));


    }


    void completeRound(GameInitializer g, RoundHandler r, Game game){

        Player p = r.getCurrent();
        Random rand = new Random(System.currentTimeMillis());
        int k;
        do{
            k = rand.nextInt(1,11);
        }
        while(k%2 != 0);

        //play Assistant
        System.out.println(r.getPhase() + " " + r.getActionPhase());
        assertEquals(r.getPhase(), Phase.Planning);
        assertEquals(r.getActionPhase(), ActionPhase.NotActionPhase);

        assertEquals(Errors.NO_ERROR.getCode(),game.playAssistant(p.getId(), k));

        p = r.getCurrent();
        game.playAssistant(p.getId(), k-1);

        p = r.getCurrent();

        //move 2 students to the island k
        System.out.println(r.getPhase() + " " + r.getActionPhase());
        assertEquals(r.getPhase(), Phase.Action);
        assertEquals(r.getActionPhase(), ActionPhase.MoveStudent);

        for (int i = 0; i < 2; i++) {
            for (Color c : Color.values())
                if (p.hasStudent(c)) {
                    game.moveStudent(p.getId(), c.getIndex(), rand.nextInt(0, 12));
                    break;
                }
        }

        //move one student to the room
        for (Color c : Color.values())
            if (p.hasStudent(c)) {
                //if there's place for another student of that color in the room
                if (Errors.MAX_STUDENT_ROOM.getCode() != game.moveStudent(p.getId(), c.getIndex(), -1)){
                    break;
                }

            }

        //move mother nature
        System.out.println(r.getPhase() + " " + r.getActionPhase());
        assertEquals(r.getPhase(), Phase.Action);
        assertEquals(r.getActionPhase(), ActionPhase.MoveMotherNature);

        game.moveMotherNature(p.getId(), p.getActiveAssistant().getMaxMovement());

        //choose cloud and get students
        System.out.println(r.getPhase() + " " + r.getActionPhase());
        assertEquals(r.getPhase(), Phase.Action);
        assertEquals(r.getActionPhase(), ActionPhase.ChooseCloud);

        int i;
        for(i=0; i<g.getPlayersNumber(); i++){
            if (Arrays.stream(g.getBoard().getCloudById(i).getStudents()).sum() > 0){
                break;
            }
        }
        assertTrue(Arrays.stream(g.getBoard().getCloudById(i).getStudents()).sum() > 0);
        assertEquals(Errors.NO_ERROR.getCode(),game.chooseCloud(p.getId(), i));

        r.next();

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