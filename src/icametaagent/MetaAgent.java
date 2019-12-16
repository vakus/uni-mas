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
     */
    MetaAgent(String name) {
        this.name = name;
    }

    /**
     * Used to return the name value of a MetaAgent.
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Abstract method that is overwritten my sub classes for handling a message.
     * @param agent
     * @param msg 
     */
    public abstract void messageHandler(MetaAgent agent, Message msg);
}
