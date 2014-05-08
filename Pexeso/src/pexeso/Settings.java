/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso;

/**
 * Trida pro nasteveni hry. (obtiznost hry, pocet karet ve hre)
 * @author Tomas
 */
public class Settings {

    private int numberOfCards;
    private int difficulty;

    /**
     * 
     * @param numberOfCards Pocet hracich karet.
     * @param difficulty Obtiznost PC protihrace.
     */
    public Settings(int numberOfCards, int difficulty) {
        this.numberOfCards = numberOfCards;
        this.difficulty = difficulty;
    }

    /**
     * 
     * @return Vrati pocet karet se kterymi se ma hrat.
     */
    public int getNumberOfCards() {
        return numberOfCards;
    }

    /**
     * Nastavi pocet karet se kterymi se ma hrat.
     * @param numberOfCards Pocet karet.
     */
    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    /**
     * 1 - lehka
     * 2 - stredni
     * 3 - tezka
     * @return Vrati obtiznost hry.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Nastavi obtiznost hry.
     * 1 - lehka
     * 2 - stredni
     * 3 - tezka
     * @param difficulty Obtiznost hry.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.numberOfCards;
        hash = 53 * hash + this.difficulty;
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
        final Settings other = (Settings) obj;
        if (this.numberOfCards != other.numberOfCards) {
            return false;
        }
        if (this.difficulty != other.difficulty) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Settings{" + "numberOfCards=" + numberOfCards + ", difficulty=" + difficulty + '}';
    }
}
