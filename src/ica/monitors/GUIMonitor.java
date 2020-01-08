/*
 * This package is used for monitoring what is being sent across the network in 
 * different ways, it adds observers to each MetaAgent when they are created which 
 * refer back to the observers that are defined.
 *
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.monitors;

import ica.GUI.ObserverGUI;
import ica.messages.Message;

/**
 *
 * @author v8036651
 * @author v8073331
 */
public class GUIMonitor extends Monitor {

    private ObserverGUI gui;
    
    public GUIMonitor(String name, ObserverGUI gui) {
        super(name);
        this.gui = gui;
    }

    @Override
    public void ReceivedMessage(Message message, String actualReciever) {
        gui.updateTable(message, "RECV", this.agentName, actualReciever);
    }

    @Override
    public void SentMessage(Message message, String actualReciever) {
        gui.updateTable(message, "SEND", this.agentName, actualReciever);
    }
}
