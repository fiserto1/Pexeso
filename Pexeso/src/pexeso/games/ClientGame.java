/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.games;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class ClientGame extends Game {

    public ClientGame(AbstractPlayer clientPlayer, DeckOfCards deck) {
        super(clientPlayer, null, deck);
        playerOnTurn = false;
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        ObjectOutputStream objOutStream = null;
        ObjectInputStream objInStream = null;

        try {
            clientSocket = new Socket("127.0.0.1", 4444);
            objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objInStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            output.setErrorMessage("Don't know about host: localhost.");
        } catch (IOException e) {
            output.setErrorMessage("Couldn't get I/O for the connection to: localhost.");
        }

        try {
            objOutStream.writeObject(player1);
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        }
        
        try {
            player2 = (AbstractPlayer) objInStream.readObject();
            player2.setDelegate(player1.getDelegate());
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        } catch (ClassNotFoundException ex) {
            output.setErrorMessage("Opp player class not found.");
        }
        try {
            int[] cc = (int[]) objInStream.readObject();
            deck.recreateDeckForOnlineGame(cc);
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        } catch (ClassNotFoundException ex) {
            output.setErrorMessage("Deck class not found.");
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
                //add click listeners
                if (player1 instanceof HumanPlayer) {
                    CardAL.setMoveCompleted(false);
                    for (int i = 0; i < deck.getCards().length; i++) {
                        deck.getCards()[i].addActionListener(listener);
                    }
                }
                newMove = player1.move(lastPlayer1Move, player2Moves);
                
                //remove click listeners
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
                    output.setErrorMessage("Opp move class not found.");
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
            clientSocket.close();
        } catch (IOException ex) {
            output.setErrorMessage("Cant close streams.");
        }
    }
}
