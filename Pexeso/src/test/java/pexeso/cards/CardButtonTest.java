package pexeso.cards;

import org.junit.Before;
import org.junit.Test;
import pexeso.gui.CardButton;

import javax.swing.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

/**
 * Created by Tomas on 23-Jan-16.
 */
public class CardButtonTest {

    CardButton cardButton;
    Card card;

    @Before
    public void setUp() {
        card = new Card();
        card.setCompareNumber(1);
        cardButton = new CardButton(card);
    }


    @Test
    public void testTurnBack() throws Exception {
        cardButton.turnBack();

        assertNull(cardButton.getIcon());
        assertTrue(cardButton.isVisible());
        assertEquals("CARD", cardButton.getText());
    }



    @Test
    public void testLoadImgFromFile() throws Exception {
        ImageIcon expectedValue = new ImageIcon(getClass().getResource("/images/1.jpg"));

        ImageIcon realValue = cardButton.loadImgFromFile(1);

        assertEquals(expectedValue, realValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImgFromFileInvalidArgumentMinus1() {
        cardButton.loadImgFromFile(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImgFromFileInvalidArgument0() {
        cardButton.loadImgFromFile(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadImgFromFileInvalidArgument33() {
        cardButton.loadImgFromFile(33);
    }
}