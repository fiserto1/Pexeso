/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.players;

import pexeso.OneMove;
import pexeso.delegates.PlayerDelegate;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstraktni trida pro hrace. Obsahuje zakladni parametry hrace (jmeno, cislo,
 * score, avatar, delegat). Nuti potomky, aby obsahovali metodu pro tahnuti.
 *
 * @author Tomas
 */
public abstract class AbstractPlayer implements Serializable {

    protected int score;
    protected String name;
    protected ImageIcon avatar;
    protected int playerNumber;
    private transient PlayerDelegate delegate;

    /**
     *
     * @param name Jmeno hrace.
     * @param avatar Avatar hrace.
     * @param playerNumber Cislo hrace.
     */
    public AbstractPlayer(String name, ImageIcon avatar, int playerNumber) {
        this.name = name;
        this.avatar = avatar;
        this.playerNumber = playerNumber;
    }

    /**
     *
     * @param myLastMove Hracuv posledni tah - jiz vyhodnoceny.
     * @param oppMoves Protihracuv posledni tah/y (zalezi jestli protihrac uhodl
     * dvojici).
     * @param numberOfCards Pocatecni pocet karet ve hre.
     * @return Vrati tah hrace.
     */
    public abstract OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves,
                                 int numberOfCards);

    /**
     *
     * @return Vrati cislo hrace.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     *
     * @return Vrati hracovo skore.
     */
    public int getScore() {
        return score;
    }

    /**
     *
     * @return Vrati jmeno hrace.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return Vrati avatara hrace.
     */
    public ImageIcon getAvatar() {
        return avatar;
    }

    /**
     *
     * @return Vrati delegata hrace.
     */
    public PlayerDelegate getDelegate() {
        return delegate;
    }

    /**
     * Nastavi hracovo skore.
     *
     * @param score Hracovo skore.
     */
    public void setScore(int score) {
        this.score = score;
        if (delegate != null) {
            delegate.scoreChanged(this);
        }
    }

    /**
     * Nastavi jmeno hrace.
     *
     * @param name Hracovo jmeno.
     */
    public void setName(String name) {
        this.name = name;
        if (delegate != null) {
            delegate.nameChanged(this);
        }
    }

    /**
     * Nastavi avatara hracovi.
     *
     * @param avatar Hracuv avatar.
     */
    public void setAvatar(ImageIcon avatar) {
        this.avatar = avatar;
        if (delegate != null) {
            delegate.avatarChanged(this);
        }
    }

    /**
     * Nastavi delegata hracovi.
     *
     * @param delegate Hracuv delegat.
     */
    public void setDelegate(PlayerDelegate delegate) {
        this.delegate = delegate;
        if (delegate != null) {
            delegate.showPlayerOnBoard(this);
        }
    }

    /**
     * Nastavi cislo hrace.
     *
     * @param playerNumber Cislo hrace.
     */
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.score;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.avatar != null ? this.avatar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractPlayer other = (AbstractPlayer) obj;
        if (this.score != other.score) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.avatar != other.avatar && (this.avatar == null || !this.avatar.equals(other.avatar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractPlayer{" + "score=" + score + ", name=" + name + ", avatar=" + avatar + ", playerNumber=" + playerNumber + ", delegate=" + delegate + '}';
    }
}
