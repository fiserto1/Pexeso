/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.games;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import pexeso.players.AbstractPlayer;
import pexeso.cards.CardAL;
import pexeso.cards.DeckOfCards;
import pexeso.HeadFrame;
import pexeso.players.HumanPlayer;
import pexeso.Message;
import pexeso.OneMove;
import pexeso.delegates.MessageDelegate;

/**
 *
 * @author Tomas
 */
public class ServerGame implements Runnable {
    
    private AbstractPlayer serverPlayer;
    private AbstractPlayer clientPlayer;
    private DeckOfCards deck;
    
    private boolean playerOnTurn = true;
    
    private int uncoveredCards = 0;
    private OneMove newMove;

    private transient Message output;
    private boolean endOfGame;
    
    private OneMove lastPlayer1Move;
    private OneMove lastPlayer2Move;
    
    private boolean rightMoveByPlayer1;
    private boolean rightMoveByPlayer2;
    
    private ArrayList<OneMove> player1Moves = new ArrayList<OneMove>();
    private ArrayList<OneMove> player2Moves = new ArrayList<OneMove>();

    public ServerGame(AbstractPlayer serverPlayer, DeckOfCards deck) {
        this.serverPlayer = serverPlayer;
        this.clientPlayer = null;
        this.deck = deck;
        this.output = new Message((HeadFrame) serverPlayer.getDelegate());
        this.lastPlayer1Move = null;
        this.lastPlayer2Move = null;
    }
    
    
    @Override
    public void run() {
        ServerSocket serverSock = null;
        try {
            serverSock = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
        }

        Socket clientSock = null;
        try {
            clientSock = serverSock.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
        }

        ObjectOutputStream objOutStream = null;
        ObjectInputStream objInStream = null;
        try {
            objOutStream = new ObjectOutputStream(clientSock.getOutputStream());
            objInStream = new ObjectInputStream(clientSock.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        try {
            clientPlayer = (AbstractPlayer) objInStream.readObject();
            clientPlayer.setDelegate(serverPlayer.getDelegate());
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            objOutStream.writeObject(serverPlayer);
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            objOutStream.writeObject(deck.getOnlineCards());
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (serverPlayer.getName().equals(clientPlayer.getName())) {
            clientPlayer.setName("Opponent");
        }
        clientPlayer.setPlayerNumber(2);
        serverPlayer.setName(serverPlayer.getName());
        serverPlayer.setScore(serverPlayer.getScore());
        serverPlayer.setAvatar(serverPlayer.getAvatar());
        clientPlayer.setName(clientPlayer.getName());
        clientPlayer.setScore(clientPlayer.getScore());
        clientPlayer.setAvatar(clientPlayer.getAvatar());
        
        if (playerOnTurn) {
            output.setHeadMessage(serverPlayer.getName() + "'s turn.");
        } else {
            output.setHeadMessage(clientPlayer.getName() + "'s turn.");
        }
        
        System.out.println(clientPlayer.getName() + " joined.");
        
        while (!endOfGame) {

            if (playerOnTurn) {
                CardAL listener = new CardAL();
                if (clientPlayer instanceof HumanPlayer) {
                    CardAL.setMoveCompleted(false);
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].addActionListener(listener);
                    }
                }
                
                newMove = serverPlayer.move(lastPlayer1Move, player2Moves);
//                lastPlayer1Move = newMove;
//                
//                if (newMove.getFirstCardIDNumber() != -1 && newMove.getSecondCardIDNumber() != -1) {
//                    lastPlayer2Move.setFirstCardCompareNumber(deck.getCards()[newMove.getFirstCardIDNumber()].getCompareNumber());
//                    lastPlayer2Move.setSecondCardCompareNumber(deck.getCards()[newMove.getSecondCardIDNumber()].getCompareNumber());
//                }
                
                if (serverPlayer instanceof HumanPlayer) {
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].removeActionListener(listener);
                    }
                }
                
                try {
//                    objOutStream.writeObject(newMove);
//                    System.out.println(newMove.getFirstCard().getIdNumber() + "," + newMove.getSecondCard().getIdNumber());
                    int[] onlineMove = {newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber()};
                    objOutStream.writeObject(onlineMove);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
//                newMove = clientPlayer.move(deck);
                try {
//                    newMove = (OneMove) objInStream.readObject();
//                    OneMove playa = (OneMove) objInStream.readObject();
                    int[] playa = (int[]) objInStream.readObject();
                    newMove = new OneMove(playa[0], playa[1]);
//                    lastPlayer2Move = newMove;
//                    System.out.println("GOT HIM, im server" + playa[0] + "," + playa[1]);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

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

            if (newMove.getFirstCardIDNumber()!= -1 && newMove.getSecondCardIDNumber() != -1) {
                compareCards();
            }
            if (uncoveredCards == DeckOfCards.NUMBER_OF_CARDS) {
                endGame();
            }
        }
        
        try {
            objOutStream.close();
            objInStream.close();
            clientSock.close();
            serverSock.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void endGame() {
        if (playerOnTurn) {
            output.setHeadMessage(serverPlayer.getName() + " WON!!");
        } else {
            output.setHeadMessage(clientPlayer.getName() + " WON!!");
        }
        endOfGame = true;
    }

    private void compareCards() {
        if (deck.getCards()[newMove.getFirstCardIDNumber()].getCompareNumber() == deck.getCards()[newMove.getSecondCardIDNumber()].getCompareNumber()) {
            deck.getCards()[newMove.getFirstCardIDNumber()].setVisible(false);
            deck.getCards()[newMove.getSecondCardIDNumber()].setVisible(false);
            uncoveredCards += 2;
            if (playerOnTurn) {
                serverPlayer.setScore(serverPlayer.getScore() + 10);
                lastPlayer1Move = new OneMove(newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber());
                if (lastPlayer1Move.getFirstCardIDNumber() != -1 && lastPlayer1Move.getSecondCardIDNumber() != -1) {
                    lastPlayer1Move.setFirstCardCompareNumber(deck.getCards()[lastPlayer1Move.getFirstCardIDNumber()].getCompareNumber());
                    lastPlayer1Move.setSecondCardCompareNumber(deck.getCards()[lastPlayer1Move.getSecondCardIDNumber()].getCompareNumber());
                    if (!rightMoveByPlayer1) {
                        rightMoveByPlayer1 = true;
                        player1Moves = new ArrayList<OneMove>();
                        player1Moves.add(lastPlayer1Move);
                    } else {
                        player1Moves.add(lastPlayer1Move);
                    }
                }
            } else {
                clientPlayer.setScore(clientPlayer.getScore() + 10);
                
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
            CardAL.unmarkCards();
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
            } else {
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
            output.setHeadMessage(clientPlayer.getName() + "'s turn.");

        } else {
            playerOnTurn = true;
            output.setHeadMessage(serverPlayer.getName() + "'s turn.");
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
        return serverPlayer;
    }

    public AbstractPlayer getPlayer2() {
        return clientPlayer;
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
