
package pexeso.cards;

import java.awt.Image;
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

    public DeckOfCards() {
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
        
        //turn
        for (int i = 0; i < cards.length; i++) {
            cards[i].turnBack();
            cards[i].setIdNumber(i);
        }
    }
    
    public Card[] getCards() {
        return cards;
    }
    
    private ImageIcon loadImgFromFile(int fileNumber) {
        ImageIcon image = new ImageIcon(getClass().getResource(
                "/images/" + fileNumber + ".jpg"));
        ImageIcon newImage = new ImageIcon(image.getImage().getScaledInstance(
                image.getIconWidth() / 2, -1, Image.SCALE_SMOOTH));
        return newImage;
    }
}
