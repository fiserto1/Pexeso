/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
    private HeadFrame frame;
    private int uncoveredCards = 0;
    private OneMove newMove;
    
    private Timer showTimer = new Timer(500, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("ShowTime bezi.");
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
            System.out.println("CheckTimerBezi");
            if (playerOnTurn) {
                newMove = null;
                newMove = player1.move(frame.getDeck());
            }
            else {
                newMove = null;
                newMove = player2.move(frame.getDeck());
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
            frame.setPlayerOnTurnLabel("Player Two's turn.");
            
        }
        else {
            playerOnTurn = true;
            frame.setPlayerOnTurnLabel("Player One's turn.");
        }
    }
    
    //COUNTDOWN
//    private int countdown = 21;
//    private Timer timer;

    public Game(AbstractPlayer player1, AbstractPlayer player2, HeadFrame frame) {
        this.player1 = player1;
        this.player2 = player2;
        this.frame = frame;
        
    }
    
    
    public void playGame() {
        frame.setPlayerOneNameLabel(player1.playerName);
        frame.setPlayerTwoNameLabel(player2.playerName);
        frame.setPlayerOneScoreLabel("Score: " + player1.playerScore);
        frame.setPlayerTwoScoreLabel("Score: " + player2.playerScore);
        frame.setPlayerOnePictureButton(player1.avatar);
        frame.setPlayerTwoPictureButton(player2.avatar);
        if (playerOnTurn) {
            frame.setPlayerOnTurnLabel("Player One's turn.");
        }
        else {
            frame.setPlayerOnTurnLabel("Player Two's turn.");
        }
        frame.pack();
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
                System.out.println("CheckTimerBezi2");
                if (playerOnTurn) {
                    newMove = null;
                    newMove = player1.move(frame.getDeck());
                } else {
                    newMove = null;
                    newMove = player2.move(frame.getDeck());
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
            frame.setPlayerOnTurnLabel(player1.playerName + " WON!!");
        } else {
            frame.setPlayerOnTurnLabel(player2.playerName + " WON!!");
        }
    }
    
    public void saveGame() {
        stopAllTimers();
        
        ObjectOutputStream objOutStr = null;
        try {
            objOutStr = new ObjectOutputStream(new FileOutputStream(
                    "/savedGame.txt"));
            objOutStr.writeObject(this);
            objOutStr.close();
            frame.setDownLabel("Save successful.");
        } catch (FileNotFoundException fnfe) {
            frame.setDownLabel("File not found.");
        } catch (IOException ioe) {
            frame.setDownLabel("IOExp");
        } 
        
        finally {
            try {
                objOutStr.close();
            } catch (IOException ex) {
                frame.setDownLabel("Stream not closed.");
            }
        }
        
        playGame();
    }
    
    private void compareCards() {
        if (newMove.getFirstCard().getCompareNumber() == newMove.getSecondCard().getCompareNumber()) {
            newMove.getFirstCard().setVisible(false);
            newMove.getSecondCard().setVisible(false);
            uncoveredCards += 2;
            setScore();
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
    
    public void setScore() {
        if (playerOnTurn) {
            player1.playerScore += 10;
            frame.setPlayerOneScoreLabel("Score: " + player1.playerScore);
        } else {
            player2.playerScore += 10;
            frame.setPlayerTwoScoreLabel("Score: " + player2.playerScore);
        }
    }
    
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

    public void setFrame(HeadFrame frame) {
        this.frame = frame;
    }

    public HeadFrame getFrame() {
        return frame;
    }
    
}
