package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test //test for error
    void playAssistant() {

        int[] id4 = new int[4];
        id4[0] = 1;
        id4[1] = 2;
        id4[2] = 3;
        id4[3] = 4;

        GameInitializer g = new GameInitializer(0, 4);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id4, r);
        r.start();

        Game game = new Game(id4.length, g, r);

        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.playAssistant(1543, 1), "player not exist");

        Player p = r.getCurrent();

        assertEquals(Errors.NOT_VALID_ASSISTANT.getCode(), game.playAssistant(p.getId(), 20), "assistant not exist");

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 1), "no error");

        assertEquals(Errors.NOT_CURRENT_PLAYER.getCode(), game.playAssistant(p.getId(), 2), "not current");

        p = r.getCurrent();

        assertEquals(Errors.ASSISTANT_ALREADY_PLAYED.getCode(), game.playAssistant(p.getId(), 1), "assistant yet played");

        p.playAssistant(Assistant.getAssistantByValue(2));

        assertEquals(Errors.NO_SUCH_ASSISTANT.getCode(), game.playAssistant(p.getId(), 2), "no error");

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 5), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 3), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 4), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.playAssistant(p.getId(), 5), "Test 3 - not right phase");

        //reset

        g = new GameInitializer(0, 4);
        r = new RoundHandler(g);

        g.createAllGame(id4, r);
        r.start();

        game = new Game(id4.length, g, r);

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 1), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 2), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 3), "Test 1 - no error");

        p = r.getCurrent();

        //not valid during normal operation

        p.playAssistant(Assistant.getAssistantByValue(4));
        p.playAssistant(Assistant.getAssistantByValue(5));
        p.playAssistant(Assistant.getAssistantByValue(6));
        p.playAssistant(Assistant.getAssistantByValue(7));
        p.playAssistant(Assistant.getAssistantByValue(8));
        p.playAssistant(Assistant.getAssistantByValue(9));

        //now the last player has only 1 2 3 10 assistant

        assertEquals(Errors.ASSISTANT_ALREADY_PLAYED.getCode(), game.playAssistant(p.getId(), 1), "Test 4 - error not all equal assistant");

        p.playAssistant(Assistant.getAssistantByValue(10));

        //now the last player has only 1 2 3 assistant

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 1), "Test 5 - has all equal assistant");
    }

    @Test //test for error
    void moveStudent() {

        int[] id4 = new int[4];
        id4[0] = 1;
        id4[1] = 2;
        id4[2] = 3;
        id4[3] = 4;

        GameInitializer g = new GameInitializer(0, 4);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id4, r);
        r.start();

        Game game = new Game(id4.length, g, r);

        Player p = r.getCurrent();
        game.playAssistant(p.getId(), 1);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 2);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 3);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 4);

        //now move student phase
        p = r.getCurrent();
        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.moveStudent(1543, Color.Red.getIndex(), -1), "player not exists");
        assertEquals(Errors.NOT_VALID_COLOR.getCode(), game.moveStudent(p.getId(), -3, -1), "color not exists");
        assertEquals(Errors.NOT_VALID_DESTINATION.getCode(), game.moveStudent(p.getId(), Color.Red.getIndex(), 20), "destination not exists");

        for (Color c: Color.values())
            if (p.hasStudent(c)) {
                assertEquals(Errors.NO_ERROR.getCode(), game.moveStudent(p.getId(), c.getIndex(), -1));
                break;
            }

        for (Color c: Color.values())
            if (p.hasStudent(c)) {
                assertEquals(Errors.NO_ERROR.getCode(), game.moveStudent(p.getId(), c.getIndex(), -1));
                break;
            }

        for (Color c: Color.values())
            if (p.hasStudent(c)) {
                assertEquals(Errors.NO_ERROR.getCode(), game.moveStudent(p.getId(), c.getIndex(), -1));
                break;
            }

        //now the player has moved all the students that he can in this turn

        for (Color c: Color.values())
            if (p.hasStudent(c)) {
                assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.moveStudent(p.getId(), c.getIndex(), -1), "not current player");
                break;
            }

        //resetting

        int[] id3 = new int[3];
        id3[0] = 1;
        id3[1] = 2;
        id3[2] = 3;

        g = new GameInitializer(0, 3);
        r = new RoundHandler(g);

        g.createAllGame(id3, r);
        r.start();

        game = new Game(id3.length, g, r);

        p = r.getCurrent();
        game.playAssistant(p.getId(), 1);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 2);
        p = r.getCurrent();
        game.playAssistant(p.getId(), 3);

        //now move student phase
        p = r.getCurrent();

        for (int i = 0; i < 3; i++) { //only one more student to move after this for
            for (Color c : Color.values())
                if (p.hasStudent(c)) {
                    assertEquals(i, r.getStudentMovedInThisTurn(), "right students moved");
                    game.moveStudent(p.getId(), c.getIndex(), -1);
                    break;
                }
        }

        for (Color c: Color.values())
            while (p.hasStudent(c)) {
                p.moveStudent(c, -1);
            }

        //now the students doesn't have any student in entrance

        for (Color c: Color.values())
            assertEquals(Errors.NO_STUDENT.getCode(), game.moveStudent(p.getId(), c.getIndex(), -1), "no more students");
    }

    @Test //test for error
    void moveMotherNature() {

        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(0, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        Game game = new Game(id2.length, g, r);

        Player p = r.getCurrent();
        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.moveMotherNature(p.getId(), 1), "not right phase");
        game.playAssistant(p.getId(), 1);

        p = r.getCurrent();
        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.moveMotherNature(p.getId(), 1), "not right phase");
        game.playAssistant(p.getId(), 2);

        p = r.getCurrent();

        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values())
                if (p.hasStudent(c)) {
                    game.moveStudent(p.getId(), c.getIndex(), -1);
                    break;
                }
        }

        //now move mother nature phase
        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.moveMotherNature(1543, 1), "player not exists");
        assertEquals(Errors.MOVEMENT_NOT_VALID.getCode(), game.moveMotherNature(p.getId(), 0), "position too low");


        Player p2;
        if (p.getId() == 4)
            p2 = g.getPlayerById(24);
        else
            p2 = g.getPlayerById(4);

        assertEquals(Errors.NOT_CURRENT_PLAYER.getCode(), game.moveMotherNature(p2.getId(), 1), "not current player");
        assertEquals(Errors.MOVEMENTS_TOO_HIGH.getCode(), game.moveMotherNature(p.getId(), p.getActiveAssistant().getMaxMovement() + 1), "not current player");
        assertEquals(Errors.NO_ERROR.getCode(), game.moveMotherNature(p.getId(), 1));
        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.moveMotherNature(p.getId(), 1), "not right phase");
    }

    @Test //test for error
    void chooseCloud() {

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

        for (int i = 0; i < 3; i++) {
            for (Color c : Color.values())
                if (p.hasStudent(c)) {
                    game.moveStudent(p.getId(), c.getIndex(), -1);
                    break;
                }
        }

        game.moveMotherNature(p.getId(), 1);

        //now choose cloud phase
        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.chooseCloud(1543, 1), "player not exists");
        assertEquals(Errors.NO_SUCH_CLOUD.getCode(), game.chooseCloud(p.getId(), 2), "cloud doesn't exists");

        Player p2;
        if (p.getId() == 4)
            p2 = g.getPlayerById(24);
        else
            p2 = g.getPlayerById(4);

        assertEquals(Errors.NOT_CURRENT_PLAYER.getCode(), game.chooseCloud(p2.getId(), 1), "not current player");
        assertEquals(Errors.NO_ERROR.getCode(), game.chooseCloud(p.getId(), 1));
        assertEquals(Errors.NOT_CURRENT_PLAYER.getCode(), game.chooseCloud(p.getId(), 1), "not current player");
        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.chooseCloud(p2.getId(), 1), "not right phase");
    }

    @Test
    void getGameModel() {

        //testing only game parameter other test will be done in gameInit and roundHandler

        int[] id1 = new int[2];
        id1[0] = 4;
        id1[1] = 24;
        Game test1 = Game.getGameModel(id1, 0);
        assertFalse(test1 instanceof AdvancedGame, "test 1 - 2 player easy mode");
        assertNotNull(test1, "test 1 - 2 player easy mode");


        int[] id2 = new int[3];
        id2[0] = 4;
        id2[1] = 24;
        id2[2] = 10;
        Game test2 = Game.getGameModel(id2, 0);
        assertFalse(test2 instanceof AdvancedGame, "test 2 - 3 player easy mode");
        assertNotNull(test2, "test 2 - 3 player easy mode");

        int[] id3 = new int[4];
        id3[0] = 1;
        id3[1] = 2;
        id3[2] = 3;
        id3[3] = 4;
        Game test3 = Game.getGameModel(id3, 1);
        assertTrue(test3 instanceof AdvancedGame, "test 3 - 4 player advanced mode");

        int[] id4 = new int[4];
        id4[0] = 10;
        id4[1] = 6;
        id4[2] = 3;
        id4[3] = 721;
        Game test4 = Game.getGameModel(id4, 1);
        assertTrue(test4 instanceof AdvancedGame, "test 4 - 4 player advanced mode, 'casual' id");

        int[] id5 = new int[4];
        id5[0] = 10;
        id5[1] = 6;
        id5[2] = 6;
        id5[3] = 721;
        Game test5 = Game.getGameModel(id5, 1);
        assertNull(test5, "test 5 - 4 player advanced mode, null for repetitive ids");

        int[] id6 = new int[4];
        id6[0] = 10;
        id6[1] = 6;
        id6[2] = 4;
        id6[3] = 721;
        Game test6 = Game.getGameModel(id6, 1);
        assertNull(test6, "test 6 - 4 player advanced mode, null for odds or evens parameters");

        int[] id7 = new int[1];
        id7[0] = 10;
        Game test7 = Game.getGameModel(id7, 1);
        assertNull(test7, "test 7 - 1 player advanced mode, null for player number");

        int[] id8 = new int[2];
        id8[0] = 4;
        id8[1] = 24;
        Game test8 = Game.getGameModel(id8, 3);
        assertNull(test8, "test 8 - 2 player no mode, null for game mode number");

        int[] id9 = new int[2];
        id9[0] = -1;
        id9[1] = 24;
        Game test9 = Game.getGameModel(id9, 1);
        assertNull(test9, "test 9 - 2 player normal, null for negative id");
    }
}