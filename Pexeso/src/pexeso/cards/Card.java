/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.cards;

import java.io.Serializable;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Tomas
 */
public class Card extends JButton implements Serializable {
    private final String REVERSE_SIDE = "CARD";
    private ImageIcon cardImage = new ImageIcon();
    private int compareNumber;
    private int idNumber;

    public Card() {
    }

    public Card(Icon icon) {
        super(icon);
    }

    public Card(String text) {
        super(text);
    }

    public Card(Action a) {
        super(a);
    }

    public Card(String text, Icon icon) {
        super(text, icon);
    }
    
    public void showCard() {
        this.setText("");
        this.setIcon(cardImage);
    }
    
    public void turnBack() {
        this.setText(REVERSE_SIDE);
        this.setIcon(null);
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
