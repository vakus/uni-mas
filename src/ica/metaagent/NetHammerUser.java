package ica.metaagent;

import ica.messages.Message;
import ica.messages.MessageType;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author v8073331
 */
public class NetHammerUser extends MetaAgent implements Runnable{

    public Portal connection;

    private ArrayList<Long> times;
    
    private boolean running;
    
    private Thread thread;

    /**
     * Constructor for a user, Draws from the super class of MetaAgent.
     *
     * @param name the name of the user agent
     * @param portal the link to the portal which the agent should be connected
     * to
     * @author v8036651
     */
    public NetHammerUser(String name, Portal portal) {
        super(name);
        connection = portal;
        running = true;
        times = new ArrayList<>();
        thread = new Thread(this, name);
        thread.start();
    }

    /**
     * Returns the Average time that it took NetHammer to submit all messages in
     * milliseconds.
     *
     * @return average time for all messages in milliseconds
     */
    public long getAverageTime() {
        int all = 0;
        for (Long l : times) {
            all += l;
        }
        return (!times.isEmpty()) ? all / times.size() : 0;
    }

    /**
     * Returns the Minimum time that it took NetHammer to submit all messages in
     * milliseconds.
     *
     * @return minimum time for fastest message in milliseconds.
     */
    public long getMinTime() {
        long lowest = Long.MAX_VALUE;
        for (Long l : times) {
            if (l < lowest) {
                lowest = l;
            }
        }
        return lowest;
    }

    /**
     * Returns the Maximum time that it took NetHammer to submit the slowest
     * message in milliseconds.
     *
     * @return maximum time for slowest message in milliseconds.
     */
    public long getMaxTime() {
        long longest = Long.MIN_VALUE;
        for (Long l : times) {
            if (l > longest) {
                longest = l;
            }
        }
        return longest;
    }

    
    /**
     * This function is used to display incoming message. Since user agent
     * should not forward any messages, if the recipient is invalid, an error
     * message is sent back to the sender, and the message is discarded.
     * @author v8073331
     */
    @Override
    public void run() {
        while(running){
            try {
                Message msg = messageQueue.take().getMessage();
                if (msg.getRecipient().equals(this.name) && msg.getMessageType().equals(MessageType.USER_MSG)) {
                    times.add(System.currentTimeMillis() - Long.decode(msg.getMessageDetails()));
                } else {
                    System.out.println("Error: " + msg.getMessageDetails());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(NetHammerUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void shutdown(){
        running = false;
    }
}
