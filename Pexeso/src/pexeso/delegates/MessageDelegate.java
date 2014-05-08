/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.delegates;

import pexeso.Message;

/**
 * Interface pro delegata zpravy.
 * @author Tomas
 */
public interface MessageDelegate {
    
    /**
     * Oznami, ze neprizniva zprava byla zmenena.
     * @param mess Neprizniva zprava.
     */
    public void errorMessageChanged(Message mess);
    
    /**
     * Oznami, ze hlavni zprava byla zmenena.
     * @param mess Hlavni zprava.
     */
    public void headMessageChanged(Message mess);
}
