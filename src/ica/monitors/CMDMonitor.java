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
public class CMDMonitor extends Monitor
{
    /**
     * A name of the agent must be passed as it says which agent the monitor is on,
     * this is to compare the intended recipient and the actual,
     * requires a message to be passed for the output.
     * @param name 
     * @author v8036651
     */
    public CMDMonitor(String name) 
    {
        super(name);
    }
    
    /**
     * Method to output to the command line showing the name of the sender, the
     * name of the recipient, the actual recipient, the message type and the
     * message details when a message is received.
     * @param actualSender
     * @parm message
     * @author v8036651
     */
    @Override
    public void ReceivedMessage (Message message, String actualSender)
    {
        System.out.println("+==========================================+");
        System.out.println("|This message was received from:           |");
        System.out.printf("|%42s|\n",message.getSender());
        System.out.println("|This message was received by:             |");
        System.out.printf("|%42s|\n",agentName);
        System.out.println("|This message was meant to be received by: |");
        System.out.printf("|%42s|\n",message.getRecipient());
        System.out.println("|This message type is:                     |");
        System.out.printf("|%42s|\n",message.getMessageType());
        System.out.println("|The contents of the message is:           |");
        System.out.printf("|%42s|\n",message.getMessageDetails());
        System.out.println("+==========================================+");
        System.out.println("");
    }
    
    /**
     * Method to output to the command line showing the name of the sender, the
     * name of the intended sender, the intended recipient, the message type and the
     * message details when a message is sent.
     * @param actualSender
     * @parm message
     * @author v8036651
     */
    @Override
    public void SentMessage (Message message, String actualSender)
    {
        System.out.println("+==========================================+");
        System.out.println("|This message was sent from:               |");
        System.out.printf("|%42s|\n",agentName);
        System.out.println("|This message is meant to be sent from:    |");
        System.out.printf("|%42s|\n",message.getSender());
        System.out.println("|This message is meant to be received by:  |");
        System.out.printf("|%42s|\n",message.getRecipient());
        System.out.println("|This message type is:                     |");
        System.out.printf("|%42s|\n",message.getMessageType());
        System.out.println("|The contents of the message is:           |");
        System.out.printf("|%42s|\n",message.getMessageDetails());
        System.out.println("+==========================================+");
        System.out.println("");
    }
}