/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

/**
 *
 * @author Tomas
 */
public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(String playerName) {
        super(playerName);
    }

    public HumanPlayer() {
        this.playerName = "UnknownPlayer";
    }
    
    @Override
    public void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
