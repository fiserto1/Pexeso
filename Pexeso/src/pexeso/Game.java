/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import pexeso.delegates.MessageDelegate;


/**
 *
 * @author Tomas
 */
public class Game implements Serializable, Runnable{
    // true - player one's turn
    // false - player two's turn
    public static boolean gameInterrupted;
    
    private boolean playerOnTurn = true;
    private AbstractPlayer player1;
    private AbstractPlayer player2;

    private DeckOfCards deck;
    private int uncoveredCards = 0;
    private OneMove newMove;
    private OneMove lastPlayer1Move;
    private OneMove lastPlayer2Move;
    
    private transient Message output;
    private boolean endOfGame;
    

    public Game(AbstractPlayer player1, AbstractPlayer player2, DeckOfCards deck) {
        this.player1 = player1;
        this.player2 = player2;
        this.deck = deck;
        this.lastPlayer1Move = null;
        this.lastPlayer2Move = null;
        this.output = new Message((HeadFrame) player1.getDelegate());
        
    }
    
    
    @Override
    public void run() {
        player1.setName(player1.name);
        player1.setScore(player1.score);
        player1.setAvatar(player1.avatar);
        player2.setName(player2.name);
        player2.setScore(player2.score);
        player2.setAvatar(player2.avatar);
        if (playerOnTurn) {
            output.setHeadMessage("Player One's turn.");
        } else {
            output.setHeadMessage("Player Two's turn.");
        }

        while (!endOfGame) {

            if (playerOnTurn) {
                CardAL listener = new CardAL();
                if (player1 instanceof HumanPlayer) {
                    CardAL.setMoveCompleted(false);
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].addActionListener(listener);
                    }
                }
                
                newMove = player1.move(lastPlayer1Move, lastPlayer2Move);
                lastPlayer1Move = newMove;
                if (newMove.getFirstCardIDNumber() != -1 && newMove.getSecondCardIDNumber() != -1) {
                    lastPlayer1Move.setFirstCardCompareNumber(deck.getCards()[newMove.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer1Move.setSecondCardCompareNumber(deck.getCards()[newMove.getSecondCardIDNumber()].getCompareNumber());
                }
                
                if (player1 instanceof HumanPlayer) {
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].removeActionListener(listener);
                    }
                }
                
            } else {
                CardAL listener = new CardAL();
                if (player2 instanceof HumanPlayer) {
                    CardAL.setMoveCompleted(false);
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].addActionListener(listener);
                    }
                }
                
                newMove = player2.move(lastPlayer2Move, lastPlayer1Move);
                lastPlayer2Move = newMove;
                
                if (newMove.getFirstCardIDNumber() != -1 && newMove.getSecondCardIDNumber() != -1) {
                    lastPlayer2Move.setFirstCardCompareNumber(deck.getCards()[newMove.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer2Move.setSecondCardCompareNumber(deck.getCards()[newMove.getSecondCardIDNumber()].getCompareNumber());
                }
                
                if (player2 instanceof HumanPlayer) {
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].removeActionListener(listener);
                    }
                }
            }

            if (gameInterrupted) {
                return;
            }
            //card show
            //card show
            deck.getCards()[newMove.getFirstCardIDNumber()].setText("");
            Image newImage = deck.getCards()[newMove.getFirstCardIDNumber()].getCardImage().getImage().getScaledInstance(
                    deck.getCards()[newMove.getFirstCardIDNumber()].getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
            deck.getCards()[newMove.getFirstCardIDNumber()].setIcon(new ImageIcon(newImage));

            deck.getCards()[newMove.getSecondCardIDNumber()].setText("");
            newImage = deck.getCards()[newMove.getSecondCardIDNumber()].getCardImage().getImage().getScaledInstance(
                    deck.getCards()[newMove.getSecondCardIDNumber()].getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
            deck.getCards()[newMove.getSecondCardIDNumber()].setIcon(new ImageIcon(newImage));
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Game cant sleep.");
            }

            if (newMove.getFirstCardIDNumber() != -1 && newMove.getSecondCardIDNumber() != -1) {
                compareCards();
            }
            if (uncoveredCards == DeckOfCards.NUMBER_OF_CARDS) {
                endGame();
            }
        }
    }
    
    public void endGame() {
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + " WON!!");
        } else {
            output.setHeadMessage(player2.getName() + " WON!!");
        }
        endOfGame = true;
    }
    
    private void compareCards() {
        if (deck.getCards()[newMove.getFirstCardIDNumber()].getCompareNumber() == deck.getCards()[newMove.getSecondCardIDNumber()].getCompareNumber()) {
            deck.getCards()[newMove.getFirstCardIDNumber()].setVisible(false);
            deck.getCards()[newMove.getSecondCardIDNumber()].setVisible(false);
            uncoveredCards += 2;
            if (playerOnTurn) {
                player1.setScore(player1.getScore() + 10);
            }
            else {
                player2.setScore(player2.getScore() + 10);
            }
            newMove = null;
            CardAL.unmarkCards();
        } else {
            deck.getCards()[newMove.getFirstCardIDNumber()].setText("CARD");
            deck.getCards()[newMove.getFirstCardIDNumber()].setIcon(null);
            deck.getCards()[newMove.getSecondCardIDNumber()].setText("CARD");
            deck.getCards()[newMove.getSecondCardIDNumber()].setIcon(null);
            changePlayerOnTurn();
            newMove = null;
            CardAL.unmarkCards();
        }
    }
    
    public void changePlayerOnTurn() {
        if (playerOnTurn) {
            playerOnTurn = false;
            output.setHeadMessage("Player Two's turn.");

        } else {
            playerOnTurn = true;
            output.setHeadMessage("Player One's turn.");
        }
    }
    
    public boolean isPlayerOnTurn() {
        return playerOnTurn;
    }

    public int getUncoveredCards() {
        return uncoveredCards;
    }

    public DeckOfCards getDeck() {
        return deck;
    }

    public void setDeck(DeckOfCards deck) {
        this.deck = deck;
    }

    public AbstractPlayer getPlayer1() {
        return player1;
    }

    public AbstractPlayer getPlayer2() {
        return player2;
    }

    public OneMove getNewMove() {
        return newMove;
    }

    public Message getOutput() {
        return output;
    }

    public boolean isEndOfGame() {
        return endOfGame;
    }

    public void setOutputDelegate(MessageDelegate delegate) {
        this.output = new Message(delegate);
    }
}
