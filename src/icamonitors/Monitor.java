/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamonitors;

import icamessages.Message;

/**
 *
 * @author v8036651
 */
public abstract class Monitor 
{
    protected Message message;
    protected String agentName;
    /**
     * Constructor for the abstract class of monitor,
     * This is abstract because it will be extended by both GUIMonitor and CMDMonitor.
     * @param msg
     * @param name 
     */
    public Monitor(Message msg, String name) 
    {
        message = msg;
        agentName = name;
    }
    
    /**
     * Abstract method that will be overwritten that will do something based on 
     * which class it is called from when a message is received.
     */
    public abstract void ReceivedMessage ();
    
    /**
     * Abstract method that will be overwritten that will do something based on 
     * which class it is called from when a message is sent.
     */
    public abstract void SentMessage ();
    
    
    
}
