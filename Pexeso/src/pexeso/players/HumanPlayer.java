/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.players;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import pexeso.cards.CardAL;
import pexeso.games.Game;
import pexeso.OneMove;

/**
 *
 * @author Tomas
 */
public class HumanPlayer extends AbstractPlayer {
    
    public HumanPlayer(String name, ImageIcon avatar, int playerNumber) {
        super(name, avatar, playerNumber);
    }
    
    @Override
    public OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves) {
        //wait for user choice
        CardAL.setMoveCompleted(false);
        
        while (!CardAL.isMoveCompleted()) {
            if (Game.gameInterrupted) {
                CardAL.setMoveCompleted(false);
                CardAL.unmarkCards();
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(HumanPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return CardAL.getMove();
    }
    

}
