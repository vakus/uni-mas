/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import icamessages.MessageType;

/**
 *
 * @author v8036651
 */
public class User extends MetaAgent
{
    protected Portal connection;
    
    /**
     * Constructor for a user,
     * Draws from the super class of MetaAgent.
     * @param name
     * @param p 
     */
    public User(String name, Portal p) 
    {
        super(name);
        connection = p;
    }

    /**
     * Overwrites the messageHandler method,
     * This is used to display the message or pass the message on depending on
     * the recipient of the message.
     * @param agent
     * @param msg 
     */
    @Override
    public void messageHandler(MetaAgent agent, Message msg) 
    {
        if(msg.getRecipient().equals(this.name)){
            System.out.println("Message (" + msg.getMessageType().toString() + "): " + msg.getMessageDetails());
        }else{
            connection.messageHandler(this, new Message(this.name, msg.getSender(), MessageType.ERROR, "Message recieved by wrong agent"));
        }
    }
    
    /**
     * Creates a message and sends it to the portal
     * @param recipient recipient of the message
     * @param details message details
     * @throws IllegalArgumentException if recipient contains "/"
     * @author v8243060
     */
    public void sendMessage (String recipient, String details){
        if (!recipientValidation(recipient)){
            throw new IllegalArgumentException("Recipient name not correct");
        }
        Message msg = new Message(name, recipient, MessageType.USER_MSG, details);
        connection.messageHandler(this, msg);
    }
    
}