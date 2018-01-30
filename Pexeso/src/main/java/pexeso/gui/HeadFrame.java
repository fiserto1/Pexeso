/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.gui;

import pexeso.Message;
import pexeso.Settings;
import pexeso.cards.Card;
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
    public final JMenu settingsMenu = new JMenu("Settings");
    private final JMenuItem mainMenuItem = new JMenuItem("Main menu");
    private final JMenuItem saveGameMenuItem = new JMenuItem("Save game");
    private final JMenuItem loadGameMenuItem = new JMenuItem("Load game");
    private final JMenuItem gameSetMenuItem = new JMenuItem("Change settings");
    //Panels
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel centerCardDeckPanel;
    private JPanel centerMenuPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    //Menu
    private JButton singlePlayerButton;
    private JButton twoPlayersButton;
    private JButton lanButton;
    private JButton startGameButton;
    private JButton startTwoPlayerGameBut;
    private JButton createLANGameBut;
    private JButton joinLANGameBut;

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
    public final ImageIcon defaultPlayerAvatar
            = new ImageIcon(getClass().getResource("/avatars/Professor.png"));
    public final ImageIcon defaultComputerAvatar
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
        getContentPane().add(centerMenuPanel, java.awt.BorderLayout.CENTER);
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
        gameMenu.add(mainMenuItem);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);
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
        centerCardDeckPanel = new JPanel(new java.awt.GridLayout(8, 8, 5, 5));
        centerMenuPanel = new JPanel(new java.awt.GridBagLayout());
        rightPanel = new JPanel(new java.awt.GridLayout(3, 1));
        leftPanel = new JPanel(new java.awt.GridLayout(3, 1));
        //Menu Buttons
        initMainMenuPanel();
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

    private void initMainMenuPanel() {
        singlePlayerButton = new JButton("Single player");
        singlePlayerButton.setPreferredSize(new Dimension(400, 100));
        singlePlayerButton.addActionListener(e -> {
            hideMainMenu();
            showSinglePlayerMenu();
        });
        twoPlayersButton = new JButton("Two players");
        twoPlayersButton.addActionListener(e -> {
            hideMainMenu();
            showTwoPlayerMenu();
        });
        lanButton = new JButton("LAN Game");
        lanButton.addActionListener(e -> {
            hideMainMenu();
            showLANMenu();
        });

        JPanel buttonGrid = new JPanel(new GridLayout(3, 1));
        buttonGrid.add(singlePlayerButton);
        buttonGrid.add(twoPlayersButton);
        buttonGrid.add(lanButton);
        centerMenuPanel.add(buttonGrid);
    }

    private void showLANMenu() {
        createLANGameBut = new JButton("Create game");
        createLANGameBut.addActionListener(e -> {
            centerMenuPanel.removeAll();
            getContentPane().remove(centerMenuPanel);
            getContentPane().add(centerCardDeckPanel, BorderLayout.CENTER);

            createLANGame();
        });
        createLANGameBut.setPreferredSize(new Dimension(400, 100));

        joinLANGameBut = new JButton("Join game");
        joinLANGameBut.addActionListener(e -> {
            centerMenuPanel.removeAll();
            getContentPane().remove(centerMenuPanel);
            getContentPane().add(centerCardDeckPanel, BorderLayout.CENTER);

            joinLANGame();
        });

        JPanel buttonGrid = new JPanel(new GridLayout(2, 1));
        buttonGrid.add(createLANGameBut);
        buttonGrid.add(joinLANGameBut);
        centerMenuPanel.add(buttonGrid);
        centerMenuPanel.repaint();
        pack();
    }

    private void joinLANGame() {
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

    private void createLANGame() {
        boolean valid = showSettingsDialog();
        if (!valid) {
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

    private void showTwoPlayerMenu() {
        startTwoPlayerGameBut = new JButton("Start game");
        startTwoPlayerGameBut.addActionListener(e -> {
            centerMenuPanel.removeAll();
            getContentPane().remove(centerMenuPanel);
            getContentPane().add(centerCardDeckPanel, BorderLayout.CENTER);

            startTwoPlayerGame();
        });
        startTwoPlayerGameBut.setPreferredSize(new Dimension(400, 100));
        JPanel buttonGrid = new JPanel(new GridLayout(1, 1));
        buttonGrid.add(startTwoPlayerGameBut);
        centerMenuPanel.add(buttonGrid);
        centerMenuPanel.repaint();
        pack();
    }

    private void startTwoPlayerGame() {
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

    private void showSinglePlayerMenu() {
        startGameButton = new JButton("Start game");
        startGameButton.addActionListener(e -> {
            centerMenuPanel.removeAll();
            getContentPane().remove(centerMenuPanel);
            getContentPane().add(centerCardDeckPanel, BorderLayout.CENTER);

            startSingleGame();
        });
        startGameButton.setPreferredSize(new Dimension(400, 100));
        JPanel buttonGrid = new JPanel(new GridLayout(1, 1));
        buttonGrid.add(startGameButton);
        centerMenuPanel.add(buttonGrid);
        centerMenuPanel.repaint();
        pack();
    }

    private void startSingleGame() {
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

    private void hideMainMenu() {
        centerMenuPanel.removeAll();
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
            if (newGame.getObjInStream() != null) {
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
        centerCardDeckPanel.setVisible(false);
        centerCardDeckPanel.removeAll();
        centerCardDeckPanel.setVisible(true);

        for (Card card : deck.getCards()) {
            card.setDelegate(this);
            centerCardDeckPanel.add(new CardButton(card));
            if (card.isDiscovered()) {
                cardRevealed(card);
            }
        }
        int rows = (int) Math.sqrt(deck.size());
        centerCardDeckPanel.setLayout(new GridLayout(rows, rows, 5, 5));

        newGame.setGameInterrupted(false);
        gameThread = new Thread(newGame);
        gameThread.start();
        pack();
    }

    /**
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
        for (int i = 0; i < centerCardDeckPanel.getComponentCount(); i++) {
            CardButton cb = (CardButton) centerCardDeckPanel.getComponent(i);
            cb.addActionListener(listener);
        }
    }

    @Override
    public void deactivateCards(CardAL listener) {
        for (int i = 0; i < centerCardDeckPanel.getComponentCount(); i++) {
            CardButton cb = (CardButton) centerCardDeckPanel.getComponent(i);
            cb.removeActionListener(listener);
        }
    }

    @Override
    public void cardRevealed(Card card) {
        centerCardDeckPanel.getComponent(card.getIdNumber()).setVisible(false);
    }

    @Override
    public void cardShowed(Card card) {
        CardButton cardBut = (CardButton) centerCardDeckPanel.getComponent(card.getIdNumber());
        cardBut.setVisible(false);
        cardBut.showCard();
        cardBut.setVisible(true);
    }

    @Override
    public void cardTurnedBack(Card card) {
        CardButton cardBut = (CardButton) centerCardDeckPanel.getComponent(card.getIdNumber());
        cardBut.turnBack();
    }

    @Override
    public void refreshDeck(DeckOfCards deck) {
        this.deck = deck;
        for (Card card : deck.getCards()) {
            card.setDelegate(this);
        }

        centerCardDeckPanel.setVisible(false);
        centerCardDeckPanel.removeAll();
        centerCardDeckPanel.setVisible(true);
        for (Card card : deck.getCards()) {
            centerCardDeckPanel.add(new CardButton(card));
        }
        int rows = (int) Math.sqrt(deck.getCards().length);
        centerCardDeckPanel.setLayout(new GridLayout(rows, rows, 5, 5));
    }

    /**
     * Prida vsechny posluchace pro menu.
     */
    private void addMenuListeners() {
        mainMenuItem.addActionListener(e -> showMainMenu());
        saveGameMenuItem.addActionListener(e -> saveGame());
        loadGameMenuItem.addActionListener(e -> loadGame());

        gameSetMenuItem.addActionListener(e -> showSettingsDialog());
    }

    private void showMainMenu() {
        endCurrentGameThread();

        saveGameMenuItem.setVisible(false);
        saveGameMenuItem.setVisible(false);
        centerCardDeckPanel.removeAll();
        centerMenuPanel.removeAll();
        getContentPane().remove(centerCardDeckPanel);
        getContentPane().add(centerMenuPanel, BorderLayout.CENTER);

        leftPanel.setVisible(false);
        rightPanel.setVisible(false);

        headOutputLabel.setText("Main menu");
        initMainMenuPanel();
        repaint();
        pack();
    }

    private void saveGame() {
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

    private void loadGame() {
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
}
