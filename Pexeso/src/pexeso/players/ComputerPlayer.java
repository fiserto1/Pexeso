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
    public OneMove move(OneMove myLastMove, ArrayList<OneMove> oppMoves) {
        if (myLastMove != null) {
            if (myLastMove.getFirstCardCompareNumber() != -1) {
                if (correctMoves.containsKey(myLastMove.getFirstCardCompareNumber())) {
                    ArrayList<Integer> newCard = correctMoves.get(myLastMove.getFirstCardCompareNumber());
                    if (newCard.size() == 1 && newCard.get(0) != myLastMove.getFirstCardIDNumber()) {
                        newCard.add(myLastMove.getFirstCardIDNumber());
                    }
                } else {
                    ArrayList<Integer> newCard = new ArrayList<Integer>();
                    newCard.add(myLastMove.getFirstCardIDNumber());
                    correctMoves.put(myLastMove.getFirstCardCompareNumber(), newCard);
                }
            }
            if (myLastMove.getSecondCardCompareNumber() != -1) {
                if (correctMoves.containsKey(myLastMove.getSecondCardCompareNumber())) {
                    ArrayList<Integer> newCard = correctMoves.get(myLastMove.getSecondCardCompareNumber());
                    if (newCard.size() == 1 && newCard.get(0) != myLastMove.getSecondCardIDNumber()) {
                        newCard.add(myLastMove.getSecondCardIDNumber());
                    }
                } else {
                    ArrayList<Integer> newCard = new ArrayList<Integer>();
                    newCard.add(myLastMove.getSecondCardIDNumber());
                    correctMoves.put(myLastMove.getSecondCardCompareNumber(), newCard);
                }
            }
            
            //delete correct moves
            if (myLastMove.getFirstCardCompareNumber() == myLastMove.getSecondCardCompareNumber()) {
                if (correctMoves.containsKey(myLastMove.getFirstCardCompareNumber())) {
                    correctMoves.remove(myLastMove.getFirstCardCompareNumber());
                    uncoveredCards.add(myLastMove.getFirstCardIDNumber());
                    uncoveredCards.add(myLastMove.getSecondCardIDNumber());
                }
            }
        }
        
        if (oppMoves != null) {
            for (int i = 0; i < oppMoves.size(); i++) {
                if (oppMoves.get(i).getFirstCardCompareNumber() != -1) {
                    if (correctMoves.containsKey(oppMoves.get(i).getFirstCardCompareNumber())) {
                        ArrayList<Integer> newCard = correctMoves.get(oppMoves.get(i).getFirstCardCompareNumber());
                        if (newCard.size() == 1 && newCard.get(0) != oppMoves.get(i).getFirstCardIDNumber()) {
                            newCard.add(oppMoves.get(i).getFirstCardIDNumber());
                        }
                    } else {
                        ArrayList<Integer> newCard = new ArrayList<Integer>();
                        newCard.add(oppMoves.get(i).getFirstCardIDNumber());
                        correctMoves.put(oppMoves.get(i).getFirstCardCompareNumber(), newCard);
                    }
                }
                if (oppMoves.get(i).getSecondCardCompareNumber() != -1) {
                    if (correctMoves.containsKey(oppMoves.get(i).getSecondCardCompareNumber())) {
                        ArrayList<Integer> newCard = correctMoves.get(oppMoves.get(i).getSecondCardCompareNumber());
                        if (newCard.size() == 1 && newCard.get(0) != oppMoves.get(i).getSecondCardIDNumber()) {
                            newCard.add(oppMoves.get(i).getSecondCardIDNumber());
                        }
                    } else {
                        ArrayList<Integer> newCard = new ArrayList<Integer>();
                        newCard.add(oppMoves.get(i).getSecondCardIDNumber());
                        correctMoves.put(oppMoves.get(i).getSecondCardCompareNumber(), newCard);
                    }
                }

                //delete correct moves
                if (oppMoves.get(i).getFirstCardCompareNumber() == oppMoves.get(i).getSecondCardCompareNumber()) {
                    if (correctMoves.containsKey(oppMoves.get(i).getFirstCardCompareNumber())) {
                        correctMoves.remove(oppMoves.get(i).getFirstCardCompareNumber());
                        uncoveredCards.add(oppMoves.get(i).getFirstCardIDNumber());
                        uncoveredCards.add(oppMoves.get(i).getSecondCardIDNumber());
                    }
                }
            }
        }
        
        for (Map.Entry<Integer, ArrayList<Integer>> entry : correctMoves.entrySet()) {
            if (entry.getValue().size() == 2) {
//                System.out.println("Playing remembered move: " + entry.getValue().get(0)+","+ entry.getValue().get(1) );
                return new OneMove(entry.getValue().get(0), entry.getValue().get(1));
            }
        }
        Random rnd = new Random();
        int firstTurn;
        int secondTurn;
        boolean pass = false;
        while (!pass) {
            firstTurn = rnd.nextInt(DeckOfCards.NUMBER_OF_CARDS);
            secondTurn = rnd.nextInt(DeckOfCards.NUMBER_OF_CARDS);
            if (firstTurn == secondTurn) {
                continue;
            }
            if (uncoveredCards.contains(firstTurn) || uncoveredCards.contains(secondTurn)) {
                continue;
            }
            
//            System.out.println("playing random: " + firstTurn + "," + secondTurn);
            return new OneMove(firstTurn, secondTurn);
        }
        
        return new OneMove(0, 1);
    }
}
