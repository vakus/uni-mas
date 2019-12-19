/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import icamessages.MessageType;
import icamonitors.Monitor;
import icamonitors.Observer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author v8036651
 * @author v8073331
 * @author v8243060
 */
public class Portal extends MetaAgent {

    protected final HashMap<String, MetaAgent> routingTable;
    /**
     * This list only stores list of socketAgents which are to be sent messages
     * only unique.
     */
    protected final ArrayList<SocketAgent> socketAgents;
    protected final Observer observers;

    /**
     * Creates new portal with specific node name.
     *
     * @param name
     * @author v8036651
     */
    public Portal(String name) {
        super(name);
        this.routingTable = new HashMap<>();
        this.socketAgents = new ArrayList<>();
        this.observers = new Observer();
    }

    /**
     * Method for adding an observer to be used when handling a message.
     *
     * @param obs
     * @author v8036651
     */
    public void addObserver(Monitor obs) {
        observers.addObserver(obs);
    }

    /**
     * Returns an agent that is within the routing table with the key of n.
     *
     * @param name
     * @return
     * @author v8036651
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
        if(!(routingTable.containsKey(name))){
            if(meta instanceof SocketAgent && !(socketAgents.contains((SocketAgent) meta))){
                if(socketAgents.isEmpty()){
                    socketAgents.add((SocketAgent) meta);
                    routingTable.put(name, meta);
                }else{
                    throw new IllegalArgumentException("Portal can not have more than one socket connection.");
                }
            }else{
                routingTable.put(name, meta);
            }
        }else{
            throw new IllegalArgumentException("Username is already in routing table.");
        }
    }

    /**
     * Removes the agent in location n from the routing table of the portal.
     *
     * @param name
     * @author v8036651
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
     * @author v8073331
     */
    @Override
    public void messageHandler(MetaAgent agent, Message message) {

        observers.updateReceiver(message);
        if (message.getRecipient().equals(this.name) || message.getRecipient().equalsIgnoreCase("GLOBAL")) {

            synchronized (routingTable) {
                switch (message.getMessageType()) {
                    case ADD_METAAGENT:
                        if (isNameAllowed(message.getSender())) {
                            addAgent(message.getSender(), agent);

                            for (SocketAgent sa : socketAgents) {
                                if (!sa.equals(agent)) {
                                    observers.updateSender(message);
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
                                observers.updateSender(message);
                                sa.messageHandler(this, message);
                            }
                        }

                        break;
                    case USER_MSG:
                        System.out.println("UserMessage: " + message.getMessageDetails());
                        break;

                    case ADD_PORTAL:
                        //portal should not be added to another instance of portal
                        if (!(this instanceof Router)) {
                            break;
                        }

                        String values = "";
                        for (String key : routingTable.keySet()) {
                            values += key + "\n";
                        }
                        values += this.name + "\n";

                        Message msg = new Message(this.name, message.getSender(), MessageType.LOAD_TABLE, values);
                        agent.messageHandler(this, msg);
                        observers.updateSender(msg);

                        addAgent(message.getSender(), agent);

                        for (SocketAgent sa : socketAgents) {
                            if (!sa.equals(agent)) {
                                Message msg2 = new Message(message.getSender(), "GLOBAL", MessageType.ADD_METAAGENT, "");
                                observers.updateSender(msg2);
                                sa.messageHandler(this, msg2);
                            }
                        }

                        break;
                    case REMOVE_PORTAL:

                        /**
                         * we assume that the portal was directly connected to
                         * us. This means that this message should never be
                         * processed by portal itself. This message should
                         * always come from SocketAgent.
                         */
                        if (!(this instanceof Router) || !(agent instanceof SocketAgent)) {
                            break;
                        }

                        socketAgents.remove((SocketAgent) agent);

                        ArrayList<String> usernames = new ArrayList(routingTable.keySet());
                        
                        //find all clients that will be removed by this action
                        for (String username : usernames) {
                            if (routingTable.get(username).equals(agent)) {
                                /**
                                 * This agent will be removed if the portal will
                                 * be removed
                                 */
                                routingTable.remove(username);
                                Message msg3 = new Message(username, "GLOBAL", MessageType.REMOVE_METAAGENT, "");
                                observers.updateSender(msg3);
                                
                                for(SocketAgent sa : socketAgents){
                                    sa.messageHandler(this, msg3);
                                }
                            }
                        }

                        ((SocketAgent)agent).close();
                        
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
                observers.updateSender(message);
                routingTable.get(message.getRecipient()).messageHandler(this, message);
            } else {
                agent.messageHandler(this, new Message(this.getName(), message.getSender(), MessageType.ERROR, "An error occured"));
            }
        }
    }

    /**
     *
     * Returns a boolean value, This checks if the MetaAgent name is valid and
     * doesn't already exist
     *
     * @param name MetaAgent name to be added
     * @return true if MetaAgent name allowed and doesn't already exists
     * @author v8243060 & v8036651
     */
    protected boolean isNameAllowed(String name) {
        return (routingTable.get(name) == null && usernameValidation(name));
    }

    /**
     * Checks whether the message came from the correct branch/agent
     *
     * @param agent MetaAgent that send/propagated the message
     * @param msg message sent
     * @return true if MetaAgent in the routing table for the message sender is
     * the same as the MetaAgent who sent the message
     * @author v8243060
     */
    protected boolean isMessageOriginCorrect(MetaAgent agent, Message msg) {
        return (agent.equals(this.routingTable.get(msg.getSender())));
    }
}
