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
public abstract class AbstractPlayer {
    private int playerScore;
    private String playerName;

    public AbstractPlayer(int playerScore, String playerName) {
        this.playerScore = playerScore;
        this.playerName = playerName;
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
    
    
    public abstract void move();
    
    
}
