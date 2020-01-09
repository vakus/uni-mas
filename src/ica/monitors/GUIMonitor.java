package ica.monitors;

import ica.GUI.ObserverGUI;
import ica.messages.Message;

/**
 * This class is designed to to display the data about a message, including
 * where it was sent, who it was sent from and what the type and data of the
 * message is in a GUI..
 *
 * @author v8036651
 * @author v8073331
 */
public class GUIMonitor extends Monitor {

    private ObserverGUI gui;

    /**
     * This is the constructor for the GUI monitor and must receive a name so it
     * can be referenced later on and take s observer GUI as a parameter so it
     * can link the changes it is passed to the JTable that is logging all the
     * network traffic.
     *
     * @param name the name of the agent which is being monitored
     * @param gui the link to the GUI element
     */
    public GUIMonitor(String name, ObserverGUI gui) {
        super(name);
        this.gui = gui;
    }

    /**
     * This method is overridden due to it having to update table of the GUI
     * that has been passed to it meaning that it must call another method that
     * is not called in its super class.
     *
     * @param msg the message which was sent.
     * @param actualSender the name of the node which is sending the message.
     */
    @Override
    public void ReceivedMessage(Message msg, String actualSender) {
        gui.updateTable(msg, "RECV", this.agentName, actualSender);
    }

    /**
     * Send message is overridden because it must update the JTable in the
     * observer GUI as well unlike in its super class.
     *
     * @param msg the message which was sent.
     * @param actualReciever the name of the node which is sending the message.
     */
    @Override
    public void SentMessage(Message msg, String actualReciever) {
        gui.updateTable(msg, "SEND", this.agentName, actualReciever);
    }
}
