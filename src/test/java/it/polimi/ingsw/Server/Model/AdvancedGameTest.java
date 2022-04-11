package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    void chooseCloudBlackBox() {
    }

    @Test
    void playCharacterBlackBox() {

    }

    @Test //black box
    void completeGame(){
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
    void testMoveMotherNature() {

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