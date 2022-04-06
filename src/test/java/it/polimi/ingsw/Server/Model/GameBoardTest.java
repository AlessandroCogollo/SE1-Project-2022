package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private static GameBoard getGameBoard(int count, int gameMode){
        int[] ids;
        if (count == 2){
            ids = new int[2];
            ids[0] = 4;
            ids[1] = 24;
        }
        else if (count == 3){
            ids = new int[3];
            ids[0] = 4;
            ids[1] = 24;
            ids[2] = 30;
        }
        else if (count == 4){
            ids = new int[4];
            ids[0] = 4;
            ids[1] = 24;
            ids[2] = 31;
            ids[3] = 71;
        }
        else return null;
        GameInitializer gInit = new GameInitializer(gameMode, ids.length);
        gInit.createAllGame(ids, null);

        return gInit.getBoard();
    }

    //@Test
    public void NewRoundTest(int count, int gameMode){
        GameBoard board = getGameBoard(count, gameMode);
        board.populateClouds();
        Collection<Cloud> clouds = new ArrayList<>();
        for (int i = 0; i < count; i++)
            clouds.add(board.getCloudById(i));
        for (Cloud c : clouds){
            int[] temp = c.getStudents();
            if (count == 3)
                assertEquals(4, Arrays.stream(temp).sum());
            else
                assertEquals(3, Arrays.stream(temp).sum());
        }
    }

    @Test
    public void AddStudentToIslandTest(){
        //done in island test
        assertTrue(true);
    }


    @Test
    public void Tests(){
        for(int count = 2; count <= 4; count ++){
            NewRoundTest(count, 0);
            NewRoundTest(count, 1);
        }
    }

}
