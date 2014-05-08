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
import pexeso.players.HumanPlayer;

/**
 * Trida pro posluchace karty. Dovoluje/zakazuje hraci kliknout na tlacitko s
 * kartou. Pri kliknuti na tlacitko otoci kartu licem vzhuru a pokud se jedna o
 * druhou kartu, tak oznami hraci, ze tah je kompletni a posle mu ID karet, na
 * ktere klikl.
 *
 * @author Tomas
 */
public class CardAL implements ActionListener, Serializable {

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

    /**
     *
     * @return Vrati soucasny tah.
     */
    public OneMove getMove() {
        return move;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.move != null ? this.move.hashCode() : 0);
        hash = 37 * hash + (this.player != null ? this.player.hashCode() : 0);
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
        final CardAL other = (CardAL) obj;
        if (this.move != other.move && (this.move == null || !this.move.equals(other.move))) {
            return false;
        }
        if (this.player != other.player && (this.player == null || !this.player.equals(other.player))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardAL{" + "move=" + move + ", player=" + player + '}';
    }
}
