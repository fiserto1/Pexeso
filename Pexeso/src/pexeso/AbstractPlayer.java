/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.io.Serializable;
import javax.swing.ImageIcon;
import pexeso.delegates.PlayerDelegate;

/**
 *
 * @author Tomas
 */
public abstract class AbstractPlayer implements Serializable {
    protected int playerScore;
    protected String playerName;
    protected ImageIcon avatar;
    protected int playerNumber;
    private PlayerDelegate delegate;

    public AbstractPlayer(String playerName, ImageIcon avatar, int playerNumber) {
        this.playerName = playerName;
        this.avatar = avatar;
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
    
    public int getPlayerScore() {
        return playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
        if (delegate != null) {
            delegate.scoreChanged(this);
        }
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        if (delegate != null) {
            delegate.nameChanged(this);
        }
    }

    public void setDelegate(PlayerDelegate delegate) {
        this.delegate = delegate;
    }
    
    
    
    public abstract OneMove move(DeckOfCards deck);
    
    
}
