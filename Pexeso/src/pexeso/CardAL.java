/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class CardAL implements ActionListener, Serializable {
    
    private Game game;
    private Card card;
    private static boolean moveCompleted;
    private static OneMove move = new OneMove(null, null);
    
    public CardAL(Card card, Game game) {
        this.card = card;
        this.game = game;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (card.getIcon() == null) {
            if (move.getFirstCard() == null) {
                card.setText("");
                Image newImage = card.getCardImage().getImage().getScaledInstance(
                        card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(newImage));
                move.setFirstCard(card);
            }
            else if (move.getSecondCard() == null) {
                card.setText("");
                Image newImage = card.getCardImage().getImage().getScaledInstance(
                        card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(newImage));
                move.setSecondCard(card);
                moveCompleted = true;
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
        move.setFirstCard(null);
        move.setSecondCard(null);
    }
}
