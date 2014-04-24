
package pexeso.cards;

import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class DeckOfCards implements Serializable {
    public static final int NUMBER_OF_CARDS = 64;
    private final Card[] cards = new Card[NUMBER_OF_CARDS];
    private final int[] onlineCards = new int[NUMBER_OF_CARDS];

    public DeckOfCards() {
        int j = 1;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card("CARD");
            cards[i].setCompareNumber(j);
            cards[i].setFocusPainted(false);
            cards[i].setCardImage(new ImageIcon(getClass().getResource("/images/" + j + ".jpg")));
            cards[i].setIdNumber(i);
            if (i % 2 != 0) {
                j++;
            }
        }
    }
    
    public void shuffleCards() {
        Random rnd = new Random();
        //shuffle
        for (int i = 0; i < cards.length; i++) {
            int change = rnd.nextInt(cards.length);
            Card temp = cards[i];
            cards[i] = cards[change];
            cards[change] = temp;
            
        }
        
        //turn
        for (int i = 0; i < cards.length; i++) {
            cards[i].setIcon(null);
            cards[i].setText("CARD");
            cards[i].setVisible(true);
            cards[i].setIdNumber(i);
        }
        
    }
    
    public void recreateDeckForOnlineGame(int[] input) {
        for (int i = 0; i < cards.length; i++) {
            cards[i].setCompareNumber(input[i]);
//            cards[i].setFocusPainted(false);
            cards[i].setCardImage(new ImageIcon(getClass().getResource("/images/" + input[i] + ".jpg")));
            cards[i].setIdNumber(i);
        }
    }
    
    public Card[] getCards() {
        return cards;
    }

    public int[] getOnlineCards() {
        for (int i = 0; i < cards.length; i++) {
            onlineCards[i] = cards[i].getCompareNumber();
//            cards[i].setIdNumber(i);
        }
        
        return onlineCards;
    }
    
    
}
