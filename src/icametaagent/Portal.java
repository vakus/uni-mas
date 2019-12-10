/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author v8036651
 */
public class Portal extends MetaAgent {

    protected HashMap<String, MetaAgent> routingTable;
    protected BlockingQueue<Message> messageQueue;
    

    /**
     *
     * @param n
     */
    public Portal(String n) 
    {
        super(n);
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.routingTable = new HashMap<>();
    }

    public MetaAgent getMetaAget(String n) 
    {
        return routingTable.get(n);
    }
    
    public void addAgent(MetaAgent meta)
    {
        routingTable.put(meta.getName(), meta);
    }
    
    public void removeAgent(String name)
    {
        routingTable.remove(name);
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
