/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import pexeso.delegates.MessageDelegate;
import pexeso.delegates.PlayerDelegate;
import pexeso.online.ClientGame;
import pexeso.online.ServerGame;
/**
 *
 * @author Tomas
 */
public class HeadFrame extends JFrame  implements Serializable, PlayerDelegate, MessageDelegate {
    
    //Menu
    private final JMenuBar headMenuBar = new JMenuBar();
    private final JMenu gameMenu = new JMenu("Game");
    private final JMenu settingsMenu = new JMenu("Settings");
    private final JMenu newGameMenu = new JMenu("New game");
    private final JMenuItem onePlayerGameMenuItem = new JMenuItem("One player");
    private final JMenuItem twoPlayersGameMenuItem = new JMenuItem("Two players");
    private final JMenuItem saveGameMenuItem = new JMenuItem("Save game");
    private final JMenuItem loadGameMenuItem = new JMenuItem("Load game");
    private final JMenuItem createOnlineGameMenuItem = new JMenuItem("Create game");
    private final JMenuItem joinOnlineGameMenuItem = new JMenuItem("Join game");
    //Panels
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    //Player 1
    private JLabel playerOneNameLabel;
    private JLabel playerOneScoreLabel;
    private JButton playerOnePictureButton;
    //Player 2
    private JLabel playerTwoNameLabel;
    private JLabel playerTwoScoreLabel;
    private JButton playerTwoPictureButton;
    //top and bottom label
    private JLabel headOutputLabel;
    private JLabel errorLabel;
    //cards
    private DeckOfCards deck = new DeckOfCards();
    
    private Game newGame;
    private ServerGame newServerGame;
    private ClientGame newClientGame;
    
    private final ImageIcon defaultPlayerAvatar = new ImageIcon(getClass().getResource("/Avatars/Professor.png"));
    private final ImageIcon defaultComputerAvatar = new ImageIcon(getClass().getResource("/Avatars/Female.png"));
    
    private Thread gameThread;

    
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
        setPreferredSize(new java.awt.Dimension(900, 700));
        pack();
        setLocationRelativeTo(null);
//        setResizable(false);
        
    }
    
    private void createMenu() {
        saveGameMenuItem.setEnabled(false);
        headMenuBar.add(gameMenu);
        headMenuBar.add(settingsMenu);
        gameMenu.add(newGameMenu);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);
        gameMenu.add(createOnlineGameMenuItem);
        gameMenu.add(joinOnlineGameMenuItem);
        newGameMenu.add(onePlayerGameMenuItem);
        newGameMenu.add(twoPlayersGameMenuItem);
        setJMenuBar(headMenuBar);
        
        onePlayerGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newGame != null) {
                    Game.gameInterrupted = true;
                    while (gameThread.isAlive()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ie) {
                            System.out.println("ouha");
                        }
                    }
                }
                deck.shuffleCards();
                AbstractPlayer player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);
                AbstractPlayer player2 = new ComputerPlayer("Computer", defaultComputerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                newGame = new Game(player1, player2, deck);
                showGameBoard();
            }
        });

        twoPlayersGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (newGame != null) {
                    Game.gameInterrupted = true;
                    while (gameThread.isAlive()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ie) {
                            System.out.println("ouha");
                        }
                    }
                }
                deck.shuffleCards();
                AbstractPlayer player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);
                AbstractPlayer player2 = new HumanPlayer("Player 2", defaultPlayerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                newGame = new Game(player1, player2, deck);
                showGameBoard();
            }
        });
        
        saveGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                newGame.saveGame();
                
                if (newGame != null) {
                    if (CardAL.getMove().getFirstCardIDNumber() != -1) {
                        deck.getCards()[CardAL.getMove().getFirstCardIDNumber()].setText("CARD");
                        deck.getCards()[CardAL.getMove().getFirstCardIDNumber()].setIcon(null);
                        CardAL.unmarkCards();
                    }
                    Game.gameInterrupted = true;
                    while (gameThread.isAlive()) {
                        try {
                            Thread.sleep(10);
                        }
                        catch (InterruptedException ie) {
                            System.out.println("ouha");
                        }
                    }
                }
                
                String filepath = System.getProperty("user.dir") + "\\savedGame.txt";
                ObjectOutputStream objOutStr = null;
                try {
                    objOutStr = new ObjectOutputStream(new FileOutputStream(filepath));
                    objOutStr.writeObject(newGame);
                    objOutStr.close();
                    errorLabel.setText("Save successful.");
                    showGameBoard();
                } catch (FileNotFoundException fnfe) {
                    errorLabel.setText("File not found.");
                } catch (IOException ioe) {
                    try {
                        throw new IOException("hh",ioe);
//                    errorLabel.setText("IOExp");
                    } catch (IOException ex) {
                        Logger.getLogger(HeadFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } finally {
//                    playGame();
                    try {
                        if (objOutStr != null) {
                            objOutStr.close();
                        }
                    } catch (IOException ex) {
                        errorLabel.setText("Stream not closed.");
                    }
                }
            }
        });
        
        loadGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (newGame != null) {
                    Game.gameInterrupted = true;
                    while (gameThread.isAlive()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ie) {
                            System.out.println("ouha");
                        }
                    }
                }
                
                
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.showOpenDialog(null);
//                if (fileChooser.getSelectedFile() != null) {
//                    String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                    ObjectInputStream objInStr = null;
//                    headOutputLabel.setText(System.getProperty("user.dir") + "\\savedGame.txt");
                    String filepath = System.getProperty("user.dir") + "\\savedGame.txt";
                    try {
                        objInStr = new ObjectInputStream(new FileInputStream(filepath));

                        Game loadGame = (Game) objInStr.readObject();
                        objInStr.close();
                        errorLabel.setText("Load successful.");
//                        System.out.println(loadGame.getUncoveredCards());
                        
                        deck = loadGame.getDeck();
                        newGame = loadGame;
                        newGame.getPlayer1().setDelegate(HeadFrame.this);
                        newGame.getPlayer2().setDelegate(HeadFrame.this);
                        newGame.setOutputDelegate(HeadFrame.this);
//                        newGame.addListernersToTimers();
                        showGameBoard();
                    } catch (FileNotFoundException fnfe) {
                        errorLabel.setText("File not found.");
                    } catch (IOException ioe) {
                        errorLabel.setText("IOExp");
                    } catch (ClassNotFoundException ex) {
                        errorLabel.setText("Class not found");
                    } finally {
                        try {
                            if (objInStr != null) {
                                objInStr.close();
                            }
                        } catch (IOException ex) {
                            errorLabel.setText("Nepodarilo se zavrit soubor");
                        }
                    }
//                }
            }
        });
        
        createOnlineGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deck.shuffleCards();
                AbstractPlayer player1 = new HumanPlayer("You", defaultPlayerAvatar, 1);
//                AbstractPlayer player2 = new HumanPlayer("Player 2", defaultPlayerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
//                player2.setDelegate(HeadFrame.this);
                newServerGame = new ServerGame(player1, deck);
                
                setPreferredSize(new java.awt.Dimension(1050, 700));
                leftPanel.setVisible(true);
                rightPanel.setVisible(true);
                saveGameMenuItem.setEnabled(true);

                centerPanel.removeAll();

                for (int i = 0; i < deck.getCards().length; i++) {
                    centerPanel.add(deck.getCards()[i]);
//            deck.getCards()[i].addActionListener(new CardAL(deck.getCards()[i], newGame));
                }
                centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
                pack();
                Thread t = new Thread(newServerGame);
                t.start();
                
            }
        });
        
        joinOnlineGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deck.shuffleCards();
//                AbstractPlayer player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);
                AbstractPlayer player2 = new HumanPlayer("You", defaultPlayerAvatar, 1);
//                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                newClientGame = new ClientGame(player2, deck);
                for (int i = 0; i < deck.getCards().length; i++) {
                    centerPanel.add(deck.getCards()[i]);
//            deck.getCards()[i].addActionListener(new CardAL(deck.getCards()[i], newGame));
                }
                setPreferredSize(new java.awt.Dimension(1050, 700));
                leftPanel.setVisible(true);
                rightPanel.setVisible(true);
                saveGameMenuItem.setEnabled(true);

//                centerPanel.removeAll();

//                for (int i = 0; i < deck.getCards().length; i++) {
//                    centerPanel.add(deck.getCards()[i]);
//            deck.getCards()[i].addActionListener(new CardAL(deck.getCards()[i], newGame));
//                }
                centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
                pack();
                
                Thread t = new Thread(newClientGame);
                t.start();
            }
        });
    }
    
    private void showGameBoard() {
        setPreferredSize(new java.awt.Dimension(1050, 700));
        leftPanel.setVisible(true);
        rightPanel.setVisible(true);
        saveGameMenuItem.setEnabled(true);
        
        centerPanel.removeAll();
        
        for (int i = 0; i < deck.getCards().length; i++) {
            centerPanel.add(deck.getCards()[i]);
//            deck.getCards()[i].addActionListener(new CardAL(deck.getCards()[i], newGame));
        }
        centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
        
        if (gameThread != null) {
//            System.out.println(gameThread.isAlive());
        }
        
        
        Game.gameInterrupted = false;
        gameThread = new Thread(newGame);
        gameThread.start();
        pack();
    }
    
    private void createPanels() {
        //Panels
        northPanel = new JPanel();
        southPanel = new JPanel();
        centerPanel = new JPanel(new java.awt.GridLayout(8, 8, 5, 5));
        rightPanel = new JPanel(new java.awt.GridLayout(3, 1));
        leftPanel = new JPanel(new java.awt.GridLayout(3, 1));
        //Player 1
        playerOneNameLabel = new JLabel("PlayerName 1", SwingConstants.CENTER);
        playerOneScoreLabel = new JLabel("Score: ", SwingConstants.CENTER);
        playerOnePictureButton = new JButton();
        playerOnePictureButton.setHorizontalAlignment(SwingConstants.CENTER);
//        ImageIcon icon1 = new ImageIcon("D:\\Dokumenty\\Vysoká škola\\2. semestr\\PR2\\Projekty\\Semestrální práce\\Pexeso\\src\\Avatars\\Professor.png");
        //Player 2
        playerTwoNameLabel = new JLabel("PlayerName 2", SwingConstants.CENTER);
        playerTwoScoreLabel = new JLabel("Score: ", SwingConstants.CENTER);
        playerTwoPictureButton = new JButton();
        playerTwoPictureButton.setHorizontalAlignment(SwingConstants.CENTER);
        //Top and bottom label
        headOutputLabel = new JLabel("Player on turn: ");
        errorLabel = new JLabel("");

        
        //leftPanel
        leftPanel.add(playerOneNameLabel);
        leftPanel.add(playerOnePictureButton);
        leftPanel.add(playerOneScoreLabel);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        leftPanel.setVisible(false);
//        leftPanel.setPreferredSize(new java.awt.Dimension(150, 250));
        //rightPanel
        rightPanel.add(playerTwoNameLabel);
        rightPanel.add(playerTwoPictureButton);
        rightPanel.add(playerTwoScoreLabel);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rightPanel.setVisible(false);
//        rightPanel.setPreferredSize(new java.awt.Dimension(150, 250));
        //northPanel
        northPanel.add(headOutputLabel);
        //southPanel
        southPanel.add(errorLabel);
        //centerPanel
        centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
    }
    
    public DeckOfCards getDeck() {
        return deck;
    }
    
    public JLabel getHeadOutputLabel() {
        return headOutputLabel;
    }

    @Override
    public void scoreChanged(AbstractPlayer player) {
        if (player.getPlayerNumber() == 1) {
            playerOneScoreLabel.setText("Score: " + player.getScore());
        }
        else {
            playerTwoScoreLabel.setText("Score: " + player.getScore());
        }
        pack();
    }

    @Override
    public void nameChanged(AbstractPlayer player) {
        if (player.getPlayerNumber() == 1) {
            playerOneNameLabel.setText(player.getName());
        } 
        else {
            playerTwoNameLabel.setText(player.getName());
        }
        pack();
    }

    @Override
    public void avatarChanged(AbstractPlayer player) {
        if (player.getPlayerNumber() == 1) {
            playerOnePictureButton.setIcon(player.getAvatar());
        } 
        else {
            playerTwoPictureButton.setIcon(player.getAvatar());
        }
        pack();
    }

    @Override
    public void errorMessageChanged(Message mess) {
        errorLabel.setText(mess.getErrorMessage());
        pack();
    }

    @Override
    public void headMessageChanged(Message mess) {
        headOutputLabel.setText(mess.getHeadMessage());
        pack();
    }
}
