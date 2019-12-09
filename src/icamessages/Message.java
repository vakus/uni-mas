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

    public Message(){
        sender = "error";
        recipient = "error";
        type = MessageType.ERROR;
        messageDetails = "error";
    }
    
    public Message(String sender, String recipeint, MessageType type, String details){
        this.sender = sender;
        this.recipient = recipeint;
        this.type = type;
        this.messageDetails = details;
    }

    public String getSender(){
        return sender;
    }
    
    public String getRecipient(){
        return recipient;
    }
    
    public MessageType getMessageType(){
        return type;
    }
    
    public String getMessageDetails(){
        return messageDetails;
    }
    
    @Override
    public String toString(){
        return sender + "/" + recipient + "/" + type.toString() + "/" + messageDetails;
    }
    
    //TO DO
    public static Message parseMessage(String msg){
        String[] msgArr = msg.split("/",4);
        
        String sender = msgArr[0];
        String recipient = msgArr[1];
        MessageType type = MessageType.valueOf(msgArr[2].toUpperCase());
        String details = msgArr[3];
        
        return new Message(sender, recipient, type, details);
    }
}