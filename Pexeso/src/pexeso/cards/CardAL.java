/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.cards;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.ImageIcon;
import pexeso.OneMove;

/**
 *
 * @author Tomas
 */
public class CardAL implements ActionListener, Serializable {
    
//    private Game game;
//    private Card card;
    private static boolean moveCompleted;
    private static OneMove move = new OneMove(-1, -1);
    
    public CardAL() {
//        this.card = card;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Card) {
            Card card = (Card) e.getSource();
            if (card.getIcon() == null) {
                if (move.getFirstCardIDNumber() == -1) {
                    card.setText("");
                    Image newImage = card.getCardImage().getImage().getScaledInstance(
                            card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                    card.setIcon(new ImageIcon(newImage));
                    move.setFirstCardIDNumber(card.getIdNumber());
                } else if (move.getSecondCardIDNumber() == -1) {
                    card.setText("");
                    Image newImage = card.getCardImage().getImage().getScaledInstance(
                            card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                    card.setIcon(new ImageIcon(newImage));
                    move.setSecondCardIDNumber(card.getIdNumber());
                    moveCompleted = true;
                }
            }
        }
    }

    public static void setMoveCompleted(boolean moveCompleted) {
        CardAL.moveCompleted = moveCompleted;
    }

    public static boolean isMoveCompleted() {
        return moveCompleted;
    }
    
    
    public static OneMove getMove() {
        return move;
    }

    public static void unmarkCards() {
        move.setFirstCardIDNumber(-1);
        move.setSecondCardIDNumber(-1);
        move.setFirstCardCompareNumber(-1);
        move.setSecondCardCompareNumber(-1);
    }
}
