package ica.metaagent;

import ica.messages.Message;
import ica.messages.MessageType;
import ica.messages.ReceivedMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is for the user to interact with, it loads the GUI and has all the
 * methods that are needed for the user.
 *
 * @author v8036651
 * @author v8073331
 * @author v8243060
 */
public class User extends MetaAgent implements Runnable {

    protected Portal connection;
    protected boolean running;
    protected Thread userThread;
    
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
        running = true;
        userThread = new Thread(this, name);
        userThread.start();
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
        try {
            messageQueue.put(new ReceivedMessage(agent, msg));
        } catch (InterruptedException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
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
        if (!usernameValidation(recipient)) {
            throw new IllegalArgumentException("Recipient name not correct");
        }
        Message msg = new Message(name, recipient, MessageType.USER_MSG, details);
        connection.messageHandler(this, msg);
    }

    @Override
    public void run() {
        while (running) {
            try {
                ReceivedMessage receivedMessage = messageQueue.take();
                
                Message msg = receivedMessage.getMessage();

                if (msg.getRecipient().equals(this.name)) {
                    System.out.println("Message (" + msg.getMessageType().toString() + "): " + msg.getMessageDetails());
                } else {
                    connection.messageHandler(this, new Message(this.name, msg.getSender(), MessageType.ERROR, "Message recieved by wrong agent"));
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop(){
        running = false;
    }
}
