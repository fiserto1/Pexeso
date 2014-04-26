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
        if (e.getSource() instanceof CardButton) {
            CardButton cardBut = (CardButton) e.getSource();
            if (cardBut.getIcon() == null) {
                if (move.getFirstCardIDNumber() == -1) {
                    cardBut.showCard();
                    move.setFirstCardIDNumber(cardBut.getCard().getIdNumber());
                    move.setFirstCardCompareNumber(cardBut.getCard().getCompareNumber());
                } else if (move.getSecondCardIDNumber() == -1) {
                    cardBut.showCard();
                    move.setSecondCardIDNumber(cardBut.getCard().getIdNumber());
                    move.setSecondCardCompareNumber(cardBut.getCard().getCompareNumber());
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
