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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Trida pro hru z pohledu serveru. Zakladni hra obohacena o komunikaci s
 * klientem.
 *
 * @author Tomas
 */
public class ServerGame extends Game {

    private ObjectOutputStream objOutStream;

    /**
     * Nastavi hrace na tahu na true - zacina server.
     *
     * @param serverPlayer Hrac. (server)
     * @param deck Balicek karet.
     */
    public ServerGame(AbstractPlayer serverPlayer, DeckOfCards deck) {
        super(serverPlayer, null, deck);
        player1OnTurn = true;
    }

    @Override
    public void run() {
        try {
            connectClient();
        } catch (UnknownHostException ex) {
//            output.setErrorMessage("IP not found.");
            closeStreams();
            return;
        } catch (ClassNotFoundException ex) {
//            output.setErrorMessage("Player class not found.");
            closeStreams();
            return;
        } catch (IOException ex) {
            output.setErrorMessage(ex.getMessage());
            closeStreams();
            return;
        }
        try {
            sendGameToClient();
        } catch (IOException ex) {
//            output.setErrorMessage("Can't send game to client.");
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
                    output.setErrorMessage("Can't send my move to client.");
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
            if (serverSock != null) {
                serverSock.close();
            }
        } catch (IOException ex) {
//            output.setErrorMessage("Connection lost.");
            System.out.println("Connection lost.");
        }
    }

    /**
     * Pripoji klienta. Nacte hrace (klienta).
     *
     * @throws UnknownHostException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void connectClient() throws UnknownHostException, ClassNotFoundException, IOException {
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

    /**
     * Odesle hrace (server) a balicek karet klientovi.
     *
     * @throws IOException
     */
    public void sendGameToClient() throws IOException {
        objOutStream.writeObject(player1);
        objOutStream.writeObject(deck);
        if (player1.getName().equals(player2.getName())) {
            player2.setName("Opponent");
        }
        output.setErrorMessage(player2.getName() + " joined.");
    }
}
