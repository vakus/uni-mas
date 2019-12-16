/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamessages;

/**
 *
 * @author v8243060
 */
public class Message {
    /**
     * String name of the sender of the message
     */
    private String sender;
    
    /**
     * String name of the recipient of the message
     * If "global" then the message is a broadcast
     */
    private String recipient;
    
    /**
     * Enum type of the message
     */
    private MessageType type;
    
    /**
     * Contains the message itself
     */
    private String messageDetails;
    
    /**
     *Constructor for message class when passed parameters.
     * @param sender
     * @param recipeint
     * @param type
     * @param details
     */
    public Message(String sender, String recipeint, MessageType type, String details){
        this.sender = sender;
        this.recipient = recipeint;
        this.type = type;
        this.messageDetails = details;
    }

    /**
     *Retrieve sender
     * @return
     */
    public String getSender(){
        return sender;
    }
    
    /**
     *Retrieve recipient
     * @return
     */
    public String getRecipient(){
        return recipient;
    }
    
    /**
     *Retrieve message type
     * @return
     */
    public MessageType getMessageType(){
        return type;
    }
    
    /**
     *Retrieve message details
     * @return
     */
    public String getMessageDetails(){
        return messageDetails;
    }
    
    /**
     * toString() method which allows the message to be passed between the portals.
     * @return 
     */
    @Override
    public String toString(){
        return sender + "/" + recipient + "/" + type.toString() + "/" + messageDetails;
    }
    
    /**
     * This is to convert back from the toString() method
     * @param msg The string that should be parsed to Message
     * @return Message object created from the string
     */
    public static Message parseMessage(String msg){
        String[] msgArr = msg.split("/",4);
        String sender = msgArr[0];
        String recipient = msgArr[1];
        MessageType type = MessageType.valueOf(msgArr[2].toUpperCase());
        String details = msgArr[3];
        
        return new Message(sender, recipient, type, details);
    }
}