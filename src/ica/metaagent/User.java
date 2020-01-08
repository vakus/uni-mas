/*
 * This is the package that holds all the meta agents that are used throught 
 * the program, all of the emta agents draw from the super class of MetaAgent 
 * which is an abstract class.
 *
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.GUI.UserGUI;
import ica.messages.Message;
import ica.messages.MessageType;

/**
 * This class is for the user to interact with, it loads the GUI and has all the
 * methods that are needed for the user.
 *
 * @author v8036651
 * @author v8073331
 * @author v8243060
 */
public class User extends MetaAgent {

    public Portal connection;
    protected UserGUI GUI;

    /**
     * Constructor for a user, Draws from the super class of MetaAgent.
     *
     * @param name the name of the user agent
     * @param portal the link to the portal which the agent should be connected
     * to
     * @author v8036651
     */
    public User(String name, Portal portal) {
        super(name);
        connection = portal;
        GUI = new UserGUI(this);
    }

    /**
     * This function is used to display incoming message. Since user agent
     * should not forward any messages, if the recipient is invalid, an error
     * message is sent back to the sender, and the message is discarded.
     *
     * @param agent the source of the message which is being received.
     * @param msg the message to be processed.
     * @author v8073331
     */
    @Override
    public void messageHandler(MetaAgent agent, Message msg) {
        if (msg.getRecipient().equals(this.name)) {
            System.out.println("Message (" + msg.getMessageType().toString() + "): " + msg.getMessageDetails());
            GUI.recivedMessage(msg.getSender(), msg.getMessageDetails());
        } else {
            connection.messageHandler(this, new Message(this.name, msg.getSender(), MessageType.ERROR, "Message recieved by wrong agent"));
        }

    }

    /**
     * Creates a message and sends it to the portal but checks if the desired
     * recipient is allowed first, therefore denying global messages to users.
     *
     * @param recipient recipient of the message
     * @param details message details
     * @throws IllegalArgumentException if recipient contains "/"
     * @author v8243060
     */
    public void sendMessage(String recipient, String details) {
        if (!recipientValidation(recipient) || recipient.equalsIgnoreCase("global")) {

            throw new IllegalArgumentException("Recipient name not correct");
        }
        Message msg = new Message(name, recipient, MessageType.USER_MSG, details);
        connection.messageHandler(this, msg);
    }

}
