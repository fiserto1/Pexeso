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
public abstract class AbstractPlayer {
    protected int playerScore = 0;
    protected String playerName;
    protected ImageIcon avatar;

    public AbstractPlayer(String playerName, ImageIcon avatar) {
        this.playerName = playerName;
        this.avatar = avatar;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    
    public abstract OneMove move(DeckOfCards deck);
    
    
}
