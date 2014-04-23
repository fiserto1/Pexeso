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
public class OnlinePlayer extends AbstractPlayer {

    public OnlinePlayer(String name, ImageIcon avatar, int playerNumber) {
        super(name, avatar, playerNumber);
    }

    @Override
    public OneMove move(DeckOfCards deck) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
