/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pexeso.OneMove;
import pexeso.players.HumanPlayer;

/**
 * Trida pro posluchace karty. Dovoluje/zakazuje hraci kliknout na tlacitko s
 * kartou. Pri kliknuti na tlacitko otoci kartu licem vzhuru a pokud se jedna o
 * druhou kartu, tak oznami hraci, ze tah je kompletni a posle mu ID karet, na
 * ktere klikl.
 *
 * @author Tomas
 */
public class CardAL implements ActionListener {

    private final OneMove move = new OneMove(-1, -1);
    private final HumanPlayer player;

    /**
     *
     * @param player Hrac ke kteremu se vztahuje posluchac karet.
     */
    public CardAL(HumanPlayer player) {
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof CardButton) {
            CardButton cardBut = (CardButton) e.getSource();
            if (cardBut.getIcon() == null) {
                if (move.getFirstCardIDNumber() == -1) { //first click
                    cardBut.showCard();
                    move.setFirstCardIDNumber(cardBut.getCard().getIdNumber());
                    move.setFirstCardCompareNumber(cardBut.getCard().getCompareNumber());
                } else if (move.getSecondCardIDNumber() == -1) { //second click
                    cardBut.showCard();
                    move.setSecondCardIDNumber(cardBut.getCard().getIdNumber());
                    move.setSecondCardCompareNumber(cardBut.getCard().getCompareNumber());
                    player.setMyMove(move);
                    player.setMoveCompleted(true);
                }
            }
        }
    }

    /**
     *
     * @return Vrati soucasny tah.
     */
    public OneMove getMove() {
        return move;
    }
}
