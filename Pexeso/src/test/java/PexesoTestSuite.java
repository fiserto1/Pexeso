import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pexeso.cards.CardButtonTest;
import pexeso.cards.CardTest;
import pexeso.cards.DeckOfCardsTest;
import pexeso.games.GameTest;
import pexeso.players.ComputerPlayerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CardTest.class,
        CardButtonTest.class,
        DeckOfCardsTest.class,
        GameTest.class,
        ComputerPlayerTest.class,
        ParametrizedSaveToMapTest.class,
        TestGameProcess.class
})
/**
 * Created by Tomas on 23-Jan-16.
 */
public class PexesoTestSuite {
}
