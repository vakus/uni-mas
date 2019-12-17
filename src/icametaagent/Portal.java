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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
    private Observable observers;
  
    /**
     * Creates new portal with specific node name.
     * @param name 
     * @author v8036651
     */
    public Portal(String name) 
    {
        super(name);
        this.routingTable = new HashMap<>();
        this.socketAgents = new ArrayList<>();
        this.observers = new Observable();
    }
    
    
    /**
     * MEthod for adding an observer to be used when handling a message.
     * @param obs 
     * @author v8036651
     */
    public void addObserver(Observer obs)
    {
        observers.addObserver(obs);
    }
    /**
     * Returns an agent that is within the routing table with the key of n.
     * @param name
     * @return 
     * @author v8036651
     */
    public MetaAgent getMetaAgent(String name) 
    {
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
     * @param name 
     * @author v8036651
     */
    public void removeAgent(String name) 
    {
        routingTable.remove(name);
    }

    /**
     * Method that is drawn from super class, This is the method that handles a
     * message object, It reads the message Type and acts upon it.
     *
     * @param agent
     * @param message 
     * @author v8073331
     */
    @Override
    public void messageHandler(MetaAgent agent, Message message) 
    {
        observers.notifyObservers(message);
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

                        if (this instanceof Portal){
                            break;
                        }
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

     * Returns a boolean value,
     * This checks if the metaagent name is valid and doesn't already exist
     * @param name metaagent name to be added
     * @return true if metaagent name allowed and doesn't already exists
     * @author v8243060 & v8036651
     */
    protected boolean isNameAllowed(String name) {
        return (routingTable.get(name) == null && usernameValidation(name));
    }
    
    /**
     * Checks whether the message came from the correct branch/agent
     * @param agent metaagent that send/propagated the message
     * @param msg message sent
     * @return true if metaagent in the routing table for the message sender is 
     * the same as the metaagent who sent the message
     * @author v8243060
     */
    protected boolean isMessageOriginCorrect(MetaAgent agent, Message msg){
        return (agent.equals(this.routingTable.get(msg.getSender())));
    }
}
