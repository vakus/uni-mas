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
 * @author v8073331
 * @author v8036651
 * @author v8243060
 */
public class Portal extends MetaAgent {

    /**
     * The routing table for all known meta agents.
     */
    protected HashMap<String, MetaAgent> routingTable;
    /**
     * This list only stores list of socketAgents which are to be sent messages
     * only unique.
     */
    private ArrayList<SocketAgent> socketAgents;
    /**
     * List of all {@link Monitor} which are observing this node.
     */
    private Observer observers;

    /**
     * Creates new portal with specific node name.
     *
     * @param name the name of the portal to be created.
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
     * @param obs the observer which should be added.
     * @author v8036651
     */
    public void addObserver(Monitor obs) {
        observers.addObserver(obs);
    }

    /**
     * Returns the next node which would be used to forward message to meta
     * agent represented by name. The meta agent which is returned is not
     * necessarily the meta agent which is represented by name.
     *
     * @param name name of the meta agent which we want to find.
     * @return {@link MetaAgent} which represents the next node in link to meta
     * Agent represented by name. Returns null if no meta agent found.
     * @author v8036651
     */
    public MetaAgent getMetaAgent(String name) {
        return routingTable.get(name);

    }

    /**
     * Adds a new agent to the routing table of the portal.
     *
     * @param name the name of the meta agent to be added.
     * @param meta the {@link MetaAgent} which should be added to the table.
     * @author v8073331
     */
    public void addAgent(String name, MetaAgent meta) {
        if (meta instanceof SocketAgent) {
            if (socketAgents.contains((SocketAgent) meta)) {
                routingTable.put(name, meta);
            } else {
                if (this instanceof Router || (this instanceof Portal && socketAgents.isEmpty())) {
                    socketAgents.add((SocketAgent) meta);
                    routingTable.put(name, meta);
                } else {
                    System.out.println("[ERROR] Portal should not have more than one SocketAgent");
                }
            }
        } else {
            routingTable.put(name, meta);
        }
    }

    /**
     * Removes the agent in location n from the routing table of the portal.
     *
     * @param name the name of the {@link MetaAgent} to be removed.
     * @author v8036651
     */
    public void removeAgent(String name) {
        routingTable.remove(name);
    }

    /**
     * Recieves and processes the message.
     * <p>
     * First all monitors are being notified of the received message. If the
     * message recipient is either the name of this portal or is "GLOBAL" (case
     * in-sensitive) then the message is processed. If the message recipient
     * isn't this portal's name nor "GLOBAL", then the source is checked if the
     * source of the message is correct (see
     * {@link #isMessageOriginCorrect(icametaagent.MetaAgent, icamessages.Message) isMessageOriginCorrect}).
     * If the source is correct then message is sent forward to the correct
     * recipient. If the recipient can not be found in routing table, the
     * message is discarded, and message is sent from the portal that the
     * recipient could not be found and that message was not received. If the
     * sender origin is invalid then the message is discarded.
     * </p>
     * <p>
     * If the message recipient is this portal name or "GLOBAL", then the
     * message will be processed. Following message types are processed by
     * portal:
     * <ul>
     * <li>{@link MessageType#ADD_METAAGENT}</li>
     * <li>{@link MessageType#REMOVE_METAAGENT}*</li>
     * <li>{@link MessageType#USER_MSG}*</li>
     * <li>{@link MessageType#LOAD_TABLE}</li>
     * <li>{@link MessageType#ERROR}*</li>
     * </ul>
     * * those messages are checked for correct origin before processing.
     * </p>
     * <p>
     * Some messages are not processed and ignored by portal (e.g.
     * {@link MessageType#ADD_PORTAL}) as they are not intended to be processed
     * by portal, but by extensions of it such as Router.
     * </p>
     * <p>
     * When the message is being processed (not forwarded), the
     * {@link #routingTable routingTable} is thread locked, as changes may occur
     * while processing messages. The table is not locked when the function is
     * only forwarding messages in order to optimise performance when only
     * forwarding messages.
     * </p>
     *
     * @param agent the source from which the message is being sent
     * @param message the message which is being received.
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
                        /*
                        if (this instanceof Portal) {
                            break;
                        }*/

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
     * Checks whatever the agent name is allowed. This function checks if the
     * agent name is not already in use and if the username is valid.
     *
     * @param name MetaAgent name to be added
     * @return true if MetaAgent name is allowed and doesn't already exists
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
