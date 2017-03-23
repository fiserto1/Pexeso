/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.players;

import pexeso.OneMove;
import pexeso.cards.CardAL;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Trida pro lidskeho hrace, ktery klika na tlacitka.
 *
 * @author Tomas
 */
public class HumanPlayer extends AbstractPlayer {

    private boolean moveCompleted;
    private OneMove myMove;

    /**
     *
     * @param name Jmeno hrace.
     * @param avatar Avatar hrace.
     * @param playerNumber Cislo hrace.
     */
    public HumanPlayer(String name, ImageIcon avatar, int playerNumber) {
        super(name, avatar, playerNumber);
    }

    @Override
    public OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves,
            int numberOfCards) {

        myMove = null;
        moveCompleted = false;

        CardAL listener = new CardAL(this);
        if (getDelegate() != null) {
            getDelegate().activateCards(listener);
        }
        //wait for user choice
        while (!moveCompleted) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                moveCompleted = false;
                myMove = null;
//                System.out.println("Interrupted");
                break;
            }
        }
        if (getDelegate() != null) {
            getDelegate().deactivateCards(listener);
        }
        return myMove;
    }

    /**
     * true - Tah je kompletni. false - Tah neni kompletni.
     *
     * @param moveCompleted Kompletni tah.
     */
//    public void setMoveCompleted(boolean moveCompleted) {
//        this.moveCompleted = moveCompleted;
//    }

    /**
     * Nastavi hracuv tah.
     *
     * @param myMove Aktualni tah.
     */
    public void setMyMove(OneMove myMove) {
        this.myMove = myMove;
        this.moveCompleted = true;
    }
}
