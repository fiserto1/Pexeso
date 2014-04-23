/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pexeso.online;

import com.sun.corba.se.pept.encoding.InputObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pexeso.AbstractPlayer;
import pexeso.HumanPlayer;

/**
 *
 * @author Tomas
 */
public class ServerGame implements Runnable {
    
    
    @Override
    public void run() {
        ServerSocket serverSock = null;
        try {
            serverSock = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
        }

        Socket clientSock = null;
        try {
            clientSock = serverSock.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
        }

        ObjectOutputStream objOutStream = null;
        ObjectInputStream objInStream = null;
        try {
            objOutStream = new ObjectOutputStream(clientSock.getOutputStream());
            objInStream = new ObjectInputStream(clientSock.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        AbstractPlayer clientPlayer = null;
        AbstractPlayer serverPlayer = new HumanPlayer("Tonda", null, 1);
        
        try {
            clientPlayer = (AbstractPlayer) objInStream.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            objOutStream.writeObject(serverPlayer);
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.out.println(clientPlayer.getName() + " joined.");
        
        
        try {
            objOutStream.close();
            objInStream.close();
            clientSock.close();
            serverSock.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
