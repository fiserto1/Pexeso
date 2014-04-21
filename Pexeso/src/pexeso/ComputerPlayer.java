/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Tomas
 */
public class ComputerPlayer extends AbstractPlayer {

    public ComputerPlayer(String playerName, ImageIcon avatar, int playerNumber) {
        super(playerName, avatar, playerNumber);
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
        
        //do click
        deck.getCards()[firstTurn].setText("");
        Image newImage = deck.getCards()[firstTurn].getCardImage().getImage().getScaledInstance(
                deck.getCards()[firstTurn].getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
        deck.getCards()[firstTurn].setIcon(new ImageIcon(newImage));
        
        deck.getCards()[secondTurn].setText("");
        newImage = deck.getCards()[secondTurn].getCardImage().getImage().getScaledInstance(
                deck.getCards()[secondTurn].getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
        deck.getCards()[secondTurn].setIcon(new ImageIcon(newImage));
        
        return new OneMove(deck.getCards()[firstTurn], deck.getCards()[secondTurn]);
    }
}
