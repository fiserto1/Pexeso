package pexeso.games;

import org.junit.Before;
import org.junit.Test;
import pexeso.cards.DeckOfCards;
import pexeso.players.HumanPlayer;

import javax.swing.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class GameTest {
    Game game;
    HumanPlayer player1;
    HumanPlayer player2;
    DeckOfCards deck;

    @Before
    public void setUp() {
        ImageIcon avatar = new ImageIcon(getClass().getResource("/avatars/Professor.png"));
        player1 = new HumanPlayer("Tomas", avatar, 1);
        player2 = new HumanPlayer("Filip", avatar, 2);
        deck = new DeckOfCards(4);
        game = new Game(player1, player2, deck);
    }



    @Test
    public void testEndGamePlayer1Win() throws Exception {

        player1.setScore(70);
        player2.setScore(60);

        game.endGame();
        String realResult = game.getResult();

        assertEquals(realResult, "Tomas WON!!");

    }

    @Test
    public void testEndGamePlayer2Win() throws Exception {
        player1.setScore(40);
        player2.setScore(60);

        game.endGame();
        String realResult = game.getResult();

        assertEquals(realResult, "Filip WON!!");

    }

    @Test
    public void testEndGameDraw() throws Exception {
        player1.setScore(60);
        player2.setScore(60);

        game.endGame();
        String realResult = game.getResult();


        assertEquals(realResult, "DRAW");

    }



    @Test
    public void testChangePlayerOnTurn() throws Exception {

        game.changePlayerOnTurn();

        assertTrue(!game.isPlayer1OnTurn());

        game.changePlayerOnTurn();

        assertTrue(game.isPlayer1OnTurn());

    }
}