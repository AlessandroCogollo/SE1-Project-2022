package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private static GameBoard getGameBoard(){
        int[] ids = new int[2];
        ids[0] = 4;
        ids[1] = 24;
        GameInitializer gInit = new GameInitializer(ids, 0);
        GameBoard board = new GameBoard(gInit);
        return board;
    }
    @Test
    public void shouldAnswerWithTrue()
    {
        GameBoard board = getGameBoard();
        board.AddStudentToIsland(Color.Blue, 3);
        int[] students = new int[Color.getNumberOfColors()];
        for (int i=0; i< students.length; i++){
            students[i] = 0;
        }
        students[Color.Blue.getIndex()] = 1;
        assertEquals(board.getIslands().getIslandFromId(3).getStudents(), students);
    }

}
