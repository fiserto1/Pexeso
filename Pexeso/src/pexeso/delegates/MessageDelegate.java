/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.delegates;

import pexeso.Message;

/**
 *
 * @author Tomas
 */
public interface MessageDelegate {
    
    public void errorMessageChanged(Message mess);
    
    public void headMessageChanged(Message mess);
}
