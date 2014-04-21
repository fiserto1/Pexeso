/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso;

import pexeso.delegates.ScoreDelegate;

/**
 *
 * @author Tomas
 */
public class Score {
    
    private ScoreDelegate delegate;
    private int value;

    public void increaseValue() {
        setValue(value + 10);
    }
    
    public void resetValue() {
        setValue(0);
    }
    public void setValue(int value) {
        this.value = value;
        if (delegate != null) {
            delegate.scoreChanged(this);
        }
    }
    
    public void setDelegate(ScoreDelegate delegate) {
        this.delegate = delegate;
    }
    
    
}
