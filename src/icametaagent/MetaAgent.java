/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;

/**
 *
 * @author v8036651
 */
public abstract class MetaAgent{

    protected final String name;

    /**
     * Constructor for a new MetaAgent.
     * @param name 
     * @author v8036651
     */
    MetaAgent(String name) 
    {
        this.name = name;
    }

    /**
     * Used to return the name value of a MetaAgent.
     * @return String of the name of the MetaAgent
     * @author v8036651
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * Abstract method that is overwritten my sub classes for handling a message.
     * @param agent
     * @param msg 
     * @author v8036651
     */
    public abstract void messageHandler(MetaAgent agent, Message msg);
}
