package pexeso.cards;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Tomas on 24-Jan-16.
 */
public class CardTest {

    @Test
    public void testEquals() throws Exception {
        Card card1 = new Card(2, 0);
        Card card2 = new Card(2, 1);
        Card card3 = new Card(2, 0);
        Card card4 = new Card(3, 1);
        Card card5 = new Card(3, 0);

        boolean realResult = card1.equals(card2);

        assertTrue(realResult);

        realResult = card1.equals(card3);

        assertTrue(realResult);

        realResult = card1.equals(card4);

        assertTrue(!realResult);

        realResult = card1.equals(card5);

        assertTrue(!realResult);
    }
}