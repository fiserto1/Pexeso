package pexeso.gui;

import org.junit.Before;
import org.junit.Test;
import pexeso.cards.Card;
import pexeso.players.HumanPlayer;

import java.awt.*;

public class CardALTest {

    CardAL cardAL;
    CardButton cardButton1;
    CardButton cardButton2;

    @Before
    public void setUp() throws Exception {
        HumanPlayer player = new HumanPlayer("Tomas", null, 1);
        cardAL = new CardAL(player);
        Card card1 = new Card();
        card1.setCompareNumber(1);
        cardButton1 = new CardButton(card1);
        cardButton1.setSize(new Dimension(30, 30));
        cardButton1.addActionListener(cardAL);

        Card card2 = new Card();
        card2.setCompareNumber(2);
        cardButton2 = new CardButton(card2);
        cardButton2.setSize(new Dimension(30, 30));
        cardButton2.addActionListener(cardAL);
    }

    @Test
    public void actionPerformed() {
        cardButton1.doClick();
        cardButton2.doClick();
//        cardAL.actionPerformed();
    }

}