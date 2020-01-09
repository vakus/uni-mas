package ica.monitors;

import ica.messages.Message;

/**
 *
 * @author v8036651
 */
public abstract class Monitor {

    protected String agentName;

    /**
     * Constructor for the abstract class of monitor, This is abstract because
     * it will be extended by both GUIMonitor and CMDMonitor.
     *
     * @param name the name of the node being monitored
     */
    public Monitor(String name) {
        agentName = name;
    }

    /**
     * Abstract method that will be overwritten that will do something based on
     * which class it is called from when a message is received.
     *
     * @param message the message which was sent.
     * @param actualSender the name of the node which forwarded the message.
     */
    public abstract void ReceivedMessage(Message message, String actualSender);

    /**
     * Abstract method that will be overwritten that will do something based on
     * which class it is called from when a message is sent.
     *
     * @param message the message which was sent.
     * @param actualReciever the name of the node which is sending the message.
     */
    public abstract void SentMessage(Message message, String actualReciever);
}
