/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;


/**
 *
 * @author Tomas
 */
public class Game {
    // true - player one's turn
    // false - player two's turn
    private boolean playerOnTurn = true;
    
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private HeadFrame frame;
    private int uncoveredCards = 0;

    public boolean isPlayerOnTurn() {
        return playerOnTurn;
    }

    public AbstractPlayer getPlayer1() {
        return player1;
    }

    public AbstractPlayer getPlayer2() {
        return player2;
    }

    public HeadFrame getFrame() {
        return frame;
    }

    public int getUncoveredCards() {
        return uncoveredCards;
    }

    public void changePlayerOnTurn() {
        if(playerOnTurn) {
            playerOnTurn = false;
            frame.setPlayerOnTurnLabel("Player One's turn.");
        }
        else {
            playerOnTurn = true;
            frame.setPlayerOnTurnLabel("Player Two's turn.");
        }
    }

    public void setUncoveredCards(int uncoveredCards) {
        this.uncoveredCards = uncoveredCards;
    }
    
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
        frame.setPlayerOnTurnLabel("Player One's turn.");
    }
    
    public void setScore() {
        if (!playerOnTurn) {
            player1.playerScore += 10;
            frame.setPlayerOneScoreLabel("Score: " + player1.playerScore);
        }
        else {
            player2.playerScore += 10;
            frame.setPlayerTwoScoreLabel("Score: " + player2.playerScore);
        }
    }
    
    public void endGame() {
        if (playerOnTurn) {
            frame.setPlayerOnTurnLabel(player1.playerName + " WON!!");
        }
        else {
            frame.setPlayerOnTurnLabel(player2.playerName + " WON!!");
        }
    }
    
    public void playGame() {
//        int uncoveredCards = 0;
//        OneMove move;
//        
//        while (uncoveredCards != 64) {
//            if (playerOnTurn) {
//                frame.setPlayerOnTurnLabel("Player One's turn.");
//                
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
//                
//                move = player1.move(frame.getDeck());
//                if (move.getFirstCard().equals(move.getSecondCard())) {
//                    move.getFirstCard().setVisible(false);
//                    move.getSecondCard().setVisible(false);
//                    uncoveredCards += 2;
//                }
//                else {
//                    move.getFirstCard().setText("CARD");
//                    move.getFirstCard().setIcon(null);
//                    move.getSecondCard().setText("CARD");
//                    move.getSecondCard().setIcon(null);
//                    playerOnTurn = false;
//                }
//            }
//            else {
//                frame.setPlayerOnTurnLabel("Player Two's turn.");
//                move = player2.move(frame.getDeck());
//                if (move.getFirstCard().equals(move.getSecondCard())) {
//                    move.getFirstCard().setVisible(false);
//                    move.getSecondCard().setVisible(false);
//                    uncoveredCards += 2;
//                }
//                else {
//                    move.getFirstCard().setText("CARD");
//                    move.getFirstCard().setIcon(null);
//                    move.getSecondCard().setText("CARD");
//                    move.getSecondCard().setIcon(null);
//                    playerOnTurn = true;
//                }
//            }
//        }
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
