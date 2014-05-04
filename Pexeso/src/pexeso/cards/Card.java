/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.cards;

import java.io.Serializable;
import javax.swing.ImageIcon;
import pexeso.delegates.CardDelegate;

/**
 *
 * @author Tomas
 */
public class Card implements Serializable {
    private String reverseSide = "CARD";
    private ImageIcon cardImage;
    private int compareNumber;
    private int idNumber;
    private transient CardDelegate delegate;
    
    
    public void hideCard() {
        if (delegate != null) {
            delegate.cardRevealed(this);
        }
    }
    
    public void showCard() {
        if (delegate != null) {
            delegate.showCard(this);
        }
    }

    public void turnBack() {
        if (delegate != null) {
            delegate.turnBackCard(this);
        }
    }
    
    public ImageIcon getCardImage() {
        return cardImage;
    }

    public void setCardImage(ImageIcon cardIcon) {
        this.cardImage = cardIcon;
    }

    public int getCompareNumber() {
        return compareNumber;
    }

    public void setCompareNumber(int compareNumber) {
        this.compareNumber = compareNumber;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getReverseSide() {
        return reverseSide;
    }

    public void setDelegate(CardDelegate delegate) {
        this.delegate = delegate;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.compareNumber;
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
        final Card other = (Card) obj;
        if (this.compareNumber != other.compareNumber) {
            return false;
        }
        return true;
    }
}
