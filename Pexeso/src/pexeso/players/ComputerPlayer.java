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
import pexeso.cards.DeckOfCards;
import pexeso.OneMove;

/**
 *
 * @author Tomas
 */
public class ComputerPlayer extends AbstractPlayer {
    
    private TreeMap<Integer, ArrayList<Integer>> correctMoves;
    private ArrayList<Integer> uncoveredCards;

    public ComputerPlayer(String playerName, ImageIcon avatar, int playerNumber) {
        super(playerName, avatar, playerNumber);
        correctMoves = new TreeMap<Integer, ArrayList<Integer>>();
        uncoveredCards = new ArrayList<Integer>();
    }
    
    @Override
    public OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves, int numberOfCards) {
        saveToMemory(myLastMove);
        
        if (oppMoves != null) {
            for (int i = 0; i < oppMoves.size(); i++) {
                saveToMemory(oppMoves.get(i));
            }
        }
        
        for (Map.Entry<Integer, ArrayList<Integer>> entry : correctMoves.entrySet()) {
            if (entry.getValue().size() == 2) {
//                System.out.println("Playing remembered move: " + entry.getValue().get(0)+","+ entry.getValue().get(1) );
                return new OneMove(entry.getValue().get(0), entry.getValue().get(1));
            }
        }
        
        return playRandom(numberOfCards);
    }
    
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

//            System.out.println("playing random: " + firstTurn + "," + secondTurn);
            return new OneMove(firstTurn, secondTurn);
        }
        return null;
    }

    private void saveToMemory(OneMove move) {
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

    private void saveToMap(int compareNumber, int idNumber) {
        if (compareNumber != -1) {
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
