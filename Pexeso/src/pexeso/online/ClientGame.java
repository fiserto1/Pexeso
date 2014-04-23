/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pexeso.AbstractPlayer;
import pexeso.HumanPlayer;

/**
 *
 * @author Tomas
 */
public class ClientGame implements Runnable {
    

    @Override
    public void run() {
        Socket clientSocket = null;
        ObjectOutputStream objOutStream = null;
        ObjectInputStream objInStream = null;

        try {
            clientSocket = new Socket("127.0.0.1", 4444);
            objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objInStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
        }

        AbstractPlayer serverPlayer = null;
        AbstractPlayer clientPlayer = new HumanPlayer("Tomas", null, 2);
        
        try {
            objOutStream.writeObject(clientPlayer);
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            serverPlayer = (AbstractPlayer) objInStream.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.out.println(serverPlayer.getName() + " joined.");
        
        try {
            objOutStream.close();
            objInStream.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
