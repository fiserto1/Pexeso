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
import pexeso.players.AbstractPlayer;
import pexeso.cards.DeckOfCards;
import pexeso.OneMove;

/**
 *
 * @author Tomas
 */
public class ServerGame extends Game {
    
    private ServerSocket serverSock;
    private Socket clientSock;
    private ObjectOutputStream objOutStream;
    private ObjectInputStream objInStream;

    public ServerGame(AbstractPlayer serverPlayer, DeckOfCards deck) {
        super(serverPlayer, null, deck);
        playerOnTurn = true;
    }
    
    
    @Override
    public void run() {
        try {
            connectClient();
        } catch (UnknownHostException ex) {
            output.setErrorMessage("IP not found.");
            return;
        } catch (ClassNotFoundException ex) {
            output.setErrorMessage("Player class not found.");
            return;
        } catch (IOException ex) {
            output.setErrorMessage(ex.getMessage());
            return;
        }
        try {
            sendGameToClient();
        } catch (IOException ex) {
            output.setErrorMessage("Can't send game to client.");
            return;
        }
        
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }
        
        while (!endOfGame) {

            if (playerOnTurn) {
                
                newMove = player1.move(lastPlayer1Move, player2Moves, deck.getCards().length);
                
                try {
                    objOutStream.writeObject(newMove);
                } catch (IOException ex) {
                    output.setErrorMessage("Can't send my move to client.");
                    return;
                }
            } else {
                try {
                    newMove = (OneMove) objInStream.readObject();
                } catch (IOException ex) {
                    output.setErrorMessage("Can't read move from client.");
                    return;
                } catch (ClassNotFoundException ex) {
                    output.setErrorMessage("OneMove class not found.");
                    return;
                }
            }

            if (gameInterrupted) {
                return;
            }
            showCards();

            if (newMove.getFirstCardIDNumber()!= -1 &&
                    newMove.getSecondCardIDNumber() != -1) {
                compareCards();
            }
            if (uncoveredCards == deck.getCards().length) {
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
    
    private void connectClient() throws UnknownHostException, ClassNotFoundException, IOException {
        output.setHeadMessage("Your IP adress: "
                + InetAddress.getLocalHost().getHostAddress());
        serverSock = new ServerSocket(4444);
        clientSock = serverSock.accept();
        objOutStream = new ObjectOutputStream(clientSock.getOutputStream());
        objInStream = new ObjectInputStream(clientSock.getInputStream());
        player2 = (AbstractPlayer) objInStream.readObject();
        player2.setPlayerNumber(2);
        player2.setDelegate(player1.getDelegate());
    }
    
    private void sendGameToClient() throws IOException {
        objOutStream.writeObject(player1);
        objOutStream.writeObject(deck);
        if (player1.getName().equals(player2.getName())) {
            player2.setName("Opponent");
        }
        output.setErrorMessage(player2.getName() + " joined.");
    }
}
