/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author Tomas
 */
public class CardAL implements ActionListener{

    private Timer showTimer = new Timer(1000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            compareCards();
            if (game.getUncoveredCards() == DeckOfCards.NUMBER_OF_CARDS) {
                game.endGame();
            }
            showTimer.stop();
        }
    });
    
    private Game game;
    private Card card;
//    static boolean ready;
    private static Card firstClickedCard;
    private static Card secondClickedCard;
    
    public CardAL(Card card, Game game) {
        this.card = card;
        this.game = game;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (card.getIcon() == null) {
            if (firstClickedCard == null) {
                card.setText("");
                Image newImage = card.getCardImage().getImage().getScaledInstance(
                        card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(newImage));
                firstClickedCard = card;
            }
            else if (secondClickedCard == null) {
                card.setText("");
                Image newImage = card.getCardImage().getImage().getScaledInstance(
                        card.getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(newImage));
                secondClickedCard = card;
                showTimer.start();
//                ready = true;
            }
        }
    }
    
//    public static OneMove getMove() {
//        OneMove move = new OneMove(firstClickedCard, secondClickedCard);
//        firstClickedCard = null;
//        secondClickedCard = null;
//        return move;
//    }

    private void compareCards() {
        if (firstClickedCard.equals(secondClickedCard)) {
            firstClickedCard.setVisible(false);
            secondClickedCard.setVisible(false);
            game.setUncoveredCards(game.getUncoveredCards() + 2);
            game.setScore();
            firstClickedCard = null;
            secondClickedCard = null;
        } else {
            firstClickedCard.setText("CARD");
            firstClickedCard.setIcon(null);
            secondClickedCard.setText("CARD");
            secondClickedCard.setIcon(null);
            game.changePlayerOnTurn();
            firstClickedCard = null;
            secondClickedCard = null;
        }
    }
    
}
