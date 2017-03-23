package crud;

import org.junit.Test;
import pexeso.cards.Card;
import pexeso.players.ComputerPlayer;
import pexeso.players.HumanPlayer;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class TestCrud {

    @Test
    public void testCRUDHumanPlayer() {
        HumanPlayer player = new HumanPlayer("Tomas", null, 1);
        int playerScore = player.getScore();
        player.setScore(playerScore + 20);
        playerScore = player.getScore();
        player.setName("Filip");
        String playerName = player.getName();
    }


    @Test
    public void testCRUDComputerPlayer() {
        ComputerPlayer player = new ComputerPlayer("Tomas", null, 1);
        int playerScore = player.getScore();
        player.setScore(playerScore + 20);
        playerScore = player.getScore();
        player.setName("Filip");
        String playerName = player.getName();
    }

    @Test
    public void testCRUDCard() {
        Card card = new Card();
        card.showCard();
        card.hideCard();
    }


}
