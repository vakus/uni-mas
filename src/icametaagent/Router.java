/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 */
public class Router extends Portal implements Runnable {
    
    protected ServerSocket server;

    /**
     * Constructor for a router object with a super class of portal.
     * @param n
     * @throws IOException 
     * @author v8036651
     */
    public Router(String n) throws IOException 
    {
        super(n);
        server = new ServerSocket(42069);
    }

    /**
     * Overwrites the run method from the implements runnable,
     * creates a new server socket to listen for new connections.
     * @author v8073331
     */
    @Override
    public void run() {
        while (!server.isClosed()) {
            try {
                Socket s = server.accept();
                System.out.println("Accepted connection from: " + s.getInetAddress().toString());
                SocketAgent newAgent = new SocketAgent(this, s);
                newAgent.start();
                
            } catch (IOException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    

}
