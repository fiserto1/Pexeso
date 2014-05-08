/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.players;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import pexeso.OneMove;

/**
 * Trida pro pocitacoveho hrace. Hrac hraje s pameti 8 predchozich tahu.
 * Rozsiruje AbstractPlayer.
 *
 * @author Tomas
 */
public class ComputerPlayer extends AbstractPlayer {

    private final TreeMap<Integer, ArrayList<Integer>> correctMoves;
    private final ArrayList<Integer> uncoveredCards;
    private final ArrayList<int[]> memory;

    /**
     *
     * @param playerName Jmeno hrace.
     * @param avatar Avatar hrace.
     * @param playerNumber Cislo hrace.
     */
    public ComputerPlayer(String playerName, ImageIcon avatar, int playerNumber) {
        super(playerName, avatar, playerNumber);
        correctMoves = new TreeMap<Integer, ArrayList<Integer>>();
        uncoveredCards = new ArrayList<Integer>();
        memory = new ArrayList<int[]>();
    }

    @Override
    public OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves, int numberOfCards) {

        refreshMemory();
        deleteOldMemory();
        saveMoves(myLastMove);

        if (oppMoves != null) {
            for (int i = 0; i < oppMoves.size(); i++) {
                saveMoves(oppMoves.get(i));
            }
        }

        for (Map.Entry<Integer, ArrayList<Integer>> entry : correctMoves.entrySet()) {
            if (entry.getValue().size() == 2) {
                return new OneMove(entry.getValue().get(0), entry.getValue().get(1));
            }
        }

        return playRandom(numberOfCards);
    }

    /**
     * Posune stari tahu.
     */
    private void refreshMemory() {
        for (int[] card : memory) {
            card[0]++;
        }
    }

    /**
     * Jakmile dosahne protihracuv nebo hracuv tah "stari" 5, tak se smaze.
     * Stari se pricita a maze jeste pred pred vyhodnocenim taktiky, tudiz
     * opravdove stari je 4 (4 x 2 hraci) tj. 8 tahu zpet.
     */
    private void deleteOldMemory() {
        for (int[] card : memory) {
            if (card[0] == 5) {
                if (correctMoves.containsKey(card[1])) {
                    ArrayList<Integer> oldCard = correctMoves.get(card[1]);
                    oldCard.remove((Integer) card[2]);
                    if (oldCard.isEmpty()) {
                        correctMoves.remove(card[1]);
                    } else {
                        correctMoves.put(card[1], oldCard);
                    }
                }
            }
        }
    }

    /**
     * Kdyz hrac nevi kam hrat na jistotu, tak zvoli nahodny mozny tah.
     *
     * @param numberOfCards Uvodni pocet karet.
     * @return Vrati nahodny tah.
     */
    private OneMove playRandom(int numberOfCards) {
        Random rnd = new Random();
        int firstTurn;
        int secondTurn;
        boolean pass = false;
        while (!pass) {
            firstTurn = rnd.nextInt(numberOfCards);
            secondTurn = rnd.nextInt(numberOfCards);
            if (firstTurn == secondTurn) {
                continue;
            }
            if (uncoveredCards.contains(firstTurn) || uncoveredCards.contains(secondTurn)) {
                continue;
            }
            return new OneMove(firstTurn, secondTurn);
        }
        return null;
    }

    /**
     * Ulozi svuj predesly tah a protihracuv tah/y do pameti a mapy, pokud to ma
     * cenu.
     *
     * @param move
     */
    private void saveMoves(OneMove move) {
        if (move != null) {
            saveToMap(move.getFirstCardCompareNumber(), move.getFirstCardIDNumber());
            saveToMap(move.getSecondCardCompareNumber(), move.getSecondCardIDNumber());

            //delete correct moves
            if (move.getFirstCardCompareNumber() == move.getSecondCardCompareNumber()) {
                if (correctMoves.containsKey(move.getFirstCardCompareNumber())) {
                    correctMoves.remove(move.getFirstCardCompareNumber());
                    uncoveredCards.add(move.getFirstCardIDNumber());
                    uncoveredCards.add(move.getSecondCardIDNumber());
                }
            }
        }
    }

    /**
     * Ulozi tah do pameti.
     *
     * @param compareNumber Porovnavaci cislo karty.
     * @param idNumber ID cislo karty.
     */
    private void saveToMemory(int compareNumber, int idNumber) {
        //0 - age
        //1 - key
        //2 - value
        int[] card = {1, compareNumber, idNumber};
        memory.add(card);
    }

    /**
     * Ulozi tah do mapy. Klic je porovnavaci cislo a hodnota je jednice nebo
     * dvojice ID karticek.
     *
     * @param compareNumber Porovnavaci cislo karty.
     * @param idNumber ID cislo karty.
     */
    private void saveToMap(int compareNumber, int idNumber) {
        if (compareNumber != -1) {
            saveToMemory(compareNumber, idNumber);

            if (correctMoves.containsKey(compareNumber)) {
                ArrayList<Integer> newCard = correctMoves.get(compareNumber);
                if (newCard.size() == 1 && newCard.get(0) != idNumber) {
                    newCard.add(idNumber);
                }
            } else {
                ArrayList<Integer> newCard = new ArrayList<Integer>();
                newCard.add(idNumber);
                correctMoves.put(compareNumber, newCard);
            }
        }
    }
}
