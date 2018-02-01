package crud;

import org.junit.Test;
import pexeso.cards.Card;
import pexeso.players.ComputerPlayer;
import pexeso.players.HumanPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class TestCrud {

    @Test
    public void testCRUDHumanPlayer() {
        HumanPlayer player = new HumanPlayer("Tomas", null, 1);
        assertEquals("Tomas", player.getName());
        int playerScore = player.getScore();
        assertEquals(0, playerScore);
        player.setScore(playerScore + 20);
        playerScore = player.getScore();
        assertEquals(20, playerScore);
        player.setName("Filip");
        assertEquals("Filip", player.getName());
    }


    @Test
    public void testCRUDComputerPlayer() {
        ComputerPlayer player = new ComputerPlayer("Insane computer", null, 1);
        assertEquals("Insane computer", player.getName());
        int playerScore = player.getScore();
        assertEquals(0, playerScore);
        player.setScore(playerScore + 20);
        playerScore = player.getScore();
        assertEquals(20, playerScore);
        player.setName("Easy computer");
        assertEquals("Easy computer", player.getName());
    }

    @Test
    public void testCRUDCard() {
        Card card = new Card();
        assertFalse(card.isVisible());
        card.showCard();
        assertTrue(card.isVisible());
        card.hideCard();
        assertFalse(card.isVisible());
    }
}
