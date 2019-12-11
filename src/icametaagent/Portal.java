/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import icamessages.MessageType;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 */
public class Portal extends MetaAgent {

    protected HashMap<String, MetaAgent> routingTable;
    protected BlockingQueue<Message> messageQueue;

    /**
     * Creates new portal with specific node name
     * @param name
     */
    public Portal(String name) {
        super(name);
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.routingTable = new HashMap<>();
    }

    public MetaAgent getMetaAgent(String n) {
        return routingTable.get(n);
    }

    public void addAgent(MetaAgent meta) {
        routingTable.put(meta.getName(), meta);
    }

    public void removeAgent(String name) {
        routingTable.remove(name);
    }

    @Override
    public void sendMessage(MetaAgent agent, Message message) {
        if (message.getRecipient().equals(this.name)) {
            messageQueue.add(message);
        } else {
            routingTable.get(message.getRecipient()).sendMessage(this, message);
        }
    }

    @Override
    public void run() {
        while(true){
            if(!messageQueue.isEmpty()){
                try {
                    Message msg = messageQueue.take();
                    
                    //process the system message
                    if(msg.getMessageType().equals(MessageType.ADD_METAAGENT)){
                        
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
