/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.games;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pexeso.players.AbstractPlayer;
import pexeso.cards.CardAL;
import pexeso.cards.DeckOfCards;
import pexeso.players.HumanPlayer;
import pexeso.OneMove;

/**
 *
 * @author Tomas
 */
public class ServerGame extends Game {
    

    public ServerGame(AbstractPlayer serverPlayer, DeckOfCards deck) {
        super(serverPlayer, null, deck);
        playerOnTurn = true;
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
            player2 = (AbstractPlayer) objInStream.readObject();
            player2.setDelegate(player1.getDelegate());
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            objOutStream.writeObject(player1);
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            objOutStream.writeObject(deck.getOnlineCards());
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (player1.getName().equals(player2.getName())) {
            player2.setName("Opponent");
        }
        player2.setPlayerNumber(2);
        player1.setName(player1.getName());
        player1.setScore(player1.getScore());
        player1.setAvatar(player1.getAvatar());
        player2.setName(player2.getName());
        player2.setScore(player2.getScore());
        player2.setAvatar(player2.getAvatar());
        
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }
        
        System.out.println(player2.getName() + " joined.");
        
        while (!endOfGame) {

            if (playerOnTurn) {
                CardAL listener = new CardAL();
                if (player2 instanceof HumanPlayer) {
                    CardAL.setMoveCompleted(false);
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].addActionListener(listener);
                    }
                }
                
                newMove = player1.move(lastPlayer1Move, player2Moves);
                
                if (player1 instanceof HumanPlayer) {
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].removeActionListener(listener);
                    }
                }
                
                try {
                    int[] myOnlineMove = {newMove.getFirstCardIDNumber(), newMove.getSecondCardIDNumber()};
                    objOutStream.writeObject(myOnlineMove);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    int[] oppOnlineMove = (int[]) objInStream.readObject();
                    newMove = new OneMove(oppOnlineMove[0], oppOnlineMove[1]);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            showCards();

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
}
