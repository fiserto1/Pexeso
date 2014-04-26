/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import pexeso.OneMove;
import pexeso.players.AbstractPlayer;
import pexeso.players.HumanPlayer;

/**
 *
 * @author Tomas
 */
public class CardAL implements ActionListener, Serializable {
    
    private OneMove move = new OneMove(-1, -1);
    private HumanPlayer player;

    public CardAL(HumanPlayer player) {
        this.player = player;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Card) {
            Card card = (Card) e.getSource();
            if (card.getIcon() == null) {
                if (move.getFirstCardIDNumber() == -1) {
                    card.showCard();
                    move.setFirstCardIDNumber(card.getIdNumber());
                    move.setFirstCardCompareNumber(card.getCompareNumber());
                } else if (move.getSecondCardIDNumber() == -1) {
                    card.showCard();
                    move.setSecondCardIDNumber(card.getIdNumber());
                    move.setSecondCardCompareNumber(card.getCompareNumber());
                    player.setMyMove(move);
                    player.setMoveCompleted(true);
                }
            }
        }
    }
    
    public OneMove getMove() {
        return move;
    }
}
