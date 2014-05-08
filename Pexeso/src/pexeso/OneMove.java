/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexeso;

import java.io.Serializable;

/**
 *
 * @author Tomas
 */
public class OneMove implements Serializable {

    private int firstCardIDNumber;
    private int firstCardCompareNumber;
    private int secondCardIDNumber;
    private int secondCardCompareNumber;

    public OneMove(int firstCardIDNumber, int secondCardIDNumber) {
        this.firstCardIDNumber = firstCardIDNumber;
        this.secondCardIDNumber = secondCardIDNumber;
        this.firstCardCompareNumber = -1;
        this.secondCardCompareNumber = -1;
    }

    public int getFirstCardIDNumber() {
        return firstCardIDNumber;
    }

    public void setFirstCardIDNumber(int firstCardIDNumber) {
        this.firstCardIDNumber = firstCardIDNumber;
    }

    public int getFirstCardCompareNumber() {
        return firstCardCompareNumber;
    }

    public void setFirstCardCompareNumber(int firstCardCompareNumber) {
        this.firstCardCompareNumber = firstCardCompareNumber;
    }

    public int getSecondCardIDNumber() {
        return secondCardIDNumber;
    }

    public void setSecondCardIDNumber(int secondCardIDNumber) {
        this.secondCardIDNumber = secondCardIDNumber;
    }

    public int getSecondCardCompareNumber() {
        return secondCardCompareNumber;
    }

    public void setSecondCardCompareNumber(int secondCardCompareNumber) {
        this.secondCardCompareNumber = secondCardCompareNumber;
    }
}
