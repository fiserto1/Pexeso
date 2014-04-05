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
    private ImageIcon cardIcon = new ImageIcon();

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

    public ImageIcon getCardIcon() {
        return cardIcon;
    }

    public void setCardIcon(ImageIcon cardIcon) {
        this.cardIcon = cardIcon;
    }
    
    
}
