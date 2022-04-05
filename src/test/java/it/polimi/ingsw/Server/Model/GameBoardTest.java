package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private static GameBoard getGameBoard(int count, int gameMode){
        int[] ids = new int[2];
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
            ids[2] = 30;
            ids[3] = 71;
        }
        else return null;
        GameInitializer gInit = new GameInitializer(ids, gameMode);
        GameBoard board = gInit.getGameBoard();
        return board;
    }

    //@Test
    public void NewRoundTest(int count, int gameMode){
        GameBoard board = getGameBoard(count, gameMode);
        board.populateClouds();
        ArrayList<Cloud> clouds = board.getClouds();
        for (Cloud c : clouds){
            System.out.println(Arrays.toString(c.getStudents()));
            if (count == 3) assertEquals(Arrays.stream(c.getStudents()).sum(), 4);
            else assertEquals(Arrays.stream(c.getStudents()).sum(), 3);
        }
    }

    public void AddStudentToIslandTest(int count, int gameMode){
        //this method only invoke a method in one other class so the tests will be done in that class (islands)


        /*GameBoard board = getGameBoard(count, gameMode);
        board.populateClouds();
        Player player = board.getgInit().iterator().next();
        Color color = null;
        for (int i=0; i<Color.getNumberOfColors(); i++){
            if (player.hasStudent(Color.getColorById(i))){
                color = Color.getColorById(i);
                player.moveStudent(new Movement(color, board.getIslands().getIslandFromId(3)));

                break;
            }
        }
        assertTrue(board.getIslands().getIslandFromId(3).getStudents()[color.getIndex()] >= 1);
        */
    }


    @Test
    public void Tests(){
        for(int count=2; count <= 4; count ++){
            NewRoundTest(count, 0);
            NewRoundTest(count, 1);
            AddStudentToIslandTest(count, 0);
            AddStudentToIslandTest(count, 1);
        }
    }

}
