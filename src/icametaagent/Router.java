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

    public Router(String n) throws IOException {
        super(n);
        server = new ServerSocket(42069);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket s = server.accept();
                SocketAgent newAgent = new SocketAgent("socket", this, s);
                
                Thread thread = new Thread(newAgent);
                thread.start();
                
                
            } catch (IOException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
