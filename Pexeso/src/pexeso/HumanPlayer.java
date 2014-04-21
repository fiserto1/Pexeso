/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

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
        OneMove move = null;
        if (CardAL.isMoveCompleted()) {
            CardAL.setMoveCompleted(false);
            move = CardAL.getMove();
        }
        return move;
    }
    

}
