/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Tomas
 */
public class CardAL implements ActionListener{

    private Card card;
    
    public CardAL(Card card) {
        this.card = card;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (card.getIcon() == null) {
            card.setIcon(card.getCardImage());
        } else {
            card.setIcon(null);
        }
    }
    
}
