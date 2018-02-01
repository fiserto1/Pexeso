package pexeso.cards;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class DeckOfCardsTest {
    DeckOfCards deck;
    int numberOfCards;

    @Before
    public void setUp(){
        numberOfCards = 4;
        deck = new DeckOfCards(numberOfCards);
    }

    @Test
    public void testShuffleCards() throws Exception {
        //ARRANGE

        //ACT
        deck.shuffleCards();

        //ASSERT


        assertEquals(0, deck.getCards()[0].getIdNumber());
        assertEquals(1, deck.getCards()[1].getIdNumber());
        assertEquals(2, deck.getCards()[2].getIdNumber());
        assertEquals(3, deck.getCards()[3].getIdNumber());
    }

    @Test
    public void testCreateDeck() throws Exception {
        //ARRANGE

        //ACT
        deck.createDeck();

        //ASSERT

        assertEquals(1, deck.getCards()[0].getCompareNumber());
        assertEquals(1, deck.getCards()[1].getCompareNumber());
        assertEquals(2, deck.getCards()[2].getCompareNumber());
        assertEquals(2, deck.getCards()[3].getCompareNumber());

        assertEquals(0, deck.getCards()[0].getIdNumber());
        assertEquals(1, deck.getCards()[1].getIdNumber());
        assertEquals(2, deck.getCards()[2].getIdNumber());
        assertEquals(3, deck.getCards()[3].getIdNumber());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        // TODO: 01-Feb-18 parametrized?
        new DeckOfCards(0);
    }
}