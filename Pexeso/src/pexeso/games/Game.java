/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.games;

import pexeso.players.AbstractPlayer;
import java.io.Serializable;
import java.util.ArrayList;
import pexeso.cards.DeckOfCards;
import pexeso.HeadFrame;
import pexeso.Message;
import pexeso.OneMove;
import pexeso.delegates.MessageDelegate;


/**
 *
 * @author Tomas
 */
public class Game implements Serializable, Runnable{
    
    public static boolean gameInterrupted;
    
    // true - player one's turn
    // false - player two's turn
    protected boolean playerOnTurn = true;
    protected AbstractPlayer player1;
    protected AbstractPlayer player2;
    protected DeckOfCards deck;
    
    protected int uncoveredCards = 0;
    
    protected OneMove newMove;
    protected OneMove lastPlayer1Move;
    protected OneMove lastPlayer2Move;
    
    protected boolean rightMoveByPlayer1;
    protected boolean rightMoveByPlayer2;
    
    protected ArrayList<OneMove> player1Moves = new ArrayList<OneMove>();
    protected ArrayList<OneMove> player2Moves = new ArrayList<OneMove>();
    
    protected transient Message output;
    protected boolean endOfGame;
    

    public Game(AbstractPlayer player1, AbstractPlayer player2, DeckOfCards deck) {
        this.player1 = player1;
        this.player2 = player2;
        this.deck = deck;
        this.lastPlayer1Move = null;
        this.lastPlayer2Move = null;
        if (player1 != null) {
            this.output = new Message((HeadFrame) player1.getDelegate());
        }
        else {
            this.output = new Message((HeadFrame) player2.getDelegate());
        }
    }
    
    
    @Override
    public void run() {
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }

        while (!endOfGame) {
            if (playerOnTurn) {
                newMove = player1.move(lastPlayer1Move, player2Moves, deck.size());
            } 
            else {
                newMove = player2.move(lastPlayer2Move, player1Moves, deck.size());
            }

            if (gameInterrupted) {
                return;
            }
            
            showCards();

            if (newMove.getFirstCardIDNumber() != -1 && 
                    newMove.getSecondCardIDNumber() != -1) {
                compareCards();
            }
            if (uncoveredCards == deck.size()) {
                endGame();
            }
        }
    }
    
    protected void showCards() {
        deck.getCards()[newMove.getFirstCardIDNumber()].showCard();
        deck.getCards()[newMove.getSecondCardIDNumber()].showCard();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Game cant sleep.");
        }
    }
    
    public void endGame() {
        if (player1.getScore() > player2.getScore()) {
            output.setHeadMessage(player1.getName() + " WON!!");
        } 
        else if (player1.getScore() < player2.getScore()){
            output.setHeadMessage(player2.getName() + " WON!!");
        }
        else {
            output.setHeadMessage("DRAW");
        }
        endOfGame = true;
    }
    
    protected void compareCards() {
        if (deck.getCards()[newMove.getFirstCardIDNumber()].equals(deck.getCards()[newMove.getSecondCardIDNumber()])) {
            deck.getCards()[newMove.getFirstCardIDNumber()].hideCard();
            deck.getCards()[newMove.getSecondCardIDNumber()].hideCard();
            uncoveredCards += 2;
            if (playerOnTurn) {
                player1.setScore(player1.getScore() + 10);
                lastPlayer1Move = new OneMove(newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber());
                if (lastPlayer1Move.getFirstCardIDNumber() != -1 && lastPlayer1Move.getSecondCardIDNumber() != -1) {
                    lastPlayer1Move.setFirstCardCompareNumber(deck.getCards()[lastPlayer1Move.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer1Move.setSecondCardCompareNumber(deck.getCards()[lastPlayer1Move.getSecondCardIDNumber()].getCompareNumber());
                    if (!rightMoveByPlayer1) {
                        rightMoveByPlayer1 = true;
                        player1Moves = new ArrayList<OneMove>();
                        player1Moves.add(lastPlayer1Move);
                    }
                    else {
                        player1Moves.add(lastPlayer1Move);
                    }
                }
            }
            else {
                player2.setScore(player2.getScore() + 10);
                lastPlayer2Move = new OneMove(newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber());

                if (lastPlayer2Move.getFirstCardIDNumber() != -1 && lastPlayer2Move.getSecondCardIDNumber() != -1) {
                    lastPlayer2Move.setFirstCardCompareNumber(deck.getCards()[lastPlayer2Move.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer2Move.setSecondCardCompareNumber(deck.getCards()[lastPlayer2Move.getSecondCardIDNumber()].getCompareNumber());
                    if (!rightMoveByPlayer2) {
                        rightMoveByPlayer2 = true;
                        player2Moves = new ArrayList<OneMove>();
                        player2Moves.add(lastPlayer2Move);
                    } else {
                        player2Moves.add(lastPlayer2Move);
                    }
                }
            }
            newMove = null;
        } else {
            if (playerOnTurn) {
                
                lastPlayer1Move = new OneMove(newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber());
                if (lastPlayer1Move.getFirstCardIDNumber() != -1 && lastPlayer1Move.getSecondCardIDNumber() != -1) {
                    lastPlayer1Move.setFirstCardCompareNumber(deck.getCards()[lastPlayer1Move.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer1Move.setSecondCardCompareNumber(deck.getCards()[lastPlayer1Move.getSecondCardIDNumber()].getCompareNumber());
                    
                    if (!rightMoveByPlayer1) {
                        player1Moves = new ArrayList<OneMove>();
                        player1Moves.add(lastPlayer1Move);
                    } else {
                        rightMoveByPlayer1 = false;
                        player1Moves.add(lastPlayer1Move);
                    }
                }
            }
            else {
                player2Moves = new ArrayList<OneMove>();
                lastPlayer2Move = new OneMove(newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber());

                if (lastPlayer2Move.getFirstCardIDNumber() != -1 && lastPlayer2Move.getSecondCardIDNumber() != -1) {
                    lastPlayer2Move.setFirstCardCompareNumber(deck.getCards()[lastPlayer2Move.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer2Move.setSecondCardCompareNumber(deck.getCards()[lastPlayer2Move.getSecondCardIDNumber()].getCompareNumber());
                    if (!rightMoveByPlayer2) {
                        player2Moves = new ArrayList<OneMove>();
                        player2Moves.add(lastPlayer2Move);
                    } else {
                        rightMoveByPlayer2 = false;
                        player2Moves.add(lastPlayer2Move);
                    }
                }
            }
            deck.getCards()[newMove.getFirstCardIDNumber()].turnBack();
            deck.getCards()[newMove.getSecondCardIDNumber()].turnBack();
            changePlayerOnTurn();
            newMove = null;
        }
    }
    
    public void changePlayerOnTurn() {
        if (playerOnTurn) {
            playerOnTurn = false;
            output.setHeadMessage(player2.getName() + "'s turn.");

        } else {
            playerOnTurn = true;
            output.setHeadMessage(player1.getName() + "'s turn.");
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
