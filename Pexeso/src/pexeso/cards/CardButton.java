/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.cards;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Trida pro tlacitko s kartickou.
 * Rozsiruje tridu JButton
 * @author Tomas
 */
public class CardButton extends JButton {

    private final Card card;

    /**
     * Nastavi tlacitku zadni stranu karty a zakaze vykreslovani posledniho kliknuti.
     * @param card Karta pridruzena k tlacitku.
     */
    public CardButton(Card card) {
        this.card = card;
        setFocusPainted(false);
        setText(card.getReverseSide());
    }

    /**
     * Nastavi tlacitku predni stranu karty v rozumnem meritku
     */
    public void showCard() {
        this.setText("");
        ImageIcon newImage = new ImageIcon(card.getCardImage().getImage().getScaledInstance(
                (this.getHeight() - (this.getHeight() / 5)), -1, Image.SCALE_SMOOTH));
        this.setIcon(newImage);
    }

    /**
     * Nastavi tlacitku zadni stranu karty.
     */
    public void turnBack() {
        this.setText(card.getReverseSide());
        this.setIcon(null);
        this.setVisible(true);
    }

    /**
     * 
     * @return Vrati kartu pridruzenou k tlacitku.
     */
    public Card getCard() {
        return card;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.card != null ? this.card.hashCode() : 0);
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
        final CardButton other = (CardButton) obj;
        if (this.card != other.card && (this.card == null || !this.card.equals(other.card))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardButton{" + "card=" + card + '}';
    }
}
