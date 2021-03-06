/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.cards;

import pexeso.delegates.CardDelegate;

import java.io.Serializable;

/**
 * Trida pro jednu karticku.
 *
 * @author Tomas
 */
public class Card implements Serializable {

    /**
     * Zadni strana karty. (rub)
     */
    private final String reverseSide = "CARD";
    /**
     * Porovnavaci cislo karty.
     */
    private int compareNumber;
    /**
     * ID karty.
     */
    private int idNumber;
    /**
     * Delegat karty.
     */
    private transient CardDelegate delegate;
    /**
     * true - karta jiz byla uhadnuta. (jiz neni soucasti hry), false - karta
     * zatim nebyla uhadnuta.
     */
    private boolean discovered;

    /**
     * true - karta je otocena licem vzhuru
     * false - karta je otocena rubem vzhuru
     */
    private boolean isVisible;

    public Card(int compareNumber, int idNumber) {
        this.compareNumber = compareNumber;
        this.idNumber = idNumber;
    }

    public Card() {
    }

    /**
     * Uhodne kartu a schova kartu.
     */
    public void hideCard() {
        this.isVisible = false;
        discovered = true;
        if (delegate != null) {
            delegate.cardRevealed(this);
        }
    }

    /**
     * Otoci kartu licem vzhuru.
     */
    public void showCard() {
        this.isVisible = true;
        if (delegate != null) {
            delegate.cardShowed(this);
        }
    }

    /**
     * Otoci kartu rubem vzhuru.
     */
    public void turnBack() {
        this.isVisible = false;
        if (delegate != null) {
            delegate.cardTurnedBack(this);
        }
    }

    /**
     *
     * @return Vrati porovnavaci cislo karty.
     */
    public int getCompareNumber() {
        return compareNumber;
    }

    /**
     * Nastavi porovnavaci cislo karty.
     *
     * @param compareNumber porovnavaci cislo karty.
     */
    public void setCompareNumber(int compareNumber) {
        this.compareNumber = compareNumber;
    }

    /**
     *
     * @return Vrati ID karty.
     */
    public int getIdNumber() {
        return idNumber;
    }

    /**
     * Nastavi ID karty.
     *
     * @param idNumber ID karty.
     */
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Vrati rub karty.
     *
     * @return Napis rubu karty.
     */
    public String getReverseSide() {
        return reverseSide;
    }

    /**
     * Nastavi delegata karty.
     *
     * @param delegate Delegat karty.
     */
    public void setDelegate(CardDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * true - karta jiz byla uhadnuta. (jiz neni soucasti hry) false - karta
     * zatim nebyla uhadnuta.
     *
     * @return Vrati zda byla karta uhadnuta.
     */
    public boolean isDiscovered() {
        return discovered;
    }

    /**
     * Nastavi zda byla karta uhadnuta. true - karta jiz byla uhadnuta. (jiz
     * neni soucasti hry) false - karta zatim nebyla uhadnuta.
     *
     * @param discovered uhodnoto
     */
    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.compareNumber;
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
        final Card other = (Card) obj;
        return this.compareNumber == other.compareNumber;
    }

    @Override
    public String toString() {
        return "Card{" + "reverseSide=" + reverseSide + ", compareNumber=" + compareNumber + ", idNumber=" + idNumber + ", delegate=" + delegate + ", discovered=" + discovered + '}';
    }

    public boolean isVisible() {
        return isVisible;
    }
}
