/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Tomas
 */
public class Card extends JButton {
    private ImageIcon cardImage = new ImageIcon();

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

    public ImageIcon getCardImage() {
        return cardImage;
    }

    public void setCardImage(ImageIcon cardIcon) {
        this.cardImage = cardIcon;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.cardImage != null ? this.cardImage.hashCode() : 0);
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
        if (this.cardImage.getImage() != other.cardImage.getImage() && (this.cardImage == null || !this.cardImage.getImage().equals(other.cardImage.getImage()))) {
            return false;
        }
        return true;
    }


}
