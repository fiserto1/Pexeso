/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class ComputerPlayer extends AbstractPlayer {

    public ComputerPlayer(String playerName, ImageIcon avatar) {
        super(playerName, avatar);
    }
    
    @Override
    public OneMove move(DeckOfCards deck) {
        Random rnd = new Random();
        int firstTurn, secondTurn;
        do {
            firstTurn = rnd.nextInt(DeckOfCards.NUMBER_OF_CARDS - 1) + 1;
            secondTurn = rnd.nextInt(DeckOfCards.NUMBER_OF_CARDS - 1) + 1;
        } while (firstTurn == secondTurn || !deck.getCards()[firstTurn].isVisible() 
                || !deck.getCards()[secondTurn].isVisible());
        deck.getCards()[firstTurn].doClick();
        deck.getCards()[secondTurn].doClick();
        CardAL.setMoveCompleted(false);
        return new OneMove(deck.getCards()[firstTurn], deck.getCards()[secondTurn]);
    }
}
