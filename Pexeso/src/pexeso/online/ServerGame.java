/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.online;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import pexeso.AbstractPlayer;
import pexeso.CardAL;
import pexeso.DeckOfCards;
import pexeso.HeadFrame;
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

    public ServerGame(AbstractPlayer serverPlayer, DeckOfCards deck) {
        this.serverPlayer = serverPlayer;
        this.clientPlayer = null;
        this.deck = deck;
        this.output = new Message((HeadFrame) serverPlayer.getDelegate());
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
                newMove = serverPlayer.move(deck);
                try {
//                    objOutStream.writeObject(newMove);
//                    System.out.println(newMove.getFirstCard().getIdNumber() + "," + newMove.getSecondCard().getIdNumber());
                    int[] onlineMove = {newMove.getFirstCard().getIdNumber(), newMove.getSecondCard().getIdNumber()};
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
                    newMove = new OneMove(deck.getCards()[playa[0]], deck.getCards()[playa[1]]);
//                    System.out.println("GOT HIM, im server" + playa[0] + "," + playa[1]);
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
        if (newMove.getFirstCard().getCompareNumber() == newMove.getSecondCard().getCompareNumber()) {
            newMove.getFirstCard().setVisible(false);
            newMove.getSecondCard().setVisible(false);
            uncoveredCards += 2;
            if (playerOnTurn) {
                serverPlayer.setScore(serverPlayer.getScore() + 10);
            } else {
                clientPlayer.setScore(clientPlayer.getScore() + 10);
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
