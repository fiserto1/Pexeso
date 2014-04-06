/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class CardAL implements ActionListener{

    private Card card;
//    static boolean ready;
    private static Card firstClickedCard;
    private static Card secondClickedCard;
    
    public CardAL(Card card) {
        this.card = card;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (card.getIcon() == null) {
            if (firstClickedCard == null) {
                card.setText("");
                Image newImage = card.getCardImage().getImage().getScaledInstance(
                        card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(newImage));
                firstClickedCard = card;
            }
            else if (secondClickedCard == null) {
                card.setText("");
                Image newImage = card.getCardImage().getImage().getScaledInstance(
                        card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(newImage));
                secondClickedCard = card;
//                ready = true;
            }
        }
    }
    
    public static OneMove getMove() {
        OneMove move = new OneMove(firstClickedCard, secondClickedCard);
        firstClickedCard = null;
        secondClickedCard = null;
        return move;
    }
}
