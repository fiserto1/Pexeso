/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.Timer;


/**
 *
 * @author Tomas
 */
public class Game implements Serializable {
    // true - player one's turn
    // false - player two's turn
    private boolean playerOnTurn = true;
    private AbstractPlayer player1;
    private AbstractPlayer player2;
//    private HeadFrame frame;
    private DeckOfCards deck;
    private int uncoveredCards = 0;
    private OneMove newMove;
    
    private Message output;
    
    private Timer showTimer = new Timer(500, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("ShowTime bezi.");
            showTimer.stop();
            compareCards();
            if (uncoveredCards == DeckOfCards.NUMBER_OF_CARDS) {
                endGame();
            }
            else {
                checkTimer.start();
            }
        }
    });
    private Timer checkTimer = new Timer(50, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("CheckTimerBezi");
            if (playerOnTurn) {
                newMove = null;
                newMove = player1.move(deck);
            }
            else {
                newMove = null;
                newMove = player2.move(deck);
            }
            if (newMove != null) {
                checkTimer.stop();
                showTimer.start();
            }
        }
    });
    
    

    public void changePlayerOnTurn() {
        if(playerOnTurn) {
            playerOnTurn = false;
            output.setHeadMessage("Player Two's turn.");
            
        }
        else {
            playerOnTurn = true;
            output.setHeadMessage("Player One's turn.");
        }
    }
    
    //COUNTDOWN
//    private int countdown = 21;
//    private Timer timer;

    public Game(AbstractPlayer player1, AbstractPlayer player2, DeckOfCards deck) {
        this.player1 = player1;
        this.player2 = player2;
        this.deck = deck;
        this.output = new Message((HeadFrame) player1.getDelegate());
        
    }
    
    
    public void playGame() {
        player1.setName(player1.name);
        player1.setScore(player1.score);
        player1.setAvatar(player1.avatar);
        player2.setName(player2.name);
        player2.setScore(player2.score);
        player2.setAvatar(player2.avatar);
        if (playerOnTurn) {
            output.setHeadMessage("Player One's turn.");
        }
        else {
            output.setHeadMessage("Player Two's turn.");
        }
        checkTimer.start();
        
        //COUNTDOWN
//        timer = new Timer(1000, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                countdown--;
//                refreshCountDown();
//                if (countdown == 0) {
//                    timer.stop();
//                }
//            }
//        });
//        timer.start();
    }
    
    public void addListernersToTimers() {
        checkTimer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("CheckTimerBezi2");
                if (playerOnTurn) {
                    newMove = null;
                    newMove = player1.move(deck);
                } else {
                    newMove = null;
                    newMove = player2.move(deck);
                }
                if (newMove != null) {
                    checkTimer.stop();
                    showTimer.start();
                }
            }
        });
        
        showTimer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("ShowTimerbezi2");
                showTimer.stop();
                compareCards();
                if (uncoveredCards == DeckOfCards.NUMBER_OF_CARDS) {
                    endGame();
                } else {
                    checkTimer.start();
                }
            }
        });
    }
    
    public void endGame() {
        if (playerOnTurn) {
            output.setHeadMessage(player1.getName() + " WON!!");
        } else {
            output.setHeadMessage(player2.getName() + " WON!!");
        }
    }
    
    public void saveGame() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.showSaveDialog(null);
//        if (fileChooser.getSelectedFile() != null) {
            stopAllTimers();
//            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
//            System.out.println(filepath);
            String filepath = System.getProperty("user.dir") + "\\savedGame.txt";
            ObjectOutputStream objOutStr = null;
            try {
                objOutStr = new ObjectOutputStream(new FileOutputStream(filepath));
                objOutStr.writeObject(this);
                objOutStr.close();
                output.setErrorMessage("Save successful.");
            } catch (FileNotFoundException fnfe) {
                output.setErrorMessage("File not found.");
            } catch (IOException ioe) {
                output.setErrorMessage("IOExp");
            } finally {
                playGame();
                try {
                    if (objOutStr != null) {
                        objOutStr.close();
                    }
                } catch (IOException ex) {
                    output.setErrorMessage("Stream not closed.");
                }
            }
//        }
        
        
        
    }
    
    private void compareCards() {
        if (newMove.getFirstCard().getCompareNumber() == newMove.getSecondCard().getCompareNumber()) {
            newMove.getFirstCard().setVisible(false);
            newMove.getSecondCard().setVisible(false);
            uncoveredCards += 2;
            if (playerOnTurn) {
                player1.setScore(player1.getScore() + 10);
            }
            else {
                player1.setScore(player1.getScore() + 10);
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
    
//    public void setScore() {
//        if (playerOnTurn) {
//            player1.score += 10;
//            frame.setPlayerOneScoreLabel("Score: " + player1.score);
//        } else {
//            player2.score += 10;
//            frame.setPlayerTwoScoreLabel("Score: " + player2.score);
//        }
//    }
    
    //COUNTDOWN
//    private void refreshCountDown() {
//        frame.setPlayerOnTurnLabel("Countdown: " + countdown);
//    }
    
    public void stopAllTimers() {
        checkTimer.stop();
        showTimer.stop();
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
}
