/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.games;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
            output.setHeadMessage(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            output.setErrorMessage("Fuck.");
        }
        try {
            serverSock = new ServerSocket(4444);
        } catch (IOException e) {
            output.setErrorMessage("Could not listen on port: 4444.");
        }

        Socket clientSock = null;
        try {
            clientSock = serverSock.accept();
        } catch (IOException e) {
            output.setErrorMessage("Accept failed.");
        }

        ObjectOutputStream objOutStream = null;
        ObjectInputStream objInStream = null;
        try {
            objOutStream = new ObjectOutputStream(clientSock.getOutputStream());
            objInStream = new ObjectInputStream(clientSock.getInputStream());
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        }
        
        try {
            player2 = (AbstractPlayer) objInStream.readObject();
            player2.setDelegate(player1.getDelegate());
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        } catch (ClassNotFoundException ex) {
            output.setErrorMessage("Class not found.");
        }
        
        try {
            objOutStream.writeObject(player1);
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        }
        try {
            objOutStream.writeObject(deck.getOnlineCards());
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
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
        
        output.setErrorMessage(player2.getName() + " joined.");
        
        while (!endOfGame) {

            if (playerOnTurn) {
                CardAL listener = new CardAL();
                //add click listener
                if (player2 instanceof HumanPlayer) {
                    CardAL.setMoveCompleted(false);
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].addActionListener(listener);
                    }
                }
                
                newMove = player1.move(lastPlayer1Move, player2Moves);
                
                //remove click listener
                if (player1 instanceof HumanPlayer) {
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].removeActionListener(listener);
                    }
                }
                
                try {
                    int[] myOnlineMove = {newMove.getFirstCardIDNumber(),
                        newMove.getSecondCardIDNumber()};
                    
                    objOutStream.writeObject(myOnlineMove);
                } catch (IOException ex) {
                    output.setErrorMessage("IOExp.");
                }
            } else {
                try {
                    int[] oppOnlineMove = (int[]) objInStream.readObject();
                    newMove = new OneMove(oppOnlineMove[0], oppOnlineMove[1]);
                } catch (IOException ex) {
                    output.setErrorMessage("IOExp.");
                } catch (ClassNotFoundException ex) {
                    output.setErrorMessage("Opp move Class not found.");
                }
            }

            showCards();

            if (newMove.getFirstCardIDNumber()!= -1 &&
                    newMove.getSecondCardIDNumber() != -1) {
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
            output.setErrorMessage("Cant close streams.");
        }
    }
}
