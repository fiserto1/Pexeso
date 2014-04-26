/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.cards;

import javax.swing.JButton;

/**
 *
 * @author Tomas
 */
public class CardButton extends JButton {
    Card card;

    public CardButton(Card card) {
        this.card = card;
        setFocusPainted(false);
        setText(card.getReverseSide());
    }
    
    public void showCard() {
        this.setText("");
        this.setIcon(card.getCardImage());
    }

    public void turnBack() {
        this.setText(card.getReverseSide());
        this.setIcon(null);
    }

    public Card getCard() {
        return card;
    }
    
    
    
}
