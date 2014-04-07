/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 *
 * @author Tomas
 */
public class HeadFrame extends JFrame {
    //Menu
    private final JMenuBar headMenuBar = new JMenuBar();
    private final JMenu gameMenu = new JMenu("Game");
    private final JMenu settingsMenu = new JMenu("Settings");
    private final JMenu newGameMenu = new JMenu("New game");
    private final JMenuItem onePlayerGameMenuItem = new JMenuItem("One player");
    private final JMenuItem twoPlayersGameMenuItem = new JMenuItem("Two players");
    private final JMenuItem saveGameMenuItem = new JMenuItem("Save game");
    private final JMenuItem loadGameMenuItem = new JMenuItem("Load game");
    //Panels
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    //Player 1
    private JLabel playerOneNameLabel;
    private JLabel PlayerOneScoreLabel;
    private JButton playerOnePictureButton;
    //Player 2
    private JLabel playerTwoNameLabel;
    private JLabel playerTwoScoreLabel;
    private JButton playerTwoPictureButton;
    //top and bottom label
    private JLabel playerOnTurnLabel;
    private JLabel jLabel1;
    //cards
    private final DeckOfCards deck = new DeckOfCards();
    
    private Game newGame;

    
    public HeadFrame() {
        //Close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //Menu
        createMenu();
        //Panels
        createPanels();
        
        getContentPane().setLayout(new java.awt.BorderLayout(30, 30));
        getContentPane().add(leftPanel, java.awt.BorderLayout.LINE_START);
        getContentPane().add(rightPanel, java.awt.BorderLayout.LINE_END);
        getContentPane().add(northPanel, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(southPanel, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
        setPreferredSize(new java.awt.Dimension(800, 700));
        pack();
        setLocationRelativeTo(null);
//        setResizable(false);
        
    }
    
    private void createMenu() {
        saveGameMenuItem.setEnabled(false);
        loadGameMenuItem.setEnabled(false);
        headMenuBar.add(gameMenu);
        headMenuBar.add(settingsMenu);
        gameMenu.add(newGameMenu);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);
        newGameMenu.add(onePlayerGameMenuItem);
        newGameMenu.add(twoPlayersGameMenuItem);
        setJMenuBar(headMenuBar);
        
        onePlayerGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newGame != null) {
                    newGame.stopAllTimers();
                }
                newGame = new Game(new HumanPlayer(), new ComputerPlayer(), HeadFrame.this);
                showGameBoard();
            }
        });

        twoPlayersGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newGame != null) {
                    newGame.stopAllTimers();
                }
                newGame = new Game(new HumanPlayer(), new HumanPlayer(), HeadFrame.this);
                showGameBoard();
            }
        });
    }
    
    private void showGameBoard() {
        setPreferredSize(null);
        leftPanel.setVisible(true);
        rightPanel.setVisible(true);
        saveGameMenuItem.setVisible(true);
        
        centerPanel.removeAll();
        deck.shuffleCards();
        for (int i = 0; i < deck.getCards().length; i++) {
            centerPanel.add(deck.getCards()[i]);
            deck.getCards()[i].addActionListener(new CardAL(deck.getCards()[i], newGame));
        }
        centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
        pack();
        newGame.playGame();
    }
    
    private void createPanels() {
        //Panels
        northPanel = new JPanel();
        southPanel = new JPanel();
        centerPanel = new JPanel(new java.awt.GridLayout(8, 8, 5, 5));
        rightPanel = new JPanel(new java.awt.GridLayout(3, 1));
        leftPanel = new JPanel(new java.awt.GridLayout(3, 1));
        //Player 1
        playerOneNameLabel = new JLabel("PlayerName 1");
        PlayerOneScoreLabel = new JLabel("Score: ");
        playerOnePictureButton = new JButton(new ImageIcon(getClass().getResource("/avatars/Professor.png")));
        //Player 2
        playerTwoNameLabel = new JLabel("PlayerName 2");
        playerTwoScoreLabel = new JLabel("Score: ");
        playerTwoPictureButton = new JButton(new ImageIcon(getClass().getResource("/avatars/Female.png")));
        //Top and bottom label
        playerOnTurnLabel = new JLabel("Player on turn: ");
        jLabel1 = new JLabel("");

        
        //leftPanel
        leftPanel.add(playerOneNameLabel);
        leftPanel.add(playerOnePictureButton);
        leftPanel.add(PlayerOneScoreLabel);
        leftPanel.setVisible(false);
//        leftPanel.setPreferredSize(new java.awt.Dimension(150, 250));
        //rightPanel
        rightPanel.add(playerTwoNameLabel);
        rightPanel.add(playerTwoPictureButton);
        rightPanel.add(playerTwoScoreLabel);
        rightPanel.setVisible(false);
//        rightPanel.setPreferredSize(new java.awt.Dimension(150, 250));
        //northPanel
        northPanel.add(playerOnTurnLabel);
        //southPanel
        southPanel.add(jLabel1);
        //centerPanel
        centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
    }
    
    public DeckOfCards getDeck() {
        return deck;
    }

    public void setPlayerOneNameLabel(String name) {
        playerOneNameLabel.setText(name);
    }

    public void setPlayerOneScoreLabel(String score) {
        PlayerOneScoreLabel.setText(score);
    }

    public void setPlayerTwoNameLabel(String name) {
        playerTwoNameLabel.setText(name);
    }

    public void setPlayerTwoScoreLabel(String score) {
        playerTwoScoreLabel.setText(score);
    }

    public void setPlayerOnTurnLabel(String output) {
        playerOnTurnLabel.setText(output);
    }

    public JLabel getPlayerOnTurnLabel() {
        return playerOnTurnLabel;
    }


}
