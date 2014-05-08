/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.delegates;

import pexeso.cards.Card;

/**
 * Interface pro delegata karty.
 * @author Tomas
 */
public interface CardDelegate {
    
    /**
     * Oznami, ze karta byla uhadnuta a schovana.
     * @param card Uhadnuta karta.
     */
    public void cardRevealed(Card card);
    
    /**
     * Oznami, ze karta byla otocena licem vzhuru.
     * @param card Otocena karta.
     */
    public void cardShowed(Card card);
    
    /**
     * Oznami, ze karta byla otocena rubem vzhuru.
     * @param card Otocena karta.
     */
    public void cardTurnedBack(Card card);
    
}
