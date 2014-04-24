/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.players;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import pexeso.OneMove;
import pexeso.delegates.PlayerDelegate;

/**
 *
 * @author Tomas
 */
public abstract class AbstractPlayer implements Serializable {
    
    protected int score;
    protected String name;
    protected ImageIcon avatar;
    protected int playerNumber;
    private transient PlayerDelegate delegate;

    public AbstractPlayer(String name, ImageIcon avatar, int playerNumber) {
        this.name = name;
        this.avatar = avatar;
        this.playerNumber = playerNumber;
    }

    
    public abstract OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves);
    
    
    public int getPlayerNumber() {
        return playerNumber;
    }
    
    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getAvatar() {
        return avatar;
    }

    public PlayerDelegate getDelegate() {
        return delegate;
    }
    
    public void setScore(int score) {
        this.score = score;
        if (delegate != null) {
            delegate.scoreChanged(this);
        }
    }
    
    public void setName(String name) {
        this.name = name;
        if (delegate != null) {
            delegate.nameChanged(this);
        }
    }

    public void setAvatar(ImageIcon avatar) {
        this.avatar = avatar;
        if (delegate != null) {
            delegate.avatarChanged(this);
        }
    }
    
    public void setDelegate(PlayerDelegate delegate) {
        this.delegate = delegate;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
