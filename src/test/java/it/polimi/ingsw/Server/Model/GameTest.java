package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void playAssistantBlackBox() {
        Game g = setGame(4, 0);
    }

    @Test
    void moveStudentsBlackBox() {
        Game g = setGame(4, 0);
    }

    @Test
    void moveMotherNatureBlackBox() {
    }

    @Test
    void chooseCloudBlackBox() {
        Game g = setGame(4, 0);
    }

    @Test //black box
    void completeGame(){
        Game g = setGame(4, 0);

    }


    protected static Game setGame (int numOfPLayers, int gameMode){
        switch (numOfPLayers){
            case 2:
                int[] id2 = new int[2];
                id2[0] = 4;
                id2[1] = 24;
                return Game.getGameModel(id2, gameMode);
            case 3:
                int[] id3 = new int[3];
                id3[0] = 4;
                id3[1] = 24;
                id3[2] = 10;
                return Game.getGameModel(id3, gameMode);
            case 4:
                int[] id4 = new int[4];
                id4[0] = 1;
                id4[1] = 2;
                id4[2] = 3;
                id4[3] = 4;
                return Game.getGameModel(id4, gameMode);
        }
        return null;
    }

    @Test //test for error
    void playAssistant() {

        int[] id4 = new int[4];
        id4[0] = 1;
        id4[1] = 2;
        id4[2] = 3;
        id4[3] = 4;

        GameInitializer g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);

        RoundHandler r = new RoundHandler(g);
        r.start();

        Game game = new Game(id4.length, g, r);

        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.playAssistant(1543, 1), "Test 0 - not exist");


        Player p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 1), "Test 1 - no error");

        assertEquals(Errors.NOT_CURRENT_PLAYER.getCode(), game.playAssistant(p.getId(), 2), "Test 2 - no current");

        p = r.getCurrent();

        assertEquals(Errors.ASSISTANT_ALREADY_PLAYED.getCode(), game.playAssistant(p.getId(), 1), "Test 2 - assistant played");

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 2), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 3), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NO_ERROR.getCode(), game.playAssistant(p.getId(), 4), "Test 1 - no error");

        p = r.getCurrent();

        assertEquals(Errors.NOT_RIGHT_PHASE.getCode(), game.playAssistant(p.getId(), 5), "Test 3 - not right phase");

        //reset

        g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);

        r = new RoundHandler(g);
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
    void moveStudents() {

        int[] id4 = new int[4];
        id4[0] = 1;
        id4[1] = 2;
        id4[2] = 3;
        id4[3] = 4;

        GameInitializer g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);

        RoundHandler r = new RoundHandler(g);
        r.start();

        Game game = new Game(id4.length, g, r);

        Player p = r.getCurrent();

        assertEquals(Errors.PLAYER_NOT_EXIST.getCode(), game.moveStudents(1543, Color.Red.getIndex(), -1), "Test 0 - not exist");

        //todo other check, need Bag class


        //todo Island check, need GameBoard
    }

    @Test //test for error
    void moveMotherNature() {

        //todo, need GameBoard class
    }

    @Test //test for error
    void chooseCloud() {

        //todo, need Cloud class
    }



    @Test
    void getGameModel() {

        //testing only game parameter other test will be done in gameInit and roundHandler

        int[] id1 = new int[2];
        id1[0] = 4;
        id1[1] = 24;
        Game test1 = Game.getGameModel(id1, 0);
        assertEquals(false, test1 instanceof AdvancedGame, "test 1 - 2 player easy mode");
        assertEquals(true, test1 instanceof Game, "test 1 - 2 player easy mode");


        int[] id2 = new int[3];
        id2[0] = 4;
        id2[1] = 24;
        id2[2] = 10;
        Game test2 = Game.getGameModel(id2, 0);
        assertEquals(false, test2 instanceof AdvancedGame, "test 2 - 3 player easy mode");
        assertEquals(true, test2 instanceof Game, "test 2 - 3 player easy mode");

        int[] id3 = new int[4];
        id3[0] = 1;
        id3[1] = 2;
        id3[2] = 3;
        id3[3] = 4;
        Game test3 = Game.getGameModel(id3, 1);
        assertEquals(true, test3 instanceof AdvancedGame, "test 3 - 4 player advanced mode");

        int[] id4 = new int[4];
        id4[0] = 10;
        id4[1] = 6;
        id4[2] = 3;
        id4[3] = 721;
        Game test4 = Game.getGameModel(id4, 1);
        assertEquals(true, test4 instanceof AdvancedGame, "test 4 - 4 player advanced mode, 'casual' id");

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
        Game test8 = Game.getGameModel(id1, 3);
        assertNull(test8, "test 8 - 2 player no mode, null for game mode number");
    }
}