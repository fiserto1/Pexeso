/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.online;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import pexeso.AbstractPlayer;
import pexeso.CardAL;
import pexeso.DeckOfCards;
import static pexeso.Game.gameInterrupted;
import pexeso.HeadFrame;
import pexeso.HumanPlayer;
import pexeso.Message;
import pexeso.OneMove;
import pexeso.delegates.MessageDelegate;

/**
 *
 * @author Tomas
 */
public class ClientGame implements Runnable {
    

    private AbstractPlayer serverPlayer;
    private AbstractPlayer clientPlayer;
    private DeckOfCards deck;
    
    private boolean playerOnTurn = false;
    
    private int uncoveredCards = 0;
    private OneMove newMove;

    private transient Message output;
    private boolean endOfGame;

    public ClientGame( AbstractPlayer clientPlayer, DeckOfCards deck) {
        this.serverPlayer = null;
        this.clientPlayer = clientPlayer;
        this.deck = deck;
        this.output = new Message((HeadFrame) clientPlayer.getDelegate());
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
            System.err.println("Don't know about host: localhost.");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
        }

        
        
        try {
            objOutStream.writeObject(clientPlayer);
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            serverPlayer = (AbstractPlayer) objInStream.readObject();
            serverPlayer.setDelegate(clientPlayer.getDelegate());
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int[] cc = (int[]) objInStream.readObject();
            deck.recreateDeckForOnlineGame(cc);
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (serverPlayer.getName().equals(clientPlayer.getName())) {
            serverPlayer.setName("Opponent");
        }
        serverPlayer.setPlayerNumber(2);
        serverPlayer.setName(serverPlayer.getName());
        serverPlayer.setScore(serverPlayer.getScore());
        serverPlayer.setAvatar(serverPlayer.getAvatar());
        clientPlayer.setName(clientPlayer.getName());
        clientPlayer.setScore(clientPlayer.getScore());
        clientPlayer.setAvatar(clientPlayer.getAvatar());
        
        if (playerOnTurn) {
            output.setHeadMessage(clientPlayer.getName() + "'s turn.");
        } else {
            output.setHeadMessage(serverPlayer.getName() + "'s turn.");
        }
        
        System.out.println(serverPlayer.getName() + " joined.");
        
        while (!endOfGame) {

            if (playerOnTurn) {
                newMove = clientPlayer.move(deck);
                try {
//                    System.out.println(newMove.getFirstCard().getIdNumber() + "," + newMove.getSecondCard().getIdNumber());
                    int[] onlineMove = {newMove.getFirstCard().getIdNumber(), newMove.getSecondCard().getIdNumber()};
                    objOutStream.writeObject(onlineMove);
//                    objOutStream.writeObject(newMove);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
//                newMove = serverPlayer.move(deck);
                try {
                    int[] playa = (int[]) objInStream.readObject();
                    newMove = new OneMove(deck.getCards()[playa[0]], deck.getCards()[playa[1]]);
//                    newMove = (OneMove) objInStream.readObject();
//                    System.out.println("GOT HIM, im client" + playa[0] + "," + playa[1]);
                } catch (IOException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //card show
            newMove.getFirstCard().setText("");
            Image newImage = newMove.getFirstCard().getCardImage().getImage().getScaledInstance(
                    newMove.getFirstCard().getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
            newMove.getFirstCard().setIcon(new ImageIcon(newImage));

            newMove.getSecondCard().setText("");
            newImage = newMove.getSecondCard().getCardImage().getImage().getScaledInstance(
                    newMove.getSecondCard().getCardImage().getIconWidth() / 2, -1, Image.SCALE_SMOOTH);
            newMove.getSecondCard().setIcon(new ImageIcon(newImage));
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Game cant sleep.");
            }

            if (newMove.getFirstCard() != null && newMove.getSecondCard() != null) {
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
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void endGame() {
        if (playerOnTurn) {
            output.setHeadMessage(clientPlayer.getName() + " WON!!");
        } else {
            output.setHeadMessage(serverPlayer.getName() + " WON!!");
        }
        endOfGame = true;
    }

    private void compareCards() {
        if (newMove.getFirstCard().getCompareNumber() == newMove.getSecondCard().getCompareNumber()) {
            newMove.getFirstCard().setVisible(false);
            newMove.getSecondCard().setVisible(false);
            uncoveredCards += 2;
            if (playerOnTurn) {
                clientPlayer.setScore(clientPlayer.getScore() + 10);
            } else {
                serverPlayer.setScore(serverPlayer.getScore() + 10);
            }
            newMove = null;
            CardAL.unmarkCards();
        } else {
            newMove.getFirstCard().setText("CARD");
            newMove.getFirstCard().setIcon(null);
            newMove.getSecondCard().setText("CARD");
            newMove.getSecondCard().setIcon(null);
            changePlayerOnTurn();
            newMove = null;
            CardAL.unmarkCards();
        }
    }

    public void changePlayerOnTurn() {
        if (playerOnTurn) {
            playerOnTurn = false;
            output.setHeadMessage(serverPlayer.getName() + "'s turn.");

        } else {
            playerOnTurn = true;
            output.setHeadMessage(clientPlayer.getName() + "'s turn.");
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
        return clientPlayer;
    }

    public AbstractPlayer getPlayer2() {
        return serverPlayer;
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
