/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso;

import pexeso.games.Difficulty;

/**
 * Trida pro nasteveni hry. (obtiznost hry, pocet karet ve hre)
 *
 * @author Tomas
 */
public class Settings {

    private int numberOfCards;
    private Difficulty difficulty;

    /**
     *
     * @param numberOfCards Pocet hracich karet.
     * @param difficulty Obtiznost PC protihrace.
     */
    public Settings(int numberOfCards, Difficulty difficulty) {
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
     *
     * @param numberOfCards Pocet karet.
     */
    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    /**
     * 1 - lehka, 2 - stredni, 3 - tezka.
     *
     * @return Vrati obtiznost hry.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Nastavi obtiznost hry. 1 - lehka, 2 - stredni, 3 - tezka.
     *
     * @param difficulty Obtiznost hry.
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        if (numberOfCards != settings.numberOfCards) return false;
        return difficulty == settings.difficulty;
    }

    @Override
    public int hashCode() {
        int result = numberOfCards;
        result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Settings{" + "numberOfCards=" + numberOfCards + ", difficulty=" + difficulty + '}';
    }
}
