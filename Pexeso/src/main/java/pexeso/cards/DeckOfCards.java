package pexeso.cards;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Trida pro balicek karet.
 *
 * @author Tomas
 */
public class DeckOfCards implements Serializable {

    private int numberOfCards;
    /**
     * Balicek karet.
     */
    private final Card[] cards;

    /**
     * Pocet karet v balicku musi byt v intervalu od 4 do 64 vcetne
     *
     * @param numberOfCards Pocet karet v balicku.
     */
    public DeckOfCards(int numberOfCards) {
        if (numberOfCards < 4 || numberOfCards > 64) {
            throw new IllegalArgumentException("Wrong number of cards.");
        }
        this.numberOfCards = numberOfCards;
        this.cards = new Card[numberOfCards];
        createDeck();
    }

    /**
     * Vytvori balicek dvojic karet do pole. Priradi kazde karte porovnavaci
     * cislo, ID.
     */
    public void createDeck() {
        int j = 1;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card();
            cards[i].setCompareNumber(j);
            cards[i].setIdNumber(i);
            if (i % 2 != 0) {
                j++;
            }
        }
    }

    /**
     * Zamicha balicek karet a otoci je rubem vzhuru.
     */
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

    /**
     *
     * @return Vrati pocet karet v balicku.
     */
    public int size() {
        return numberOfCards;
    }

    /**
     * Vrati balicek karet v poli.
     *
     * @return
     */
    public Card[] getCards() {
        return cards;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeckOfCards other = (DeckOfCards) obj;
        if (this.numberOfCards != other.numberOfCards) {
            return false;
        }
        if (!Arrays.deepEquals(this.cards, other.cards)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DeckOfCards{" + "numberOfCards=" + numberOfCards + ", cards=" + cards + '}';
    }
}
