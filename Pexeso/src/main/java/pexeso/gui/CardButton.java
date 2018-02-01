/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.gui;

import pexeso.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Trida pro tlacitko s kartickou. Rozsireny JButton o vlastnost mit na sobe
 * kartu.
 *
 * @author Tomas
 */
public class CardButton extends JButton {

    private static final long serialVersionUID = 2378059682891565891L;
    private final Card card;

    /**
     * Nastavi tlacitku zadni stranu karty a zakaze vykreslovani posledniho
     * kliknuti.
     *
     * @param card Karta pridruzena k tlacitku.
     */
    public CardButton(Card card) {
        this.card = card;
        setFocusPainted(false);
        setText(card.getReverseSide());
    }

    /**
     * Nastavi tlacitku predni stranu karty v rozumnem meritku.
     */
    public void showCard() {
        this.setText("");

        int width = this.getHeight() - (this.getHeight() / 5);
        int height = -1;
        ImageIcon loadedImageIcon = loadImgFromFile(card.getCompareNumber());
        ImageIcon rescaledImIcon = new ImageIcon(loadedImageIcon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH));
        this.setIcon(rescaledImIcon);
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
     * Nacte predni stranu karty ze souboru. Cislo souboru je stejne jako
     * porovnavaci cislo karty. Takze vzniknou dvojice obrazku.
     *
     * @param fileNumber Cislo souboru.
     * @return
     */
    public ImageIcon loadImgFromFile(int fileNumber) {
        if (fileNumber < 1 || fileNumber > 32) {
            throw new IllegalArgumentException("File number has to be in the interval <1, 32>.");
        }
        final String filepath = String.format("/images/%d.jpg", fileNumber);
        URL resource = getClass().getResource(filepath);
        ImageIcon image = new ImageIcon(resource);
        return image;
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
