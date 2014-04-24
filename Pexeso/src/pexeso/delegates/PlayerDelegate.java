/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.delegates;

import pexeso.players.AbstractPlayer;

/**
 *
 * @author Tomas
 */
public interface PlayerDelegate {
    
    public void scoreChanged(AbstractPlayer player);
    
    public void nameChanged(AbstractPlayer player);
    
    public void avatarChanged(AbstractPlayer player);
}
