/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.delegates;

import pexeso.cards.Card;

/**
 *
 * @author Tomas
 */
public interface CardDelegate {
    
    public void cardRevealed(Card card);
    
    public void showCard(Card card);
    
    public void turnBackCard(Card card);
    
}
