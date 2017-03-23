import org.junit.Before;
import org.junit.Test;
import pexeso.OneMove;
import pexeso.Settings;
import pexeso.cards.DeckOfCards;
import pexeso.games.Game;
import pexeso.players.ComputerPlayer;
import pexeso.players.HumanPlayer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class TestGameProcess {

    Settings settings;
    DeckOfCards deck;
    HumanPlayer player1;
    HumanPlayer player2;
    ComputerPlayer cpu2;
    ComputerPlayer cpu1;


    @Before
    public void setUp() {
        settings = new Settings(4, 1);
        deck = new DeckOfCards(settings.getNumberOfCards());

        player1 = new HumanPlayer("Tomas", null, 1);
        player2 = new HumanPlayer("Filip", null, 2);
        cpu2 = new ComputerPlayer(2);
        cpu1 = new ComputerPlayer(1);
    }

    @Test
    public void testGameProc1() throws InterruptedException {
//        HeadFrame headFrame = new HeadFrame();
//        headFrame.setVisible(true);
//        headFrame.onePlayerGameMenuItem.doClick();
        deck.shuffleCards();
        Game game = new Game(cpu1, cpu2, deck);
        game.run();


        //ASSERT
        assertTrue(game.isEndOfGame());
        assertEquals(settings.getNumberOfCards(), game.getUncoveredCards());

    }

    @Test
    public void testGameProc2() throws InterruptedException {

        Game game = new Game(player1, player2, deck);
        Thread gameThread = new Thread(game);
        gameThread.start();

        Thread.sleep(1500);
        if(!game.isPlayer1OnTurn()) {
            fail();
        }
        player1.setMyMove(new OneMove(0, 1));
        Thread.sleep(1500);
        if(!game.isPlayer1OnTurn()) {
            fail();
        }
        player1.setMyMove(new OneMove(2, 3));
        Thread.sleep(1500);


        //ASSERT
        assertTrue(game.isEndOfGame());
        assertEquals(settings.getNumberOfCards(), game.getUncoveredCards());
        assertEquals("Tomas WON!!", game.getResult());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGamePlayerVsSamePlayer() throws InterruptedException {
        Game game = new Game(player1, player1, deck);

    }
}
