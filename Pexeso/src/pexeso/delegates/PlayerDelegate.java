/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.delegates;

import pexeso.cards.CardAL;
import pexeso.cards.DeckOfCards;
import pexeso.players.AbstractPlayer;

/**
 * Interface pro delegata hrace
 *
 * @author Tomas
 */
public interface PlayerDelegate {

    /**
     * Oznami, ze skore hrace bylo zmeneno.
     *
     * @param player Hrac.
     */
    public void scoreChanged(AbstractPlayer player);

    /**
     * Oznami, ze jmeno hrace bylo zmeneno.
     *
     * @param player Hrac.
     */
    public void nameChanged(AbstractPlayer player);

    /**
     * Oznami, ze avatar hrace byl zmenen.
     *
     * @param player Hrac.
     */
    public void avatarChanged(AbstractPlayer player);

    /**
     * Zobrazi hrace na hraci desce.
     *
     * @param player Hrac.
     */
    public void showPlayerOnBoard(AbstractPlayer player);

    /**
     * Aktivuje tlacitka pro karty, aby mohl hrac tahnout.
     *
     * @param listener Posluchac karty.
     */
    public void activateCards(CardAL listener);

    /**
     * Deaktivuje tlacitka pro karty, aby hrac jiz nemohl tahnout.
     *
     * @param listener Posluchac karty.
     */
    public void deactivateCards(CardAL listener);

    /**
     * Obnovi a zobrazi hraci desku.
     *
     * @param deck Balicek karet.
     */
    public void refreshDeck(DeckOfCards deck);
}
