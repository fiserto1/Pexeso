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
 *
 * @author Tomas
 */
public class CardButton extends JButton {

    private final Card card;

    public CardButton(Card card) {
        this.card = card;
        setFocusPainted(false);
        setText(card.getReverseSide());
    }

    public void showCard() {
        this.setText("");
        ImageIcon newImage = new ImageIcon(card.getCardImage().getImage().getScaledInstance(
                (this.getHeight() - (this.getHeight() / 5)), -1, Image.SCALE_SMOOTH));
        this.setIcon(newImage);
    }

    public void turnBack() {
        this.setText(card.getReverseSide());
        this.setIcon(null);
        this.setVisible(true);
    }

    public Card getCard() {
        return card;
    }
}
