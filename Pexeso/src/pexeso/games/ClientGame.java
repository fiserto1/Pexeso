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
    
    private Socket clientSocket;
    private ObjectOutputStream objOutStream;
    private ObjectInputStream objInStream;

    public ClientGame(AbstractPlayer clientPlayer, DeckOfCards deck) {
        super(clientPlayer, null, deck);
        playerOnTurn = false;
    }

    @Override
    public void run() {
        
        try {
            connectToServer();
        } catch (UnknownHostException e) {
            output.setErrorMessage("Wrong ip adress.");
            return;
        } catch (IOException e) {
            output.setErrorMessage("Can't connect to host.");
            return;
        }
        
        try {
            loadFromServer();
        } catch (ClassNotFoundException ex) {
            output.setErrorMessage("Class not found. " + ex.getMessage());
            return;
        } catch (IOException ex) {
            output.setErrorMessage("Can't load data from server.");
            return;
        }
        
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }
        
        while (!endOfGame) {

            if (playerOnTurn) {
                newMove = player1.move(lastPlayer1Move, player2Moves);
                try {
                    objOutStream.writeObject(newMove);
                } catch (IOException ex) {
                    output.setErrorMessage("Can't send my move to server.");
                    return;
                }
            } else {
                try {
                    newMove = (OneMove) objInStream.readObject();
                } catch (IOException ex) {
                    output.setErrorMessage("Can't read move from server.");
                    return;
                } catch (ClassNotFoundException ex) {
                    output.setErrorMessage("OneMove class not found.");
                    return;
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
            output.setErrorMessage("Can't close streams or socket.");
        }
    }
    
    private void connectToServer() throws UnknownHostException, IOException {
        String ipAdress = (String) JOptionPane.showInputDialog(null,
                "Enter the IP adress of the host.", "Connecting to host...",
                JOptionPane.PLAIN_MESSAGE, null, null, "127.0.0.1");
        
        clientSocket = new Socket(ipAdress, 4444);
        objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objInStream = new ObjectInputStream(clientSocket.getInputStream());
        objOutStream.writeObject(player1);
    }
    
    private void loadFromServer() throws ClassNotFoundException, IOException {
        player2 = (AbstractPlayer) objInStream.readObject();
        player2.setPlayerNumber(2);
        player2.setDelegate(player1.getDelegate());
        deck = (DeckOfCards) objInStream.readObject();
        player1.getDelegate().refreshDeck(deck);
        
        if (player1.getName().equals(player2.getName())) {
            player2.setName("Opponent");
        }
        output.setErrorMessage(player2.getName() + " joined.");
    }
}
