/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import java.util.HashMap;

/**
 *
 * @author v8036651
 */
public class Portal extends MetaAgent {

    protected HashMap<String, MetaAgent> routingTable;

    /**
     * Creates new portal with specific node name
     *
     * @param name
     */
    public Portal(String name) {
        super(name);
        this.routingTable = new HashMap<>();
    }

    public MetaAgent getMetaAgent(String n) {
        return routingTable.get(n);
    }

    public void addAgent(String name, MetaAgent meta) {
        routingTable.put(name, meta);
    }

    public void removeAgent(String name) {
        routingTable.remove(name);
    }

    @Override
    public void sendMessage(MetaAgent agent, Message message) {
        if (message.getRecipient().equals(this.name)) {
            switch (message.getMessageType()) {
                case ADD_METAAGENT:
                    if(isNameAllowed(message.getSender())){
                        addAgent(message.getSender(), agent);
                    }else{
                        System.out.println("Error message:" + message.getMessageDetails());
                    }
                    break;
                case REMOVE_METAAGENT:
                    removeAgent(message.getSender());
                    break;
                case USER_MSG:
                    System.out.println("UserMessage: " + message.getMessageDetails());
                    break;
                default:
                    System.out.println("Error:" + message.getMessageDetails());
                    break;
            }
        } else {
            routingTable.get(message.getRecipient()).sendMessage(this, message);
        }
    }
    
    private boolean isNameAllowed(String name){
        return routingTable.get(name) != null;
    }
}
