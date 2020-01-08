/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.monitors;

import ica.messages.Message;

/**
 *
 * @author v8036651
 */
public abstract class Monitor
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
     * @param actualSender
     */
    public abstract void ReceivedMessage (Message message, String actualSender);
    
    /**
     * Abstract method that will be overwritten that will do something based on 
     * which class it is called from when a message is sent.
     * @param message
     * @param actualSender
     */
    public abstract void SentMessage (Message message,  String actualSender);
}