/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import javax.swing.*;
/**
 *
 * @author Tomas
 */
public class HeadFrame extends JFrame {
    //Menu
    private JMenuBar headMenuBar;
    private JMenu gameMenu;
    private JMenu settingsMenu;
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
    private DeckOfCards deck;
    
    
    public HeadFrame() {
        //Menu
        headMenuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        settingsMenu = new JMenu("Settings");
        //Panels
        northPanel = new JPanel();
        southPanel = new JPanel();
        centerPanel = new JPanel(new java.awt.GridLayout(8, 8, 5, 5));
        rightPanel = new JPanel(new java.awt.GridLayout(3, 1));
        leftPanel = new JPanel(new java.awt.GridLayout(3, 1));
        //Player 1
        playerOneNameLabel = new JLabel("PlayerName 1");
        PlayerOneScoreLabel = new JLabel("Score: ");
        playerOnePictureButton = new JButton(new ImageIcon("src\\avatars\\Professor.png"));
        //Player 2
        playerTwoNameLabel = new JLabel("PlayerName 2");
        playerTwoScoreLabel = new JLabel("Score: ");
        playerTwoPictureButton = new JButton(new ImageIcon("src\\avatars\\Female.png"));
        //top and bottom label
        playerOnTurnLabel = new JLabel("Player on turn: ");
        jLabel1 = new JLabel("");
        //Cards
        deck = new DeckOfCards();
        
        
        //Close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout(30, 30));
        //Menu
        headMenuBar.add(gameMenu);
        headMenuBar.add(settingsMenu);
        //leftPanel
        leftPanel.add(playerOneNameLabel);
        leftPanel.add(playerOnePictureButton);
        leftPanel.add(PlayerOneScoreLabel);
//        leftPanel.setPreferredSize(new java.awt.Dimension(150, 250));
        //rightPanel
        rightPanel.add(playerTwoNameLabel);
        rightPanel.add(playerTwoPictureButton);
        rightPanel.add(playerTwoScoreLabel);
//        rightPanel.setPreferredSize(new java.awt.Dimension(150, 250));
        //northPanel
        northPanel.add(playerOnTurnLabel);
        //southPanel
        southPanel.add(jLabel1);
        //centerPanel
        deck.shuffleCards();
        for (int i = 0; i < deck.getCards().length; i++) {
            centerPanel.add(deck.getCards()[i]);
        }
        centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
        

        setJMenuBar(headMenuBar);
        getContentPane().add(leftPanel, java.awt.BorderLayout.LINE_START);
        getContentPane().add(rightPanel, java.awt.BorderLayout.LINE_END);
        getContentPane().add(northPanel, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(southPanel, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        
        
        pack();
        setLocationRelativeTo(null);
//        setResizable(false);
        
    }
}
