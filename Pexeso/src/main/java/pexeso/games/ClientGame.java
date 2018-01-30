/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.games;

import pexeso.cards.DeckOfCards;
import pexeso.players.AbstractPlayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Trida pro hru z pohledu klienta. Zakladni hra obohacena o komunikaci se
 * serverem.
 *
 * @author Tomas
 */
public class ClientGame extends Game {

    private ObjectOutputStream objOutStream;

    /**
     * Nastavi hrace na tahu na false. (hru zacina server)
     *
     * @param clientPlayer Hrac. (klient)
     * @param deck Balicek karet.
     */
    public ClientGame(AbstractPlayer clientPlayer, DeckOfCards deck) {
        super(clientPlayer, null, deck);
        player1OnTurn = false;
    }

    @Override
    public void run() {

        try {
            connectToServer();
        } catch (UnknownHostException e) {
            output.setErrorMessage("Wrong ip adress.");
            closeStreams();
            return;
        } catch (IOException e) {
            output.setErrorMessage("Can't connect to host.");
            closeStreams();
            return;
        }

        try {
            loadFromServer();
        } catch (ClassNotFoundException ex) {
//            output.setErrorMessage("Class not found. " + ex.getMessage());
            closeStreams();
            return;
        } catch (IOException ex) {
            output.setErrorMessage("Can't load data from server.");
            closeStreams();
            return;
        }

        if (player1OnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }

        while (!endOfGame) {

            if (player1OnTurn) {
                newMove = player1.move(lastPlayer1Move, player2Moves, deck.getCards().length);
                try {
                    objOutStream.writeObject(newMove);
                } catch (IOException ex) {
                    output.setErrorMessage("Can't send my move to server.");
                    closeStreams();
                    return;
                }
            } else {
                try {
                    newMove = (OneMove) objInStream.readObject();
                } catch (IOException ex) {
                    output.setErrorMessage("Opponent left.");
                    closeStreams();
                    return;
                } catch (ClassNotFoundException ex) {
//                    output.setErrorMessage("OneMove class not found.");
                    closeStreams();
                    return;
                }
            }

            if (gameInterrupted) {
                closeStreams();
                return;
            }
            evaluateMove();
        }

        closeStreams();
    }

    /**
     * Zavre proudy.
     */
    public void closeStreams() {
        try {
            if (objInStream != null) {
                objInStream.close();
            }
            if (objOutStream != null) {
                objOutStream.close();
            }
            if (clientSock != null) {
                clientSock.close();
            }
        } catch (IOException ex) {
//            output.setErrorMessage("Connection lost.");
        }
    }

    /**
     * Pripoji se k serveru. Posle serveru hrace(klienta).
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connectToServer() throws UnknownHostException, IOException {
        clientSock = new Socket(hostIPAddress, 4444);
        objOutStream = new ObjectOutputStream(clientSock.getOutputStream());
        objInStream = new ObjectInputStream(clientSock.getInputStream());
        objOutStream.writeObject(player1);
    }

    /**
     * Nacte hrace a balicek karet ze serveru.
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void loadFromServer() throws ClassNotFoundException, IOException {
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
