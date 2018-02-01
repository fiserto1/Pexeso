import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pexeso.cards.CardTest;
import pexeso.cards.DeckOfCardsTest;
import pexeso.games.GameTest;
import pexeso.gui.CardALTest;
import pexeso.gui.CardButtonTest;
import pexeso.players.AbstractPlayerTest;
import pexeso.players.ComputerPlayerTest;
import pexeso.players.HumanPlayerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CardTest.class,
        DeckOfCardsTest.class,
        GameTest.class,
        CardALTest.class,
        CardButtonTest.class,
        AbstractPlayerTest.class,
        ComputerPlayerTest.class,
        HumanPlayerTest.class,
})
public class JUnitTestSuite {
}

