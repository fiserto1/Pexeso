/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.games;

import pexeso.HeadFrame;
import pexeso.Message;
import pexeso.OneMove;
import pexeso.cards.DeckOfCards;
import pexeso.delegates.MessageDelegate;
import pexeso.players.AbstractPlayer;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Trida pro zakladni hru. Zajistuje provoz hry. Porovnava karty, urcuje, kdy ma
 * byt konec, zobrazuje otocene karty po dobu 1 sekundy. Vymenuje hrace na tahu.
 * Pridava skore prislusnemu hraci.
 *
 * @author Tomas
 */
public class Game implements Serializable, Runnable {

    protected Socket clientSock;
    protected ServerSocket serverSock;
    protected ObjectInputStream objInStream;
    protected String hostIPAddress;

    protected boolean gameInterrupted;

    // true - player one's turn
    // false - player two's turn
    protected boolean player1OnTurn;
    protected AbstractPlayer player1;
    protected AbstractPlayer player2;
    protected DeckOfCards deck;

    protected int uncoveredCards;

    protected OneMove newMove;
    protected OneMove lastPlayer1Move;
    protected OneMove lastPlayer2Move;

    protected boolean rightMoveByPlayer1;
    protected boolean rightMoveByPlayer2;

    protected ArrayList<OneMove> player1Moves = new ArrayList<OneMove>();
    protected ArrayList<OneMove> player2Moves = new ArrayList<OneMove>();

    protected transient Message output;
    protected boolean endOfGame;

    private String result;

    /**
     * Zacina prvni hrac.
     *
     * @param player1 Privni hrac.
     * @param player2 Druhy hrac.
     * @param deck Balicek karet.
     */
    public Game(AbstractPlayer player1, AbstractPlayer player2, DeckOfCards deck) {
        this.player1 = player1;
        this.player2 = player2;
        this.deck = deck;
        this.result = "";
        this.lastPlayer1Move = null;
        this.lastPlayer2Move = null;
        this.player1OnTurn = true;
        if (player1 != null) {
            this.output = new Message((HeadFrame) player1.getDelegate());
        } else {
            this.output = new Message((HeadFrame) player2.getDelegate());
        }
    }

    @Override
    public void run() {
        if (player1OnTurn) {
            output.setHeadMessage(player1.getName() + "'s turn.");
        } else {
            output.setHeadMessage(player2.getName() + "'s turn.");
        }

        while (!endOfGame) {
            if (player1OnTurn) {
                newMove = player1.move(lastPlayer1Move, player2Moves, deck.size());
            } else {
                newMove = player2.move(lastPlayer2Move, player1Moves, deck.size());
            }
            if (gameInterrupted) {
                return;
            }
            evaluateMove();
        }
    }

    /**
     * Vyhodnoti tah prislusneho hrace.
     */
    protected void evaluateMove() {

        if (newMove != null) {
            showCards();
            if (newMove.getFirstCardIDNumber() != -1
                    && newMove.getSecondCardIDNumber() != -1) {
                compareCards();
            }
        }

        if (uncoveredCards == deck.size()) {
            endGame();
        }
    }

    /**
     * Otoci karticky po dobu jedne vteriny licem vzhuru.
     */
    protected void showCards() {
        deck.getCards()[newMove.getFirstCardIDNumber()].showCard();
        deck.getCards()[newMove.getSecondCardIDNumber()].showCard();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Game cant sleep.");
        }
    }

    /**
     * Ukonci hru a urci viteze.
     */
    public void endGame() {
        endOfGame = true;
        if (player1.getScore() > player2.getScore()) {
            result = player1.getName() + " WON!!";
        } else if (player1.getScore() < player2.getScore()) {
            result = player2.getName() + " WON!!";
        } else {
            result = "DRAW";
        }
        output.setHeadMessage(result);
    }

    /**
     * Porovna karty a ulozi, aby je mohl pozdeji poslat protihraci.
     */
    protected void compareCards() {
        int firstCardIDNum = newMove.getFirstCardIDNumber();
        int secondCardIDNum = newMove.getSecondCardIDNumber();
        if (deck.getCards()[firstCardIDNum].equals(
                deck.getCards()[secondCardIDNum])) {

            rightMove(firstCardIDNum, secondCardIDNum);

        } else {

            falseMove(firstCardIDNum, secondCardIDNum);

            deck.getCards()[firstCardIDNum].turnBack();
            deck.getCards()[secondCardIDNum].turnBack();
            changePlayerOnTurn();
            newMove = null;
        }
    }

    /**
     * Hrac nasel dvojici.
     *
     * @param firstCardIDNum ID prvni karty.
     * @param secondCardIDNum ID druhe karty.
     */
    public void rightMove(int firstCardIDNum, int secondCardIDNum) {
        deck.getCards()[firstCardIDNum].hideCard();
        deck.getCards()[secondCardIDNum].hideCard();
        uncoveredCards += 2;
        if (player1OnTurn) {
            player1.setScore(player1.getScore() + 10);
            lastPlayer1Move = new OneMove(firstCardIDNum, secondCardIDNum);
            if (firstCardIDNum != -1 && secondCardIDNum != -1) {
                lastPlayer1Move.setFirstCardCompareNumber(
                        deck.getCards()[firstCardIDNum].getCompareNumber());
                lastPlayer1Move.setSecondCardCompareNumber(
                        deck.getCards()[secondCardIDNum].getCompareNumber());
                if (!rightMoveByPlayer1) {
                    rightMoveByPlayer1 = true;
                    player1Moves = new ArrayList<OneMove>();
                    player1Moves.add(lastPlayer1Move);
                } else {
                    player1Moves.add(lastPlayer1Move);
                }
            }
        } else {
            player2.setScore(player2.getScore() + 10);
            lastPlayer2Move = new OneMove(firstCardIDNum, secondCardIDNum);

            if (firstCardIDNum != -1 && secondCardIDNum != -1) {
                lastPlayer2Move.setFirstCardCompareNumber(
                        deck.getCards()[firstCardIDNum].getCompareNumber());
                lastPlayer2Move.setSecondCardCompareNumber(
                        deck.getCards()[secondCardIDNum].getCompareNumber());
                if (!rightMoveByPlayer2) {
                    rightMoveByPlayer2 = true;
                    player2Moves = new ArrayList<OneMove>();
                    player2Moves.add(lastPlayer2Move);
                } else {
                    player2Moves.add(lastPlayer2Move);
                }
            }
        }
        newMove = null;
    }

    /**
     * Hrac netrefil dvojici.
     *
     * @param firstCardIDNum ID prvni karty.
     * @param secondCardIDNum ID druhe karty.
     */
    public void falseMove(int firstCardIDNum, int secondCardIDNum) {
        if (player1OnTurn) {

            lastPlayer1Move = new OneMove(firstCardIDNum, secondCardIDNum);
            if (firstCardIDNum != -1 && secondCardIDNum != -1) {
                lastPlayer1Move.setFirstCardCompareNumber(
                        deck.getCards()[firstCardIDNum].getCompareNumber());
                lastPlayer1Move.setSecondCardCompareNumber(
                        deck.getCards()[secondCardIDNum].getCompareNumber());

                if (!rightMoveByPlayer1) {
                    player1Moves = new ArrayList<OneMove>();
                    player1Moves.add(lastPlayer1Move);
                } else {
                    rightMoveByPlayer1 = false;
                    player1Moves.add(lastPlayer1Move);
                }
            }
        } else {
            player2Moves = new ArrayList<OneMove>();
            lastPlayer2Move = new OneMove(firstCardIDNum,
                    newMove.getSecondCardIDNumber());

            if (firstCardIDNum != -1
                    && secondCardIDNum != -1) {
                lastPlayer2Move.setFirstCardCompareNumber(
                        deck.getCards()[firstCardIDNum].getCompareNumber());
                lastPlayer2Move.setSecondCardCompareNumber(
                        deck.getCards()[secondCardIDNum].getCompareNumber());
                if (!rightMoveByPlayer2) {
                    player2Moves = new ArrayList<OneMove>();
                    player2Moves.add(lastPlayer2Move);
                } else {
                    rightMoveByPlayer2 = false;
                    player2Moves.add(lastPlayer2Move);
                }
            }
        }
    }

    /**
     * Vymeni hrace na tahu.
     */
    public void changePlayerOnTurn() {
        if (player1OnTurn) {
            player1OnTurn = false;
            output.setHeadMessage(player2.getName() + "'s turn.");

        } else {
            player1OnTurn = true;
            output.setHeadMessage(player1.getName() + "'s turn.");
        }
    }

    /**
     * true - prvni hrac je na tahu. false - druha hrac je na tahu.
     *
     * @return Vrati hrace na tahu.
     */
    public boolean isPlayer1OnTurn() {
        return player1OnTurn;
    }

    /**
     *
     * @return Vrati pocet uhadnutych karet.
     */
    public int getUncoveredCards() {
        return uncoveredCards;
    }

    /**
     *
     * @return Vrati balicek karet.
     */
    public DeckOfCards getDeck() {
        return deck;
    }

    /**
     * Nastavi balicek karet.
     *
     * @param deck Balicek karet.
     */
    public void setDeck(DeckOfCards deck) {
        this.deck = deck;
    }

    /**
     *
     * @return Vrati prvniho hrace.
     */
    public AbstractPlayer getPlayer1() {
        return player1;
    }

    /**
     *
     * @return Vrati druheho hrace.
     */
    public AbstractPlayer getPlayer2() {
        return player2;
    }

    public void setEndOfGame(boolean endOfGame) {
        this.endOfGame = endOfGame;
    }

    /**
     *
     * @return Vrati aktualni tah.
     */
    public OneMove getNewMove() {
        return newMove;
    }

    /**
     *
     * @return Vrati zpravu hry.
     */
    public Message getOutput() {
        return output;
    }

    /**
     * true - konec hry. false - hra stale bezi.
     *
     * @return Vrati, zda je hra u konce.
     */
    public boolean isEndOfGame() {
        return endOfGame;
    }

    /**
     * Nastavi delegata zprave.
     *
     * @param delegate Delegat zpravy.
     */
    public void setOutputDelegate(MessageDelegate delegate) {
        this.output = new Message(delegate);
    }

    /**
     * true - Hra byla prerusena. false - Hra nebyla prerusena.
     *
     * @return Vrati, zda byla hra prerusena.
     */
    public boolean isGameInterrupted() {
        return gameInterrupted;
    }

    /**
     * true - Hra byla prerusena. false - Hra nebyla prerusena.
     *
     * @param gameInterrupted Hra prerusena.
     */
    public void setGameInterrupted(boolean gameInterrupted) {
        this.gameInterrupted = gameInterrupted;
    }

    /**
     *
     * @return Vrati socket klienta.
     */
    public Socket getClientSock() {
        return clientSock;
    }

    /**
     *
     * @return Vrati socket serveru.
     */
    public ServerSocket getServerSock() {
        return serverSock;
    }

    /**
     * Nastavi IP Adresu hostitele(serveru).
     *
     * @param hostIPAddress IP Adresa hostitele(serveru).
     */
    public void setHostIPAddress(String hostIPAddress) {
        this.hostIPAddress = hostIPAddress;
    }

    /**
     * 
     * @return Vrati vstupni proud hry.
     */
    public ObjectInputStream getObjInStream() {
        return objInStream;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.player1OnTurn ? 1 : 0);
        hash = 41 * hash + (this.player1 != null ? this.player1.hashCode() : 0);
        hash = 41 * hash + (this.player2 != null ? this.player2.hashCode() : 0);
        hash = 41 * hash + (this.deck != null ? this.deck.hashCode() : 0);
        hash = 41 * hash + this.uncoveredCards;
        hash = 41 * hash + (this.newMove != null ? this.newMove.hashCode() : 0);
        hash = 41 * hash + (this.endOfGame ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Game other = (Game) obj;
        if (this.player1OnTurn != other.player1OnTurn) {
            return false;
        }
        if (this.player1 != other.player1 && (this.player1 == null || !this.player1.equals(other.player1))) {
            return false;
        }
        if (this.player2 != other.player2 && (this.player2 == null || !this.player2.equals(other.player2))) {
            return false;
        }
        if (this.deck != other.deck && (this.deck == null || !this.deck.equals(other.deck))) {
            return false;
        }
        if (this.uncoveredCards != other.uncoveredCards) {
            return false;
        }
        if (this.newMove != other.newMove && (this.newMove == null || !this.newMove.equals(other.newMove))) {
            return false;
        }
        if (this.endOfGame != other.endOfGame) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Game{" + "player1OnTurn=" + player1OnTurn + ", player1=" + player1 + ", player2=" + player2 + ", deck=" + deck + ", uncoveredCards=" + uncoveredCards + ", newMove=" + newMove + ", endOfGame=" + endOfGame + '}';
    }

    public String getResult() {
        return result;
    }
}
