package parametrized;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pexeso.players.HumanPlayer;
import util.CSVFileReaderUtil;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class HumanPlayerParametrizedTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final String name;
    private final int playerNumber;
    private final boolean isExceptionExpected;

    @Parameterized.Parameters
    public static Collection<String[]> data() throws Exception {
        return CSVFileReaderUtil.readCSVfileToCollection("computer_player_data.csv", ";");
    }


    public HumanPlayerParametrizedTest(String name, String playerNumber, String isExceptionExpected) {
        this.name = name;
        this.playerNumber = Integer.parseInt(playerNumber.trim());
        this.isExceptionExpected =  Boolean.parseBoolean(isExceptionExpected.trim());
    }

    @Test
    public void testCreateComputerPlayerParametrized() {
        if (isExceptionExpected) {
            exception.expect(IllegalArgumentException.class);
        }

        HumanPlayer computerPlayer = new HumanPlayer(name, null, playerNumber);
        assertNotNull(computerPlayer);
        assertEquals(name, computerPlayer.getName());
        assertEquals(playerNumber, computerPlayer.getPlayerNumber());
    }
}
