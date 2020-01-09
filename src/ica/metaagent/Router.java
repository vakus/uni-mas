/*
 * This is the package that holds all the meta agents that are used throught 
 * the program, all of the emta agents draw from the super class of MetaAgent 
 * which is an abstract class.
 */
package ica.metaagent;

import ica.messages.Message;
import ica.messages.MessageType;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 * @author v8073331
 * @author V8243060
 */
public class Router extends Portal implements Runnable {

    /**
     * Used to receive connections from other MetaAgents.
     */
    protected ServerSocket server;

    /**
     * Stores the addresses of other routers on the network.
     *
     * @author V8243060
     */
    private ArrayList<SocketAgent> routerAddresses;

    /**
     * Constructor for a router object with a super class of portal.
     *
     * @param name the name of the Router Agent
     * @throws IOException if unable to instantiate {@link ServerSocket} on port
     * 42069
     * @author v8036651
     */
    public Router(String name) throws IOException {
        super(name);
        server = new ServerSocket(42069);
        routerAddresses = new ArrayList<>();
    }

    /**
     * Overwrites the run method from the implements runnable, creates a new
     * server socket to listen for new connections.
     *
     * @author v8073331
     */
    @Override
    public void run() {
        while (!server.isClosed()) {
            try {
                Socket socket = server.accept();
                System.out.println("Accepted connection from: " + socket.getInetAddress().toString());
                SocketAgent newAgent = new SocketAgent(this, socket);
                newAgent.start();
            } catch (IOException ex) {
                Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * method to addAgent to the routingTable. Additionally if agent is of
     * instance SocketAgent, it is added to the socketAgents list, to ensure
     * correct message propagation
     *
     * @param name the username which should be added to routingTable
     * @param agent the agent to which forward messages for this username
     * @throws IllegalArgumentException if name is already used in routingTable
     * @author v8073331
     */
    @Override
    public void addAgent(String name, MetaAgent agent) {
        if (!routingTable.containsKey(name)) {
            if (agent instanceof SocketAgent && !socketAgents.contains((SocketAgent) agent)) {
                socketAgents.add((SocketAgent) agent);
            }
            routingTable.put(name, agent);
        } else {
            throw new IllegalArgumentException("Username is already in routing table.");
        }
    }

    /**
     * A method that is called when a message is received by this object, it
     * decides what to do with the message next by finding if the target
     * recipient is on the network or not.
     *
     * @param agent the agent which is forwarding the message
     * @param message message which is being recieved
     * @author v8073331
     * @author V8243060
     */
    @Override
    public void messageHandler(MetaAgent agent, Message message) {
        if (message.getRecipient().equals(this.name) || message.getRecipient().equalsIgnoreCase("GLOBAL")) {
            synchronized (routingTable) {
                switch (message.getMessageType()) {
                    case ADD_PORTAL:
                        /**
                         * Create list of all agents we have
                         */
                        String routingValues = "";
                        for (String key : routingTable.keySet()) {
                            routingValues += key + "\n";
                        }
                        routingValues += this.name + "\n";

                        /**
                         * Respond with list of the agents we have to sync
                         * routingTable of newly joining portal
                         */
                        Message msgRoutingTable = new Message(this.name, message.getSender(), MessageType.LOAD_TABLE, routingValues);
                        agent.messageHandler(this, msgRoutingTable);
                        observers.updateSender(msgRoutingTable, agent.getName());

                        /**
                         * Add the portal to our routing table
                         */
                        addAgent(message.getSender(), agent);

                        /**
                         * Notify all other portals and routers of newly joining
                         * agent (portal)
                         */
                        forwardGlobal(this, new Message(message.getSender(), "GLOBAL", MessageType.ADD_METAAGENT, ""));

                        break;

                    case REMOVE_PORTAL:
                        /**
                         * REMOVE_PORTAL message should only be processed if
                         * sent from the correct location
                         */
                        if (!isMessageOriginCorrect(agent, message)) {
                            break;
                        }
                        /**
                         * This message should always come from SocketAgent, via
                         * which the portal to be removed is connected. This
                         * message should not be propagated further.
                         */
                        if (!(agent instanceof SocketAgent)) {
                            break;
                        }

                        socketAgents.remove((SocketAgent) agent);

                        /**
                         * We must make a copy of the keySet, which will not be
                         * modified when we remove entries
                         */
                        ArrayList<String> usernames = new ArrayList<>(routingTable.keySet());

                        /**
                         * find all clients that will be removed by this action
                         */
                        for (String username : usernames) {
                            if (routingTable.get(username).equals(agent)) {
                                /**
                                 * This agent will be removed if the portal will
                                 * be removed
                                 */
                                removeAgent(username);

                                forwardGlobal(this, new Message(username, "GLOBAL", MessageType.REMOVE_METAAGENT, ""));
                            }
                        }

                        ((SocketAgent) agent).close();

                        break;

                    case ADD_ROUTER:
                        /**
                         * Create list of all agents assigned to this router
                         */
                        String partialRoutingValues = "";
                        for (String key : routingTable.keySet()) {
                            if (routingTable.get(key) instanceof SocketAgent && !routerAddresses.contains((SocketAgent) routingTable.get(key))) {
                                partialRoutingValues += key + "\n";
                            }
                        }
                        partialRoutingValues += this.name + "\n";

                        /**
                         * Respond with list of the agents we have to sync to
                         * the routingTable of newly joining router
                         */
                        Message msgPartialRotingTable = new Message(this.name, message.getSender(), MessageType.LOAD_TABLE, partialRoutingValues);
                        agent.messageHandler(this, msgPartialRotingTable);
                        observers.updateSender(msgPartialRotingTable, agent.getName());

                        /**
                         * Notify portals of this router agent (router)
                         */
                        for (String key : routingTable.keySet()) {
                            if (routingTable.get(key) instanceof SocketAgent && !routerAddresses.contains((SocketAgent) routingTable.get(key))) {
                                ((MetaAgent) routingTable.get(key)).messageHandler(agent, new Message(message.getSender(), "GLOBAL", MessageType.ADD_METAAGENT, ""));
                            }
                        }

                        /**
                         * Add the router to our routing table and to the
                         * addresses table
                         */
                        addAgent(message.getSender(), agent);
                        routerAddresses.add((SocketAgent) agent);

                        break;

                    case REQUEST_ROUTER_ADDRESSES:
                        /**
                         * Create a list of all router addresses
                         */
                        String addresses = "";
                        for (SocketAgent address : routerAddresses) {
                            addresses += address.socket.getInetAddress().getHostAddress() + "\n";
                        }

                        /**
                         * Respond with list of the router addresses
                         */
                        Message msgRoutingAddresses = new Message(this.name, message.getSender(), MessageType.LOAD_ADDRESSES, addresses);
                        agent.messageHandler(this, msgRoutingAddresses);
                        observers.updateSender(msgRoutingAddresses, agent.getName());

                        break;

                    case LOAD_ADDRESSES:
                        /**
                         * Checks whether router address table is empty to
                         * prevent merging two networks
                         */
                        if (routerAddresses.isEmpty()) {
                            String[] values = message.getMessageDetails().split("\n");
                            /**
                             * checks whether the message details are empty to
                             * prevent creation of empty sockets
                             */
                            if (!message.getMessageDetails().isEmpty()) {
                                for (String address : values) {
                                    try {
                                        Socket s = new Socket(address, 42069);
                                        SocketAgent socketAgent = new SocketAgent(this, s);
                                        socketAgent.start();
                                        routerAddresses.add(socketAgent);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }

                            routerAddresses.add((SocketAgent) agent);

                            /**
                             * Sends the add router message to every other
                             * router in the network
                             */
                            for (SocketAgent socketAgent : routerAddresses) {
                                socketAgent.messageHandler(this, new Message(this.name, "GLOBAL", MessageType.ADD_ROUTER, ""));
                            }
                        } else {
                            System.out.println("Error: Router is already connected to a different network");
                        }
                        break;

                    case LOAD_TABLE:
                        /**
                         * Merges two routing tables this one and one carried by
                         * the message
                         */
                        String[] namesArray = message.getMessageDetails().split("\n");
                        for (String stringName : namesArray) {
                            addAgent(stringName, agent);
                        }
                        break;
                    default:
                        super.messageHandler(agent, message);
                }
            }
        } else {
            super.messageHandler(agent, message);
        }
    }
    
    @Override
    public void shutdown(){
        super.shutdown();
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
