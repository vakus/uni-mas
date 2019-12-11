/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import icamessages.MessageType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author v8036651
 */
public class Portal extends MetaAgent {

    protected HashMap<String, MetaAgent> routingTable;
    /**
     * This list only stores list of socketAgents which are to be sent messages only unique
     * ps domk argument with patryk
     */
    private ArrayList<SocketAgent> socketAgents;

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
    public void messageHandler(MetaAgent agent, Message message) {
        System.out.println(message.toString());
        if (message.getRecipient().equals(this.name) || message.getRecipient().equalsIgnoreCase("GLOBAL")) {
            switch (message.getMessageType()) {
                case ADD_METAAGENT:
                    System.out.println(message.getMessageType().toString());
                    if (isNameAllowed(message.getSender())) {
                        addAgent(message.getSender(), agent);

                        
                        
                        for (String s : routingTable.keySet()) {
                            if (routingTable.get(s) instanceof SocketAgent){
                                routingTable.get(s).messageHandler(this, message);
                                if(this instanceof Portal){
                                    break;
                                }
                            }
                            
                        }

                    } else {
                        System.out.println("Username not allowed: " + message.getSender());
                    }
                    break;
                case REMOVE_METAAGENT:
                    System.out.println(message.getMessageType().toString());
                    removeAgent(message.getSender());
                    break;
                case USER_MSG:
                    System.out.println(message.getMessageType().toString());
                    System.out.println("UserMessage: " + message.getMessageDetails());
                    break;
                case ADD_PORTAL:
                    System.out.println(message.getMessageType().toString());

                    routingTable.put(message.getSender(), agent);

                    String values = "";
                    for (String key : routingTable.keySet()) {
                        System.out.println("k: " + key);
                        values += key + "\n";
                    }

                    agent.messageHandler(this, new Message(this.name, message.getSender(), MessageType.LOAD_TABLE, values));
                    break;
                case REMOVE_PORTAL:
                    System.out.println(message.getMessageType().toString());

                    break;
                case LOAD_TABLE:
                    System.out.println(message.getMessageType().toString());

                    String[] values2 = message.getMessageDetails().split("\n");
                    for (String s : values2) {
                        addAgent(s, agent);
                    }
                    break;
                default:
                    System.out.println(message.getMessageType().toString());
                    System.out.println("Error:" + message.getMessageDetails());
                    break;
            }
        } else {
            routingTable.get(message.getRecipient()).messageHandler(this, message);
        }
    }

    public boolean isNameAllowed(String name) {
        return routingTable.get(name) != null;
    }

}
