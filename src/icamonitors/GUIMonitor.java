/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamonitors;

import icaGUI.ObserverGUI;
import icamessages.Message;
import java.util.Observable;

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
    public void ReceivedMessage(Message message) {
        gui.updateTable(message, agentName);
    }

    @Override
    public void SentMessage(Message message) {
        gui.updateTable(message, agentName);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
