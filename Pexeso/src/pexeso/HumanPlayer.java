/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

/**
 *
 * @author Tomas
 */
public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(String playerName) {
        super(playerName);
    }

    public HumanPlayer() {
        this.playerName = "UnknownPlayer";
    }
    
    @Override
    public OneMove move(DeckOfCards deck) {
        for (int i = 0; i < deck.getCards().length; i++) {
            deck.getCards()[i].addActionListener(new CardAL(deck.getCards()[i]));
        }
        
//        boolean valid = false;
//        while(!valid) {
//            if (firstClickedCard == null) {
//                firstClickedCard = CardAL.getClickedCard();
//            }
//            else {
//                secondClickedCard = CardAL.getClickedCard();
//                valid = true;
//            }
//        }
        
        OneMove move = CardAL.getMove();
        return move;
    }
    

}
