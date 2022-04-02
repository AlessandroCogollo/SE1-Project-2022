package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private static GameBoard getGameBoard(){
        int[] ids = new int[2];
        ids[0] = 4;
        ids[1] = 24;
        GameInitializer gInit = new GameInitializer(ids, 0);
        GameBoard board = gInit.getGameBoard();
        return board;
    }
    @Test
    public void addStudentTest()
    {
        Color color = Color.Yellow;
        GameBoard board = getGameBoard();
        board.getgInit().getPlayers().get(0).moveStudent(new Movement(color, null));
        board.AddStudentToIsland(color, 3);
        int[] students = new int[Color.getNumberOfColors()];
        for (int i=0; i< students.length; i++){
            students[i] = 0;
        }
        students[color.getIndex()] = 1;
        for(int i=0; i<Color.getNumberOfColors(); i++){
            assertEquals(board.getIslands().getIslandFromId(3).getStudents()[i], students[i]);
        }

    }

}
