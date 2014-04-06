/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Tomas
 */
public class Game {
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private HeadFrame frame;
    
    //COUNTDOWN
//    private int countdown = 21;
//    private Timer timer;

    public Game(AbstractPlayer player1, AbstractPlayer player2, HeadFrame frame) {
        this.player1 = player1;
        this.player2 = player2;
        this.frame = frame;
        frame.setPlayerOneNameLabel(player1.playerName);
        frame.setPlayerTwoNameLabel(player2.playerName);
        frame.setPlayerOneScoreLabel("Score: " + player1.playerScore);
        frame.setPlayerTwoScoreLabel("Score: " + player2.playerScore);
    }
    
    public void playGame() {
        int uncoveredCards = 0;
        OneMove move;
        // true - player one's turn
        // false - player two's turn
        boolean playerOneTurn = true;
        while (uncoveredCards != 64) {
            if (playerOneTurn) {
                frame.setPlayerOnTurnLabel("Player One's turn.");
                
                //COUNTDOWN
//                timer = new Timer(1000, new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        countdown--;
//                        refreshCountDown();
//                        if (countdown == 0) {
//                            timer.stop();
//                        }
//                    }
//                });
//                timer.start();
                
                move = player1.move(frame.getDeck());
                if (move.getFirstCard().equals(move.getSecondCard())) {
                    move.getFirstCard().setVisible(false);
                    move.getSecondCard().setVisible(false);
                    uncoveredCards += 2;
                }
                else {
                    move.getFirstCard().setText("CARD");
                    move.getFirstCard().setIcon(null);
                    move.getSecondCard().setText("CARD");
                    move.getSecondCard().setIcon(null);
                    playerOneTurn = false;
                }
            }
            else {
                frame.setPlayerOnTurnLabel("Player Two's turn.");
                move = player2.move(frame.getDeck());
                if (move.getFirstCard().equals(move.getSecondCard())) {
                    move.getFirstCard().setVisible(false);
                    move.getSecondCard().setVisible(false);
                    uncoveredCards += 2;
                }
                else {
                    move.getFirstCard().setText("CARD");
                    move.getFirstCard().setIcon(null);
                    move.getSecondCard().setText("CARD");
                    move.getSecondCard().setIcon(null);
                    playerOneTurn = true;
                }
            }
        }
    }
    
    public void saveGame() {
        
    }
    
    public void loadGame() {
        
    }
    
    //COUNTDOWN
//    private void refreshCountDown() {
//        frame.setPlayerOnTurnLabel("Countdown: " + countdown);
//    }
}
