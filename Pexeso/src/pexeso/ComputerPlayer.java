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
    public OneMove move(OneMove myLastMove, OneMove oppMove) {
        Random rnd = new Random();
        int firstTurn, secondTurn;
        do {
            firstTurn = rnd.nextInt(DeckOfCards.NUMBER_OF_CARDS - 1);
            secondTurn = rnd.nextInt(DeckOfCards.NUMBER_OF_CARDS - 1);
        } while (firstTurn == secondTurn);
        
        return new OneMove(firstTurn, secondTurn);
    }
}
