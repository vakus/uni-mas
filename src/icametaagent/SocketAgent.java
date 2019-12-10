/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import java.net.Socket;

/**
 *
 * @author v8036651
 */
public class SocketAgent extends MetaAgent
{
    protected Portal portalConection;
    protected Socket routerConnection;
    
    public SocketAgent(String name, Portal p, Socket s) 
    {
        super(name);
        portalConection = p;
        routerConnection = s;
    }

    @Override
    public void sendMessage(Message m) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message receiveMessage() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
