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
import javax.swing.JOptionPane;
import pexeso.players.AbstractPlayer;
import pexeso.cards.DeckOfCards;
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

        
        String ipAdress =(String) JOptionPane.showInputDialog(null, 
                "Enter the IP adress of the host.", "Connecting to host...", 
                JOptionPane.PLAIN_MESSAGE, null, null, "127.0.0.1");

        try {
            clientSocket = new Socket(ipAdress, 4444);
            objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objInStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            output.setErrorMessage("Wrong ip adress.");
            return;
        } catch (IOException e) {
            output.setErrorMessage("Can't connect to host.");
            return;
        }

        try {
            objOutStream.writeObject(player1);
        } catch (IOException ex) {
            output.setErrorMessage("IOExp.");
        }
        
        try {
            player2 = (AbstractPlayer) objInStream.readObject();
            player2.setPlayerNumber(2);
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
        
        
        
        
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }
        
        output.setErrorMessage(player2.getName() + " joined.");
        
        while (!endOfGame) {

            if (playerOnTurn) {
                newMove = player1.move(lastPlayer1Move, player2Moves);
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
