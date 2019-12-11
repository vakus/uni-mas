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
    
    public User(String name, Portal p) 
    {
        super(name);
        connection = p;
    }

    @Override
    public void messageHandler(MetaAgent agent, Message msg) 
    {
        if(msg.getRecipient().equals(this.name)){
            System.out.println("Message (" + msg.getMessageType().toString() + "): " + msg.getMessageDetails());
        }else{
            //connection.messageHandler(this, new Message(this.name, msg.getSender(), MessageType.ERROR, "Message recieved by wrong agent"));
        }
    }
}