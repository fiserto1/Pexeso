package pexeso.gui;

import org.junit.Before;
import org.junit.Test;
import pexeso.cards.Card;

import javax.swing.*;

import java.awt.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

public class CardButtonTest {
    CardButton cardButton;
    Card card;
    @Before
    public void setUp() {
        card = new Card();
        card.setCompareNumber(1);
        cardButton = new CardButton(card);
    }

    @Test(expected = IllegalArgumentException.class)
    public void showCardInvalidSize() {
        cardButton.showCard();
    }

    @Test
    public void showCard() {
        cardButton.setSize(new Dimension(30, 30));

        cardButton.showCard();

        assertEquals("", cardButton.getText());
        Icon icon = cardButton.getIcon();
        assertNotNull(icon);
        assertEquals(24, icon.getIconWidth());
        assertEquals(24, icon.getIconHeight());

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

        assertEquals(expectedValue.getImage(), realValue.getImage());
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

    @Test
    public void testEquals() {
        assertFalse(cardButton.equals(null));

        JButton but = new JButton();
        assertFalse(cardButton.equals(but));

        CardButton comparedCB = new CardButton(new Card());

        assertFalse(cardButton.equals(comparedCB));

        Card card = new Card();
        card.setCompareNumber(2);
        comparedCB = new CardButton(card);

        assertFalse(cardButton.equals(comparedCB));

        card.setCompareNumber(1);
        assertTrue(cardButton.equals(comparedCB));
    }
}