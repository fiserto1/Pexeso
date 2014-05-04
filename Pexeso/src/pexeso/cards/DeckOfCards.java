
package pexeso.cards;

import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class DeckOfCards implements Serializable {
//    public static final int NUMBER_OF_CARDS = 64;
    private int numberOfCards;
    private final Card[] cards;

    public DeckOfCards(int numberOfCards) {
        if (numberOfCards < 4 || numberOfCards > 64) {
            throw new IllegalArgumentException("Wrong number of cards.");
        }
        this.numberOfCards = numberOfCards;
        this.cards = new Card[numberOfCards];
        int j = 1;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card();
            cards[i].setCompareNumber(j);
            cards[i].setCardImage(loadImgFromFile(j));
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
        
        //turn back
        for (int i = 0; i < cards.length; i++) {
            cards[i].turnBack();
            cards[i].setIdNumber(i);
        }
    }
    public int size() {
        return numberOfCards;
    }
    
    public Card[] getCards() {
        return cards;
    }
    
    private ImageIcon loadImgFromFile(int fileNumber) {
        ImageIcon image = new ImageIcon(getClass().getResource(
                "/images/" + fileNumber + ".jpg"));
        return image;
    }
}
