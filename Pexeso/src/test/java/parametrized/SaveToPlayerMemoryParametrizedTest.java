package parametrized;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pexeso.players.ComputerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tomas on 24-Jan-16.
 */
@RunWith(Parameterized.class)
public class SaveToPlayerMemoryParametrizedTest {
    private int compareNumber;
    private int idNumber;
    private int expectedResult;
    private ComputerPlayer player;

    @Before
    public void initialize() {
        player = new ComputerPlayer(2);
        ArrayList<Integer> cards = new ArrayList<>();
        cards.add(0);
        player.getCorrectMoves().put(1, cards);
    }

    public SaveToPlayerMemoryParametrizedTest(int compareNumber, int idNumber, int expectedResult) {
        this.compareNumber = compareNumber;
        this.idNumber = idNumber;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection cardsMCDC() {
        return Arrays.asList(new Object[][] {
                {1,2,2},
                {2,3,1},
                {-1,2,0},
                {1,0,1}
        });
    }

    @Test
    public void testSaveToMapParam() {

        player.saveToMap(compareNumber, idNumber);
        int realResult;

        if (compareNumber != -1) {
            realResult = player.getCorrectMoves().get(compareNumber).size();
        } else {
            realResult = 0;
        }

        assertEquals(realResult, expectedResult);
    }
}
