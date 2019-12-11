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
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 */
public class SocketAgent extends MetaAgent implements Runnable {

    protected Portal portalConection;
    protected Socket routerConnection;
    private Worker worker;

    public SocketAgent(String name, Portal p, Socket s) {
        super(name);
        portalConection = p;
        routerConnection = s;
        worker = new Worker(this);
    }

    @Override
    public void messageHandler(MetaAgent agent, Message msg) {
        try {
            routerConnection.getOutputStream().write(msg.toString().getBytes());
            routerConnection.getOutputStream().flush();
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
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Worker implements Runnable{

    public BlockingQueue<Message> messageQueue;
    private SocketAgent agent;
    
    public Worker(SocketAgent agent){
        this.agent = agent;
    }
    
    @Override
    public void run() {
        while(true){
            try {
                agent.portalConection.messageHandler(agent, messageQueue.take());
            } catch (InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}