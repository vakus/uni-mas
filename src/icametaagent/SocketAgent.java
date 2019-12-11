/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 */
public class SocketAgent extends MetaAgent implements Runnable {

    protected Portal portalConection;
    protected Socket routerConnection;

    public SocketAgent(String name, Portal p, Socket s) {
        super(name);
        portalConection = p;
        routerConnection = s;
    }

    @Override
    public void sendMessage(MetaAgent agent, Message msg) {
        try {
            routerConnection.getOutputStream().write(msg.toString().getBytes());
        } catch (IOException ex) {
            Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * rmsg = recived message
     * armsg = actual recived message
     * farmsg = final actual message to be sent
     */
    @Override
    public void run() {

        try {
            InputStream in = routerConnection.getInputStream();

            while (!routerConnection.isClosed()) {

                
                if(in.available() != 0){
                    byte[] rmsg = new byte[in.available()];
                    in.read(rmsg);
                    
                    String armsg = new String(rmsg);
                    Message farmsg = Message.parseMessage(armsg);
                    portalConection.sendMessage(this, farmsg);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
