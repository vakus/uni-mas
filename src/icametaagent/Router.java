/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author v8036651
 */
public class Router extends Portal {
    protected ServerSocket server;
    
    public Router(String n) throws IOException 
    {
        super(n);
        server = new ServerSocket(42069);
    }

    @Override
    public void run() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
