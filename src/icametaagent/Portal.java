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
     * This list only stores list of socketAgents which are to be sent messages
     * only unique.
     */
    private ArrayList<SocketAgent> socketAgents;
    /**
     * Creates new portal with specific node name.
     *
     * @param name
     */
    public Portal(String name) {
        super(name);
        this.routingTable = new HashMap<>();
        this.socketAgents = new ArrayList<>();
    }
    
    
    /**
     * Returns an agent that is within the routing table with the key of n.
     *
     * @param name
     * @return
     */
    public MetaAgent getMetaAgent(String name) {
        return routingTable.get(name);
    }

    /**
     * Adds a new agent to the routing table of the portal.
     *
     * @param name
     * @param meta
     * @author v8073331
     */
    public void addAgent(String name, MetaAgent meta) {
        if(!(meta instanceof SocketAgent && this instanceof Portal && socketAgents.isEmpty())){
            routingTable.put(name, meta);
            if(meta instanceof SocketAgent){
                socketAgents.add((SocketAgent) meta);
            }
        }
    }

    /**
     * Removes the agent in location n from the routing table of the portal.
     *
     * @param name
     */
    public void removeAgent(String name) {
        routingTable.remove(name);
    }

    /**
     * Method that is drawn from super class, This is the method that handles a
     * message object, It reads the message Type and acts upon it.
     *
     * @param agent
     * @param message
     */
    @Override
    public void messageHandler(MetaAgent agent, Message message) {
        System.out.println(this.name + "Processing: " + message.toString());
        if (message.getRecipient().equals(this.name) || message.getRecipient().equalsIgnoreCase("GLOBAL")) {

            synchronized (routingTable) {
                switch (message.getMessageType()) {
                    case ADD_METAAGENT:
                        System.out.println(this.name + "Adding " + message.toString());
                        if (isNameAllowed(message.getSender())) {
                            addAgent(message.getSender(), agent);

                            for (SocketAgent sa : socketAgents) {
                                if (!sa.equals(agent)) {
                                    sa.messageHandler(this, message);
                                }
                            }

                        } else {
                            System.out.println("Username not allowed: " + message.getSender());
                        }
                        break;
                    case REMOVE_METAAGENT:
                        removeAgent(message.getSender());

                        for (SocketAgent sa : socketAgents) {
                            if (!sa.equals(agent)) {
                                sa.messageHandler(this, message);
                            }
                        }

                        break;
                    case USER_MSG:
                        System.out.println("UserMessage: " + message.getMessageDetails());
                        break;
                    case ADD_PORTAL:

                        addAgent(message.getSender(), agent);

                        for (SocketAgent sa : socketAgents) {
                            if (!sa.equals(agent)) {
                                sa.messageHandler(this, new Message(message.getSender(), "GLOBAL", MessageType.ADD_METAAGENT, ""));
                            }
                        }

                        String values = "";
                        for (String key : routingTable.keySet()) {
                            values += key + "\n";
                        }
                        values += this.name + "\n";

                        agent.messageHandler(this, new Message(this.name, message.getSender(), MessageType.LOAD_TABLE, values));
                        break;
                    case REMOVE_PORTAL:

                        break;
                    case LOAD_TABLE:

                        String[] values2 = message.getMessageDetails().split("\n");
                        for (String s : values2) {
                            addAgent(s, agent);
                        }
                        break;
                    default:
                        System.out.println("Error:" + message.getMessageDetails());
                        break;
                }
            }
        } else {
            if (routingTable.containsKey(message.getRecipient())) {
                routingTable.get(message.getRecipient()).messageHandler(this, message);
            } else {
                agent.messageHandler(this, new Message(this.getName(), message.getSender(), MessageType.ERROR, "An error occured"));
            }
        }
    }

    /**
     * Returns a boolean value, This checks if the name that is being used is in
     * use in the portals routing table.
     *
     * @param name
     * @return
     */
    public boolean isNameAllowed(String name) {
        return routingTable.get(name) == null;
    }

}
