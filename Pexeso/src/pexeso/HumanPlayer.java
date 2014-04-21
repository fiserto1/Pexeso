/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class HumanPlayer extends AbstractPlayer {

//    private ImageIcon defaultPlayerAvatar = new ImageIcon(getClass().getResource("/Avatars/Professor.png"));
    
    public HumanPlayer(String name, ImageIcon avatar, int playerNumber) {
        super(name, avatar, playerNumber);
    }
    
    @Override
    public OneMove move(DeckOfCards deck) {
        //wait for user choice
        CardAL.setMoveCompleted(false);
        CardAL listener = new CardAL();
        for (int i = 0; i < deck.getCards().length; i++) {
            deck.getCards()[i].addActionListener(listener);
        }
        
        while (!CardAL.isMoveCompleted()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(HumanPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for (int i = 0; i < deck.getCards().length; i++) {
            deck.getCards()[i].removeActionListener(listener);
        }
        
        return CardAL.getMove();
    }
    

}
