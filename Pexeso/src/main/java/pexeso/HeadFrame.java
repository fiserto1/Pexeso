/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso;

import pexeso.cards.Card;
import pexeso.cards.CardAL;
import pexeso.cards.CardButton;
import pexeso.cards.DeckOfCards;
import pexeso.delegates.CardDelegate;
import pexeso.delegates.MessageDelegate;
import pexeso.delegates.PlayerDelegate;
import pexeso.games.ClientGame;
import pexeso.games.Game;
import pexeso.games.ServerGame;
import pexeso.players.AbstractPlayer;
import pexeso.players.ComputerPlayer;
import pexeso.players.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Trida pro hlavni okno. Zobrazuje menu, pomoci ktereho se ovlada program.
 * Zobrazuje hraci desku a 2 hrace.
 *
 * @author Tomas
 */
public class HeadFrame extends JFrame implements Serializable, PlayerDelegate,
        MessageDelegate, CardDelegate {

    //Menu
    private final JMenuBar headMenuBar = new JMenuBar();
    private final JMenu gameMenu = new JMenu("Game");
    private final JMenu settingsMenu = new JMenu("Settings");
    private final JMenu newGameMenu = new JMenu("New game");
    private final JMenu offlineGameMenu = new JMenu("Offline game");
    private final JMenu onlineGameMenu = new JMenu("LAN game");
    private final JMenuItem onePlayerGameMenuItem = new JMenuItem("One player");
    private final JMenuItem twoPlayersGameMenuItem = new JMenuItem("Two players");
    private final JMenuItem saveGameMenuItem = new JMenuItem("Save game");
    private final JMenuItem loadGameMenuItem = new JMenuItem("Load game");
    private final JMenuItem createOnlineGameMenuItem = new JMenuItem("Create game");
    private final JMenuItem joinOnlineGameMenuItem = new JMenuItem("Join game");
    private final JMenuItem gameSetMenuItem = new JMenuItem("Change settings");
    //Panels
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    //Player 1
    private AbstractPlayer player1;
    private JLabel playerOneNameLabel;
    private JLabel playerOneScoreLabel;
    private JButton playerOnePictureButton;
    //Player 2
    private AbstractPlayer player2;
    private JLabel playerTwoNameLabel;
    private JLabel playerTwoScoreLabel;
    private JButton playerTwoPictureButton;
    //Top and bottom label
    private JLabel headOutputLabel;
    private JLabel errorLabel;
    //Cards
    private DeckOfCards deck;
    private final Settings settings = new Settings(64, 1);
    private Game newGame;
    //Thread
    private Thread gameThread;
    //Avatars
    private final ImageIcon defaultPlayerAvatar
            = new ImageIcon(getClass().getResource("/avatars/Professor.png"));
    private final ImageIcon defaultComputerAvatar
            = new ImageIcon(getClass().getResource("/avatars/Female.png"));

    /**
     * Kontruktor hlavniho okna. Zalozi menu s posluchaci, panely a hlavniho
     * hrace.
     */
    public HeadFrame() {
        //Close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Menu
        createMenu();
        //Panels
        createPanels();

        this.player1 = new HumanPlayer("Player 1", defaultPlayerAvatar, 1);

        getContentPane().setLayout(new java.awt.BorderLayout(30, 30));
        getContentPane().add(leftPanel, java.awt.BorderLayout.LINE_START);
        getContentPane().add(rightPanel, java.awt.BorderLayout.LINE_END);
        getContentPane().add(northPanel, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(southPanel, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
        setMinimumSize(new java.awt.Dimension(1050, 700));
        pack();
        setLocationRelativeTo(null);
//        setResizable(false);

    }

    /**
     * Zalozi menu se vsemi posluchaci.
     */
    private void createMenu() {
        saveGameMenuItem.setEnabled(false);
        headMenuBar.add(gameMenu);
        headMenuBar.add(settingsMenu);
        gameMenu.add(newGameMenu);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);
        newGameMenu.add(offlineGameMenu);
        newGameMenu.add(onlineGameMenu);
        onlineGameMenu.add(createOnlineGameMenuItem);
        onlineGameMenu.add(joinOnlineGameMenuItem);
        offlineGameMenu.add(onePlayerGameMenuItem);
        offlineGameMenu.add(twoPlayersGameMenuItem);
        settingsMenu.add(gameSetMenuItem);
        setJMenuBar(headMenuBar);

        addMenuListeners();
    }

    /**
     * Vytvori panely.
     */
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
        headOutputLabel = new JLabel("Welcome in Pexeso");
        errorLabel = new JLabel();

        //leftPanel
        leftPanel.add(playerOneNameLabel);
        leftPanel.add(playerOnePictureButton);
        leftPanel.add(playerOneScoreLabel);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        leftPanel.setVisible(false);
        //rightPanel
        rightPanel.add(playerTwoNameLabel);
        rightPanel.add(playerTwoPictureButton);
        rightPanel.add(playerTwoScoreLabel);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rightPanel.setVisible(false);
        //northPanel
        northPanel.add(headOutputLabel);
        //southPanel
        southPanel.add(errorLabel);
    }

    /**
     * Zobrazi dialog pro nastaveni hry a nasledne zmeni nastaveni. Lze nastavit
     * jmeno hrace a pocet karet.
     */
    private boolean showSettingsDialog() {
        JTextField playerNameTF = new JTextField(player1.getName());
        ButtonGroup radButGroup = new ButtonGroup();
        JPanel radButPanel = new JPanel();
        JRadioButton[] radButts = {new JRadioButton("64", true),
            new JRadioButton("36"), new JRadioButton("16"),
            new JRadioButton("4")};

        for (JRadioButton button : radButts) {
            radButGroup.add(button);
            radButPanel.add(button);
        }
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Player name:"), playerNameTF,
            new JLabel("Number of cards:"),
            radButPanel
        };

        int choice = JOptionPane.showConfirmDialog(null, inputs,
                "Creating game...", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (choice != JOptionPane.OK_OPTION) {
            return false;
        }
        for (JRadioButton rBut : radButts) {
            if (rBut.isSelected()) {
                settings.setNumberOfCards(Integer.parseInt(rBut.getText()));
                break;
            }
        }
        player1.setName(playerNameTF.getText());
        return true;
    }

    /**
     * Zobrazi dialog pro pripojeni ke hre. Je potreba vyplnit jmeno hrace a IP
     * adresa hry, ke ktere se pripojujeme.
     *
     * @return Vrati zadanou IP adresu serveru(hostitele).
     */
    private String showJoinGameDialog() {
        JTextField firstTF = new JTextField(player1.getName());
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Player name:"), firstTF,
            new JLabel("Enter the IP adress of the host.")
        };
        String ipAdress = (String) JOptionPane.showInputDialog(null,
                inputs, "Connecting to host...",
                JOptionPane.PLAIN_MESSAGE, null, null, "127.0.0.1");
        player1.setName(firstTF.getText());
        return ipAdress;
    }

    /**
     * Ukonci vlakno aktualni hry. Aby bylo mozne zacit novou hru na stejnem
     * vlakne.
     */
    private void endCurrentGameThread() {
        if (gameThread != null) {
            newGame.setGameInterrupted(true);
            if (newGame.getServerSock() != null) {
                try {
                    newGame.getServerSock().close();
                } catch (IOException ex) {
                    errorLabel.setText("Server socket not closed.");
                }
            }
            if(newGame.getObjInStream() != null) {
                try {
                    newGame.getObjInStream().close();
                } catch (IOException ex) {
                    errorLabel.setText("Input stream not closed.");
                }
            }
            gameThread.interrupt();

            while (gameThread.isAlive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                    errorLabel.setText("Gui thread can't sleep.");
                }
            }
        }
    }

    /**
     * Zobrazi hraci plochu a dva hrace.
     */
    private void showGameBoard() {

        setPreferredSize(new java.awt.Dimension(1050, 700));
        leftPanel.setVisible(true);
        rightPanel.setVisible(true);
        centerPanel.setVisible(false);
        centerPanel.removeAll();
        centerPanel.setVisible(true);

        for (Card card : deck.getCards()) {
            card.setDelegate(this);
            centerPanel.add(new CardButton(card));
            if (card.isDiscovered()) {
                cardRevealed(card);
            }
        }
        int rows = (int) Math.sqrt(deck.size());
        centerPanel.setLayout(new GridLayout(rows, rows, 5, 5));

        newGame.setGameInterrupted(false);
        gameThread = new Thread(newGame);
        gameThread.start();
        pack();
    }

    /**
     *
     * @return Vrati balicek karet.
     */
    public DeckOfCards getDeck() {
        return deck;
    }

    @Override
    public void scoreChanged(AbstractPlayer player) {
        if (player.getPlayerNumber() == 1) {
            playerOneScoreLabel.setText("Score: " + player.getScore());
        } else {
            playerTwoScoreLabel.setText("Score: " + player.getScore());
        }
        pack();
    }

    @Override
    public void nameChanged(AbstractPlayer player) {
        if (player.getPlayerNumber() == 1) {
            playerOneNameLabel.setText(player.getName());
        } else {
            playerTwoNameLabel.setText(player.getName());
        }
        pack();
    }

    @Override
    public void avatarChanged(AbstractPlayer player) {
        if (player.getPlayerNumber() == 1) {
            playerOnePictureButton.setIcon(player.getAvatar());
        } else {
            playerTwoPictureButton.setIcon(player.getAvatar());
        }
        pack();
    }

    @Override
    public void errorMessageChanged(Message mess) {
        if (!newGame.isGameInterrupted()) {
            JOptionPane.showMessageDialog(null, mess.getErrorMessage());
        }
//        errorLabel.setText(mess.getErrorMessage());
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
    public void cardShowed(Card card) {
        CardButton cardBut = (CardButton) centerPanel.getComponent(card.getIdNumber());
        cardBut.setVisible(false);
        cardBut.showCard();
        cardBut.setVisible(true);
    }

    @Override
    public void cardTurnedBack(Card card) {
        CardButton cardBut = (CardButton) centerPanel.getComponent(card.getIdNumber());
        cardBut.turnBack();
    }

    @Override
    public void refreshDeck(DeckOfCards deck) {
        this.deck = deck;
        for (Card card : deck.getCards()) {
            card.setDelegate(this);
        }

        centerPanel.setVisible(false);
        centerPanel.removeAll();
        centerPanel.setVisible(true);
        for (Card card : deck.getCards()) {
            centerPanel.add(new CardButton(card));
        }
        int rows = (int) Math.sqrt(deck.getCards().length);
        centerPanel.setLayout(new GridLayout(rows, rows, 5, 5));
    }

    /**
     * Prida vsechny posluchace pro menu.
     */
    private void addMenuListeners() {
        addOnePlGameMenuListener();
        addTwoPlGameMenuListener();
        addSaveGameMenuListener();
        addLoadGameMenuListener();
        addCreateOnlineGameMenuListener();
        addJoinGameMenuListener();

        gameSetMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showSettingsDialog();
            }
        });
    }

    /**
     * Prida posluchace pro hru jednoho hrace.
     */
    private void addOnePlGameMenuListener() {
        onePlayerGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGameMenuItem.setEnabled(true);
                endCurrentGameThread();
                deck = new DeckOfCards(settings.getNumberOfCards());
                deck.shuffleCards();
                player2 = new ComputerPlayer("Computer", defaultComputerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new Game(player1, player2, deck);
                showGameBoard();
            }
        });
    }

    /**
     * Prida posluchace pro hru dvou hracu.
     */
    private void addTwoPlGameMenuListener() {
        twoPlayersGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGameMenuItem.setEnabled(true);
                endCurrentGameThread();

                deck = new DeckOfCards(settings.getNumberOfCards());
                deck.shuffleCards();
                player2 = new HumanPlayer("Player 2", defaultPlayerAvatar, 2);
                player1.setDelegate(HeadFrame.this);
                player2.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new Game(player1, player2, deck);
                showGameBoard();
            }
        });
    }

    /**
     * Prida posluchace pro ulozeni hry.
     */
    private void addSaveGameMenuListener() {
        saveGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String filepath = System.getProperty("user.dir") + "\\savedGame.txt";
                ObjectOutputStream objOutStr = null;
                try {
                    objOutStr = new ObjectOutputStream(new FileOutputStream(filepath));
                    objOutStr.writeObject(newGame);
                    objOutStr.close();
                    endCurrentGameThread();
                    JOptionPane.showMessageDialog(null, "Save successful.");
                    showGameBoard();
                } catch (FileNotFoundException fnfe) {
                    JOptionPane.showMessageDialog(null, "\"savedGame.txt\" not found.");
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Save failed.");
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
    }

    /**
     * Prida posluchace pro nacteni hry.
     */
    private void addLoadGameMenuListener() {
        loadGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ObjectInputStream objInStr = null;
                String filepath = System.getProperty("user.dir") + "\\savedGame.txt";
                try {
                    objInStr = new ObjectInputStream(new FileInputStream(filepath));

                    Game loadGame = (Game) objInStr.readObject();
                    objInStr.close();
                    endCurrentGameThread();
                    JOptionPane.showMessageDialog(null, "Load successful.");
                    saveGameMenuItem.setEnabled(true);
                    deck = loadGame.getDeck();
                    newGame = loadGame;
                    newGame.getPlayer1().setDelegate(HeadFrame.this);
                    newGame.getPlayer2().setDelegate(HeadFrame.this);
                    newGame.setOutputDelegate(HeadFrame.this);
                    showGameBoard();
                } catch (FileNotFoundException fnfe) {
                    JOptionPane.showMessageDialog(null, "\"savedGame.txt\" not found. You have to save any game first.");
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "\"savedGame.txt\" is corrupted. You have to save any game first.");
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Class not found");
                } finally {
                    try {
                        if (objInStr != null) {
                            objInStr.close();
                        }
                    } catch (IOException ex) {
                        errorLabel.setText("Stream not closed.");
                    }
                }
            }
        });
    }

    /**
     * Prida posluchace pro zalozeni hry.
     */
    private void addCreateOnlineGameMenuListener() {
        createOnlineGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                
                boolean valid = showSettingsDialog();
                if (!valid){
                    return;
                }
                saveGameMenuItem.setEnabled(false);
                endCurrentGameThread();
                

                deck = new DeckOfCards(settings.getNumberOfCards());
                deck.shuffleCards();

                player1.setDelegate(HeadFrame.this);
                player1.setScore(0);

                newGame = new ServerGame(player1, deck);
                showGameBoard();
            }
        });

    }

    /**
     * Prida posluchace pro pripojeni ke hre.
     */
    private void addJoinGameMenuListener() {
        joinOnlineGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String ipAdress = showJoinGameDialog();
                if (ipAdress == null) {
                    return;
                }
                saveGameMenuItem.setEnabled(false);
                endCurrentGameThread();

                player1.setDelegate(HeadFrame.this);
                player1.setScore(0);
                newGame = new ClientGame(player1, null);
                setPreferredSize(new java.awt.Dimension(1050, 700));
                leftPanel.setVisible(true);
                rightPanel.setVisible(true);

                newGame.setHostIPAddress(ipAdress);

                newGame.setGameInterrupted(false);
                gameThread = new Thread(newGame);
                gameThread.start();
                pack();
            }
        });
    }
}
