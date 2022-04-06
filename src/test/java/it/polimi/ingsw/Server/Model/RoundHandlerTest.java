package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundHandlerTest {

    @Test
    void canPLayAssistant() {

        //done also in game play assistant test
        int[] ids = new int[4];
        ids[0] = 10;
        ids[1] = 6;
        ids[2] = 3;
        ids[3] = 721;

        GameInitializer g = new GameInitializer(0, ids.length);


        RoundHandler r = new RoundHandler(g);


        g.createAllGame(ids, r);
        r.start();

        Player p = r.getCurrent();
        //all null
        assertEquals(true, r.canPLayAssistant(p, Assistant.getAssistantByValue(1)), "Test 1 - all null");

        int i = 1;
        for (Player x: g){
            if (!p.equals(x)) {
                x.playAssistant(Assistant.getAssistantByValue(i));
                i++;
            }
        }

        //set all assistant except for p
        assertEquals(false, r.canPLayAssistant(p, Assistant.getAssistantByValue(1)), "Test 1 - all set");
        assertEquals(true, r.canPLayAssistant(p, Assistant.getAssistantByValue(4)), "Test 1 - all set");

        for (i = 4; i <= 10; i++)
            p.playAssistant(Assistant.getAssistantByValue(i));

        //now p has only 1 2 3 assistant

        assertEquals(true, r.canPLayAssistant(p, Assistant.getAssistantByValue(1)), "Test 1 - all set");
        assertEquals(true, r.canPLayAssistant(p, Assistant.getAssistantByValue(2)), "Test 1 - all set");
        assertEquals(true, r.canPLayAssistant(p, Assistant.getAssistantByValue(3)), "Test 1 - all set");

    }

    @Test
    void start() {

        int[] ids = new int[4];
        ids[0] = 10;
        ids[1] = 6;
        ids[2] = 3;
        ids[3] = 721;

        GameInitializer gInit = new GameInitializer(0, ids.length);


        RoundHandler roundHandler = new RoundHandler(gInit);

        assertNull(roundHandler.getCurrent(), "Test 1 - current null");

        gInit.createAllGame(ids, roundHandler);
        roundHandler.start();

        Player p = roundHandler.getCurrent();
        assertNotNull(p, "Test 2 - current not null");
        assertEquals(true, gInit.existsPlayer(p.getId()), "Test 3 - player exists");
        p.playAssistant(Assistant.getAssistantByValue(4));
        roundHandler.next();

        p = roundHandler.getCurrent();
        assertNotNull(p, "Test 2 - current not null");
        assertEquals(true, gInit.existsPlayer(p.getId()), "Test 3 - player exists");
        p.playAssistant(Assistant.getAssistantByValue(3));
        roundHandler.next();

        p = roundHandler.getCurrent();
        assertNotNull(p, "Test 2 - current not null");
        assertEquals(true, gInit.existsPlayer(p.getId()), "Test 3 - player exists");
        p.playAssistant(Assistant.getAssistantByValue(2));
        roundHandler.next();

        p = roundHandler.getCurrent();
        assertNotNull(p, "Test 2 - current not null");
        assertEquals(true, gInit.existsPlayer(p.getId()), "Test 3 - player exists");
        p.playAssistant(Assistant.getAssistantByValue(1));
        roundHandler.next();

        assertEquals(Phase.Action, roundHandler.getPhase(), "Test 4 - Right Phase");
        assertEquals(p, roundHandler.getCurrent(), "Test 5 - Right sorting");
    }

    @Test
    void next() {

        //only 2 for much faster cycle throw phase
        int[] ids = new int[2];
        ids[0] = 10;
        ids[1] = 6;

        GameInitializer g = new GameInitializer(0, ids.length);

        RoundHandler r = new RoundHandler(g);

        g.createAllGame(ids, r);
        r.start();

        assertEquals(Phase.Planning, r.getPhase(), "Test 1 - Planning");
        assertEquals(ActionPhase.NotActionPhase, r.getActionPhase(), "Test 1 - Planning");

        Player p2 = r.getCurrent();
        p2.playAssistant(Assistant.getAssistantByValue(2));

        r.next();

        assertEquals(Phase.Planning, r.getPhase(), "Test 1 - Planning");
        assertEquals(ActionPhase.NotActionPhase, r.getActionPhase(), "Test 1 - Planning");

        Player p1 = r.getCurrent();
        p1.playAssistant(Assistant.getAssistantByValue(1));

        r.next();

        assertEquals(p1, r.getCurrent(), "Test 2 - Ordered Correctly");

        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveStudent, r.getActionPhase(), "Test 3 - Action");

        r.next(); //p1 moved a student

        assertEquals(p1, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveStudent, r.getActionPhase(), "Test 3 - Action");
        assertEquals(1, r.getStudentMovedInThisTurn(), "Test 4 - right movement");

        r.next(); //p1 has moved 2 student

        assertEquals(p1, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveStudent, r.getActionPhase(), "Test 3 - Action");
        assertEquals(2, r.getStudentMovedInThisTurn(), "Test 4 - right movement");

        r.next(); //p1 has moved 3 student

        assertEquals(p1, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveMotherNature, r.getActionPhase(), "Test 3 - Action");
        assertEquals(0, r.getStudentMovedInThisTurn(), "Test 4 - right movement");

        r.next(); //p1 has moved mother nature

        assertEquals(p1, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.ChooseCloud, r.getActionPhase(), "Test 3 - Action");

        r.next(); //p1 has chosen the cloud

        assertEquals(p2, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveStudent, r.getActionPhase(), "Test 3 - Action");

        r.next(); //p2 has moved 1 student

        assertEquals(p2, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveStudent, r.getActionPhase(), "Test 3 - Action");
        assertEquals(1, r.getStudentMovedInThisTurn(), "Test 4 - right movement");

        r.next(); //p2 has moved 2 student

        assertEquals(p2, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveStudent, r.getActionPhase(), "Test 3 - Action");
        assertEquals(2, r.getStudentMovedInThisTurn(), "Test 4 - right movement");

        r.next(); //p2 has moved 3 student

        assertEquals(p2, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.MoveMotherNature, r.getActionPhase(), "Test 3 - Action");
        assertEquals(0, r.getStudentMovedInThisTurn(), "Test 4 - right movement");

        r.next(); //p2 has moved mother nature

        assertEquals(p2, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Action, r.getPhase(), "Test 3 - Action");
        assertEquals(ActionPhase.ChooseCloud, r.getActionPhase(), "Test 3 - Action");

        r.next(); //p2 has chosen the cloud

        assertEquals(p1, r.getCurrent(), "Test 2 - Ordered Correctly");
        assertEquals(Phase.Planning, r.getPhase(), "Test 4 - Planning");
        assertEquals(ActionPhase.NotActionPhase, r.getActionPhase(), "Test 4 - Planning");
    }
}