/*
 * This is the package that holds all the meta agents that are used throught 
 * the program, all of the emta agents draw from the super class of MetaAgent 
 * which is an abstract class.
 *
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.messages.Message;

/**
 *
 * @author v8036651
 */
public abstract class MetaAgent {

    protected final String name;

    /**
     * Constructor for a new MetaAgent.
     *
     * @param name the name of the node to be created
     * @author v8036651
     */
    MetaAgent(String name) {
        if (!usernameValidation(name)) {
            throw new IllegalArgumentException("Invalid user name");
        }
        this.name = name;
    }

    /**
     * Used to return the name value of a MetaAgent.
     *
     * @return String of the name of the MetaAgent
     * @author v8036651
     */
    public String getName() {
        return name;
    }

    /**
     * Abstract method that is overwritten by subclasses for handling a message.
     *
     * @param agent source of the message which is being sent
     * @param msg the message which is being sent
     * @author v8036651
     */
    public abstract void messageHandler(MetaAgent agent, Message msg);

    /**
     * Validates whether the user name is allowed
     *
     * @param name metaagent name to be used
     * @return true if metaagent name allowed
     * @author v8243060
     */
    protected boolean usernameValidation(String name) {
        return (name != null && !name.contains("/") && !name.equalsIgnoreCase("global"));
    }

    /**
     * Validates the recipient string
     *
     * @param recipient recipient of the message
     * @return true if recipient doesn't contain "/"
     * @author v8243060
     */
    public boolean recipientValidation(String recipient) {
        return (!recipient.contains("/"));
    }
}
