
package pexeso;

import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class DeckOfCards {
    public static final int NUMBER_OF_CARDS = 64;
    private final Card[] cards = new Card[NUMBER_OF_CARDS];

    public DeckOfCards() {
        int j = 1;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card("CARD");
            cards[i].setCardImage(new ImageIcon(getClass().getResource("/cards/" + j + ".jpg")));
            if (i % 2 != 0) {
                j++;
            }
        }
    }
    
    public void shuffleCards() {
        Random rnd = new Random();
        for (int i = 0; i < cards.length; i++) {
            cards[i].setIcon(null);
            cards[i].setText("CARD");
            cards[i].setVisible(true);
            int change = rnd.nextInt(cards.length);
            Card temp = cards[i];
            cards[i] = cards[change];
            cards[change] = temp;
            
        }
    }
    
    public Card[] getCards() {
        return cards;
    }
}
