/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import icamessages.MessageType;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8036651
 */
public class Router extends Portal implements Runnable {

    protected ServerSocket server;

    /**
     * Constructor for a router object with a super class of portal.
     *
     * @param name the name of the Router Agent
     * @throws IOException if the serverSocket instance can not be created
     * @author v8036651
     */
    public Router(String name) throws IOException {
        super(name);
        server = new ServerSocket(42069);
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
                Socket s = server.accept();
                System.out.println("Accepted connection from: " + s.getInetAddress().toString());
                SocketAgent newAgent = new SocketAgent(this, s);
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
            if (agent instanceof SocketAgent) {
                socketAgents.add((SocketAgent) agent);
            }
            routingTable.put(name, agent);
        } else {
            throw new IllegalArgumentException("Username is already in routing table.");
        }
    }

    /**
     *
     *
     * @param agent
     * @param message
     * @author v8073331
     */
    @Override
    public void messageHandler(MetaAgent agent, Message message) {
        super.messageHandler(agent, message);
        if (message.getRecipient().equals(this.name) || message.getRecipient().equalsIgnoreCase("GLOBAL")) {
            synchronized (routingTable) {
                switch (message.getMessageType()) {
                    case ADD_PORTAL:

                        /**
                         * Create list of all agents we have
                         */
                        String values = "";
                        for (String key : routingTable.keySet()) {
                            values += key + "\n";
                        }
                        values += this.name + "\n";

                        /**
                         * Respond with list of the agents we have to sync
                         * routingTable of newly joining portal
                         */
                        Message msg = new Message(this.name, message.getSender(), MessageType.LOAD_TABLE, values);
                        agent.messageHandler(this, msg);
                        observers.updateSender(msg);

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
                        ArrayList<String> usernames = new ArrayList(routingTable.keySet());

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
                }
            }
        }
    }
}
