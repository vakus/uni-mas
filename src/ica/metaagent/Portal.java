package ica.metaagent;

import ica.messages.Message;
import ica.messages.MessageType;
import ica.monitors.Monitor;
import ica.monitors.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is where all methods that are used by the portal are defined and
 * some methods that are also used in the router class, it also holds the
 * constructor and acts as a super class to router while extending the super
 * class of MetaAgent.
 *
 * @author v8073331
 * @author v8036651
 * @author v8243060
 */
public class Portal extends MetaAgent {

    /**
     * The routing table for all known meta agents.
     */
    protected final HashMap<String, MetaAgent> routingTable;
    /**
     * This list only stores list of socketAgents which are to be sent messages
     * only unique.
     */
    protected final ArrayList<SocketAgent> socketAgents;
    /**
     * List of all {@link Monitor} which are observing this node.
     */
    protected final Observer observers;

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
     * @param observer the observer which should be added.
     * @author v8036651
     */
    public void addObserver(Monitor observer) {
        observers.addObserver(observer);
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
     * Adds a new agent to the routing table of the portal as long as there is
     * no agent with the same name already in the routing table.
     *
     * @param name the name of the agent to be added
     * @param meta the {@link MetaAgent} which the messages should be forwarded
     * to.
     * @throws IllegalArgumentException if name is already in routingTable. Also
     * thrown if portal already has a socket connection.
     * @author v8073331
     */
    public void addAgent(String name, MetaAgent meta) {
        if (!(routingTable.containsKey(name))) {
            if (meta instanceof SocketAgent && !(socketAgents.contains((SocketAgent) meta))) {
                if (socketAgents.isEmpty()) {
                    socketAgents.add((SocketAgent) meta);
                    routingTable.put(name, meta);
                } else {
                    throw new IllegalArgumentException("Portal can not have more than one socket connection.");
                }
            } else {
                routingTable.put(name, meta);
            }
        } else {
            throw new IllegalArgumentException("Username is already in routing table.");
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
     * <p>
     * First all monitors are being notified of the received message. If the
     * message recipient is either the name of this portal or is "GLOBAL" (case
     * in-sensitive) then the message is processed. If the message recipient
     * isn't this portal's name nor "GLOBAL", then the source is checked if the
     * source of the message is correct (see
     * {@link #isMessageOriginCorrect(ica.metaagent.MetaAgent, ica.messages.Message)  isMessageOriginCorrect}).
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
     * </p>
     * <ul>
     * <li>{@link MessageType#ADD_METAAGENT}</li>
     * <li>{@link MessageType#REMOVE_METAAGENT}*</li>
     * <li>{@link MessageType#USER_MSG}*</li>
     * <li>{@link MessageType#LOAD_TABLE}</li>
     * <li>{@link MessageType#ERROR}*</li>
     * </ul>
     * <p>
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
        observers.updateReceiver(message, agent.getName());
        if (message.getRecipient().equals(this.name) || message.getRecipient().equalsIgnoreCase("GLOBAL")) {

            synchronized (routingTable) {
                switch (message.getMessageType()) {
                    case ADD_METAAGENT:
                        if (isNameAllowed(message.getSender())) {
                            addAgent(message.getSender(), agent);

                            forwardGlobal(agent, message);

                        } else {
                            System.out.println("Username not allowed: " + message.getSender());
                        }
                        break;
                    case REMOVE_METAAGENT:
                        if (isMessageOriginCorrect(agent, message)) {
                            removeAgent(message.getSender());

                            forwardGlobal(agent, message);

                        } else {
                            System.out.println("Invalid origin for message: " + message.toString());
                        }
                        break;
                    case USER_MSG:
                        if (isMessageOriginCorrect(agent, message)) {
                            System.out.println("UserMessage: " + message.getMessageDetails());
                        } else {
                            System.out.println("Invalid origin for message: " + message.toString());
                        }
                        break;

                    case LOAD_TABLE:
                        if (routingTable.isEmpty()) {
                            String[] values2 = message.getMessageDetails().split("\n");
                            for (String s : values2) {
                                addAgent(s, agent);
                            }
                        } else {
                            /**
                             * We should only load the routing table if it is
                             * empty to prevent case where one portal may have
                             * different data than the other We also can just
                             * disconnect since the connection was unsuccessful
                             */
                            System.out.println("Error: Can not load routing table, as it is not empty.");
                            ((SocketAgent) agent).close();
                        }
                        break;
                    case ERROR:
                        if (isMessageOriginCorrect(agent, message)) {
                            System.out.println("Error: " + message.getMessageDetails());
                        } else {
                            System.out.println("Invalid origin for message: " + message.toString());
                        }
                        break;
                    default:
                        Message msg = new Message(this.name, message.getSender(), MessageType.ERROR, "Could not process the message: Invalid message type");
                        observers.updateSender(msg, agent.getName());
                        agent.messageHandler(this, msg);
                }
            }
        } else {
            if (isMessageOriginCorrect(agent, message)) {
                if (routingTable.containsKey(message.getRecipient())) {
                    observers.updateSender(message, agent.getName());
                    routingTable.get(message.getRecipient()).messageHandler(this, message);
                } else {
                    Message msg = new Message(this.getName(), message.getSender(), MessageType.ERROR, "Could not route message to " + message.getRecipient() + ": The recipient was not found");
                    observers.updateSender(msg, agent.getName());
                    agent.messageHandler(this, msg);
                }
            } else {
                System.out.println("Invalid origin for message: " + message.toString());
            }
        }
    }

    /**
     * Checks whatever the agent name is allowed. This function checks if the
     * agent name is not already in use and if the username is valid.
     *
     * @param name MetaAgent name to be added
     * @return true if MetaAgent name is allowed and doesn't already exists
     * @author v8243060
     * @author v8036651
     */
    public boolean isNameAllowed(String name) {
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

    /**
     * This function sends message to all other nodes This function may be
     * overridden to modify the behaviour in which global messages are sent. For
     * example portal would only send global message forward to router while
     * router would send it to other places as well.
     *
     * @param msg the message to be sent
     * @param source the location from which the message came. This node will
     * not be sent the global message as it already seen it before
     * @author v8073331
     */
    protected void forwardGlobal(MetaAgent source, Message msg) {
        socketAgents.stream().filter((socketAgent) -> (!socketAgent.equals(source))).forEachOrdered((sa) -> {
            observers.updateSender(msg, source.getName());
            sa.messageHandler(this, msg);
        });
    }

    /**
     * Method that turns off a portal and disconnects it from the router and the
     * agents that were connected to that portal, removing all from the network.
     */
    public void shutdown() {
        forwardGlobal(this, new Message(this.getName(), "GLOBAL", MessageType.REMOVE_PORTAL, ""));
        socketAgents.forEach((socketAgent) -> {
            socketAgent.close();
        });
    }

    /**
     * Returns copy of routing Table which is unmodifiable
     *
     * @return unmodifiable copy of routing table
     */
    public Map<String, MetaAgent> getRoutingTable() {
        return Collections.unmodifiableMap(routingTable);
    }
}
