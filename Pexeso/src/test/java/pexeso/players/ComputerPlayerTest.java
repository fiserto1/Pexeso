package pexeso.players;

import org.junit.Before;
import org.junit.Test;
import pexeso.games.OneMove;

import javax.swing.*;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class ComputerPlayerTest {

    ComputerPlayer computerPlayer;
    OneMove lastMove;
    ArrayList<OneMove> oppMoves;
    @Before
    public void setUp() {
        computerPlayer = new ComputerPlayer("Comp", new ImageIcon(""), 2);
        lastMove = new OneMove(1,2);
        oppMoves = new ArrayList<>();
        oppMoves.add(new OneMove(2,4));
        oppMoves.add(new OneMove(5,9));
    }


    @Test
    public void testDeleteOldMemory() throws Exception {
        //ASSERT
        computerPlayer.deleteOldMemory();

        //ASSERT
        assertTrue(computerPlayer.getMemory().isEmpty());

        //ARRANGE
        computerPlayer.saveMove(new OneMove(2,4));
        computerPlayer.refreshMemory();
        computerPlayer.refreshMemory();
        computerPlayer.refreshMemory();
        computerPlayer.saveMove(new OneMove(6,8));
        computerPlayer.refreshMemory();

        //ACT
        computerPlayer.deleteOldMemory();

        //ASSERT
        assertEquals(1, computerPlayer.getMemory().size());
    }


    @Test
    public void testSaveToMemory() throws Exception {
        int[] expectedValue = new int[]{1, 2, 2};

        computerPlayer.saveToMemory(2, 2);

        assertArrayEquals(expectedValue, computerPlayer.getMemory().get(0));
    }

    @Test
    public void testSaveToMap() throws Exception {
        ArrayList<Integer> expectedValue = new ArrayList<>();
        expectedValue.add(2);

        computerPlayer.saveToMap(2, 2);

        assertEquals(expectedValue ,computerPlayer.getCorrectMoves().get(2));


        expectedValue.add(9);

        computerPlayer.saveToMap(2, 9);

        assertEquals(expectedValue ,computerPlayer.getCorrectMoves().get(2));


        computerPlayer.saveToMap(-1, 9);


        assertEquals(expectedValue ,computerPlayer.getCorrectMoves().get(2));


    }
}