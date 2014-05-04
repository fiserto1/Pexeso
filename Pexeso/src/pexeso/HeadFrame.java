/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import java.awt.GridLayout;
import pexeso.cards.*;
import pexeso.games.*;
import pexeso.players.*;
import pexeso.delegates.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.*;
/**
 *
 * @author Tomas
 */
public class HeadFrame extends JFrame  implements Serializable, PlayerDelegate, MessageDelegate, CardDelegate {
    
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
    private final JMenuItem nameSetMenuItem = new JMenuItem("Change name");
    private final JMenuItem numOfCardsSetMenuItem = new JMenuItem("Number of Cards");
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
    private DeckOfCards deck;
    private Settings settings;
    private Game newGame;
    
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    
    private final ImageIcon defaultPlayerAvatar = 
            new ImageIcon(getClass().getResource("/avatars/Professor.png"));
    private final ImageIcon defaultComputerAvatar = 
            new ImageIcon(getClass().getResource("/avatars/Female.png"));
    
    private Thread gameThread;

    
    public HeadFrame() {
        //Close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Menu
        createMenu();
        //Panels
        createPanels();
        
        this.player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);
        settings = new Settings(64, 1);
        
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
        settingsMenu.add(nameSetMenuItem);
        settingsMenu.add(numOfCardsSetMenuItem);
        setJMenuBar(headMenuBar);
        
        onePlayerGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryToEndThread();
                deck = new DeckOfCards(settings.getNumberOfCards());
                deck.shuffleCards();
//                AbstractPlayer player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);
                player2 = new ComputerPlayer("Computer", defaultComputerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new Game(player1, player2, deck);
                showGameBoard();
            }
        });

        twoPlayersGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryToEndThread();
                
                deck = new DeckOfCards(settings.getNumberOfCards());
                deck.shuffleCards();
//                AbstractPlayer player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);
                player2 = new HumanPlayer("Player 2", defaultPlayerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new Game(player1, player2, deck);
                showGameBoard();
            }
        });
        
        saveGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tryToEndThread();
                
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
                    errorLabel.setText("IOExp");
                } finally {
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
                tryToEndThread();
                
                ObjectInputStream objInStr = null;
                String filepath = System.getProperty("user.dir") + "\\savedGame.txt";
                try {
                    objInStr = new ObjectInputStream(new FileInputStream(filepath));

                    Game loadGame = (Game) objInStr.readObject();
                    objInStr.close();
                    errorLabel.setText("Load successful.");

                    deck = loadGame.getDeck();
                    newGame = loadGame;
                    newGame.getPlayer1().setDelegate(HeadFrame.this);
                    newGame.getPlayer2().setDelegate(HeadFrame.this);
                    newGame.setOutputDelegate(HeadFrame.this);
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
            }
        });
        
        createOnlineGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                tryToEndThread();
                
                JTextField firstTF = new JTextField(player1.getName());
                JRadioButton[] buttons = {new JRadioButton("64", true),
                    new JRadioButton("36"), new JRadioButton("16"),
                    new JRadioButton("4")};
                ButtonGroup bGroup = new ButtonGroup();
                JPanel panel = new JPanel();
                for (JRadioButton button : buttons) {
                    bGroup.add(button);
                    panel.add(button);
                }
                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Player name:"), firstTF,
                    new JLabel("Number of cards:"),
                    panel
                };
                
                int choice = JOptionPane.showConfirmDialog(null, inputs, "Creating game...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (choice != JOptionPane.OK_OPTION) {
                    return;
                }
                gameMenu.setEnabled(false);
                for (JRadioButton rBut : buttons) {
                    if (rBut.isSelected()) {
                        settings.setNumberOfCards(Integer.parseInt(rBut.getText()));
                        break;
                    }
                }
                player1.setName(firstTF.getText());
                
                deck = new DeckOfCards(settings.getNumberOfCards());
                deck.shuffleCards();
                for (Card card : deck.getCards()) {
                    card.setDelegate(HeadFrame.this);
                }
//                AbstractPlayer player1 = new HumanPlayer("You", defaultPlayerAvatar, 1);
                player1.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new ServerGame(player1, deck);
                
                setPreferredSize(new java.awt.Dimension(1050, 700));
                leftPanel.setVisible(true);
                rightPanel.setVisible(true);

                centerPanel.removeAll();
                for (Card card : deck.getCards()) {
                    centerPanel.add(new CardButton(card));
                }
                
                centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
                int rows = (int) Math.sqrt(deck.size());
                centerPanel.setLayout(new GridLayout(rows, rows, 5, 5));
                pack();
                Game.gameInterrupted = false;
                gameThread = new Thread(newGame);
                gameThread.start();
                
            }
        });
        
        joinOnlineGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveGameMenuItem.setEnabled(false);
                tryToEndThread();
                
//                AbstractPlayer player1 = new HumanPlayer("You", defaultPlayerAvatar, 1);
                player1.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new ClientGame(player1, null);
                setPreferredSize(new java.awt.Dimension(1050, 700));
                leftPanel.setVisible(true);
                rightPanel.setVisible(true);
                
                centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
                pack();
                Game.gameInterrupted = false;
                gameThread = new Thread(newGame);
                gameThread.start();
            }
        });
        
        nameSetMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog("Enter new name.");
                if (newName != null) {
                    player1.setName(newName);
                }
            }
        });
        
        numOfCardsSetMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int num = 0;
                do {
                    String newNumber = JOptionPane.showInputDialog("Enter number of cards. {64, 36, 16, 4}");
                    try {
                        num = Integer.parseInt(newNumber);
                    }
                    catch (NumberFormatException nfe) {
                        errorLabel.setText("Wrong number. Try again.");
                    }
                }
                while(num != 64 && num != 36 && num != 16 && num != 4);
                settings.setNumberOfCards(num);
            }
        });
    }
    
    private void tryToEndThread() {
        if (gameThread != null) {
            Game.gameInterrupted = true;
            while (gameThread.isAlive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                    System.out.println("ouha");
                }
            }
        }
    }
    
    private void showGameBoard() {
        setPreferredSize(new java.awt.Dimension(1050, 700));
        leftPanel.setVisible(true);
        rightPanel.setVisible(true);
        saveGameMenuItem.setEnabled(true);
        
        centerPanel.removeAll();
        for (Card card : deck.getCards()) {
            card.setDelegate(this);
            centerPanel.add(new CardButton(card));
            if (card.isDiscovered()) {
                cardRevealed(card);
            }
        }
        centerPanel.setPreferredSize(new java.awt.Dimension(550, 550));
        int rows = (int) Math.sqrt(deck.size());
        centerPanel.setLayout(new GridLayout(rows, rows, 5, 5));
        if (gameThread != null) {
            System.out.println(gameThread.isAlive());
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
        if (newGame.isEndOfGame()) {
            gameMenu.setEnabled(true);
        }
        headOutputLabel.setText(mess.getHeadMessage());
        pack();
    }

    @Override
    public void showPlayerOnBoard(AbstractPlayer player) {
        player.setName(player.getName());
        player.setScore(player.getScore());
        player.setAvatar(player.getAvatar());
    }

    @Override
    public void activateCards(CardAL listener) {
        for (int i = 0; i < centerPanel.getComponentCount(); i++) {
            CardButton cb = (CardButton) centerPanel.getComponent(i);
            cb.addActionListener(listener);
        }
    }

    @Override
    public void deactivateCards(CardAL listener) {
        for (int i = 0; i < centerPanel.getComponentCount(); i++) {
            CardButton cb = (CardButton) centerPanel.getComponent(i);
            cb.removeActionListener(listener);
        }
    }

    @Override
    public void cardRevealed(Card card) {
        centerPanel.getComponent(card.getIdNumber()).setVisible(false);
    }

    @Override
    public void showCard(Card card) {
        CardButton cardBut = (CardButton) centerPanel.getComponent(card.getIdNumber());
        cardBut.showCard();
    }

    @Override
    public void turnBackCard(Card card) {
        CardButton cardBut = (CardButton) centerPanel.getComponent(card.getIdNumber());
        cardBut.turnBack();
    }

    @Override
    public void refreshDeck(DeckOfCards deck) {
        this.deck = deck;
        for (Card card : deck.getCards()) {
            card.setDelegate(this);
        }
        
        centerPanel.removeAll();
        for (Card card : deck.getCards()) {
            centerPanel.add(new CardButton(card));
        }
        int rows = (int) Math.sqrt(deck.getCards().length);
        centerPanel.setLayout(new GridLayout(rows, rows, 5, 5));
    }
}
