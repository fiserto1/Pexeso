/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso.games;

import java.io.Serializable;

/**
 * Trida pro jeden tah, tj. jedna dvojice karet.
 *
 * @author Tomas
 */
public class OneMove implements Serializable {

    private int firstCardIDNumber;
    /**
     * Porovnavaci cislo prvni otocene karty.
     */
    private int firstCardCompareNumber;
    private int secondCardIDNumber;
    /**
     * Porovnavaci cislo druhe otocene karty.
     */
    private int secondCardCompareNumber;

    /**
     * Nastavi ID obou karet. Porovnavaci cisla karet jsou -1. (zatim je cislo
     * zatajeno)
     *
     * @param firstCardIDNumber ID prvni otocene karty.
     * @param secondCardIDNumber ID druhe otocene karty.
     */
    public OneMove(int firstCardIDNumber, int secondCardIDNumber) {
        this.firstCardIDNumber = firstCardIDNumber;
        this.secondCardIDNumber = secondCardIDNumber;
        this.firstCardCompareNumber = -1;
        this.secondCardCompareNumber = -1;
    }

    /**
     *
     * @return Vraci ID prvni otocene karty.
     */
    public int getFirstCardIDNumber() {
        return firstCardIDNumber;
    }

    /**
     * Nastavi ID prvni otocene karty.
     *
     * @param firstCardIDNumber ID prvni otocene karty.
     */
    public void setFirstCardIDNumber(int firstCardIDNumber) {
        this.firstCardIDNumber = firstCardIDNumber;
    }

    /**
     *
     * @return Vraci porovnavaci cislo prvni otocene karty.
     */
    public int getFirstCardCompareNumber() {
        return firstCardCompareNumber;
    }

    /**
     * Nastavi porovnavaci cislo prvni otocene karty.
     *
     * @param firstCardCompareNumber Porovnavaci cislo prvni otocene karty.
     */
    public void setFirstCardCompareNumber(int firstCardCompareNumber) {
        this.firstCardCompareNumber = firstCardCompareNumber;
    }

    /**
     *
     * @return Vraci ID druhe otocene karty.
     */
    public int getSecondCardIDNumber() {
        return secondCardIDNumber;
    }

    /**
     * Nastavi ID druhe otocene karty.
     *
     * @param secondCardIDNumber ID druhe otocene karty.
     */
    public void setSecondCardIDNumber(int secondCardIDNumber) {
        this.secondCardIDNumber = secondCardIDNumber;
    }

    /**
     *
     * @return Vraci porovnavaci cislo druhe otocene karty.
     */
    public int getSecondCardCompareNumber() {
        return secondCardCompareNumber;
    }

    /**
     * Nastavi porovnavaci cislo druhe otocene karty.
     *
     * @param secondCardCompareNumber Porovnavaci cislo druhe otocene karty.
     */
    public void setSecondCardCompareNumber(int secondCardCompareNumber) {
        this.secondCardCompareNumber = secondCardCompareNumber;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.firstCardIDNumber;
        hash = 53 * hash + this.secondCardIDNumber;
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
        final OneMove other = (OneMove) obj;
        if (this.firstCardIDNumber != other.firstCardIDNumber) {
            return false;
        }
        if (this.secondCardIDNumber != other.secondCardIDNumber) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OneMove{" + "firstCardIDNumber=" + firstCardIDNumber + ", firstCardCompareNumber=" + firstCardCompareNumber + ", secondCardIDNumber=" + secondCardIDNumber + ", secondCardCompareNumber=" + secondCardCompareNumber + '}';
    }
}
