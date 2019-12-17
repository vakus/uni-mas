/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamonitors;

import icamessages.Message;
import java.util.Observer;

/**
 *
 * @author v8036651
 */
public abstract class Monitor implements Observer
{
    protected String agentName;
    /**
     * Constructor for the abstract class of monitor,
     * This is abstract because it will be extended by both GUIMonitor and CMDMonitor.
     * @param name 
     */
    public Monitor(String name) 
    {
        agentName = name;
    }
    
    /**
     * Abstract method that will be overwritten that will do something based on 
     * which class it is called from when a message is received.
     * @param message
     */
    public abstract void ReceivedMessage (Message message);
    
    /**
     * Abstract method that will be overwritten that will do something based on 
     * which class it is called from when a message is sent.
     * @param message
     */
    public abstract void SentMessage (Message message);
    

    
    
    
}
