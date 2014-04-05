/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Tomas
 */
public class DeckOfCards {
    
    private final Card[] cards = new Card[64];

    public DeckOfCards() {
        int j = 1;
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new Card("CARD");
//            cards[i].setIcon(new ImageIcon("src\\cards\\" + j + ".jpg"));
            cards[i].setCardImage(new ImageIcon("src\\cards\\" + j + ".jpg"));
            cards[i].addActionListener(new CardAL(cards[i]));
            if (i%2 != 0) {
                j++;
            }
        }
    }
    
    public void shuffleCards() {
        Random rnd = new Random();
        for (int i = 0; i < cards.length; i++) {
            int change = rnd.nextInt(cards.length);
            Card temp = cards[i];
            cards[i] = cards[change];
            cards[change] = temp;
            
        }
    }
    
    public JButton[] getCards() {
        return cards;
    }
    
    
}
