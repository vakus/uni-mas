/**
 * This package holds all the different graphical user interface (GUI) classes.
 * It is called by the main run methods can has classes that are passed as parameters
 * to the constructors of other packages. These classes describe the layout for the
 * observer GUI and the layout for the user interface. The methods in these classes
 * are used to help update the GUI's and to display messages. These methods are called
 * within methods of different classes and packages.
 */
package ica.GUI;

import ica.metaagent.User;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class is used to layout what will be displayed and how it will be
 * displayed when using the user GUI.
 *
 * @author v8036651
 */
public class UserInterface implements ActionListener {

    private final JButton send = new JButton("Send");
    private final JTextArea messageText = new JTextArea();
    private final JTextArea recievedMessage = new JTextArea();
    private final JTextField recipient = new JTextField();
    private final Dimension buttonSize = new Dimension(200, 100);
    private final Dimension textFieldSize = new Dimension(200, 100);
    private final Dimension textAreaSize = new Dimension(200, 100);
    private String messageDetails;
    private String recipientName;
    private final User user;

    final JPanel mainPanel;

    /**
     * This is the constructor for the userInterface class, it outlines how the
     * interface for the GUI will be laid out and what will be on it.
     *
     * @param agent the user agent which is being attached to this GUI.
     */
    public UserInterface(User agent) {
        user = agent;

        mainPanel = new JPanel(new GridLayout(4, 2));

        send.addActionListener(this);
        send.setPreferredSize(buttonSize);
        recipient.setPreferredSize(textFieldSize);
        messageText.setPreferredSize(textAreaSize);
        mainPanel.add(new JLabel("Recipient:"));
        mainPanel.add(recipient);

        mainPanel.add(new JLabel("Message:"));
        mainPanel.add(messageText);

        mainPanel.add(new JLabel());
        mainPanel.add(send);

        mainPanel.add(new JLabel("Recieved Message:"));
        mainPanel.add(recievedMessage);

        mainPanel.setName(agent.getName());
    }

    /**
     * A method that is not called directly at any point in the program but
     * takes place when the send button is clicked.
     *
     * @param event the action event passed by event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(send)) {
            recipientName = recipient.getText();
            if (recipientName.equalsIgnoreCase("GLOBAL")) {
                System.out.println("Error - User cannot send a global message!");
            } else {
                messageDetails = messageText.getText();
                user.sendMessage(recipientName, messageDetails);
                System.out.println("Sent Message");
            }
        }
    }

    /**
     * This method is called when a message is received by a user agent and is
     * used to display what the contents of the message was.
     *
     * @param sender the sender of the message being received.
     * @param details the content of the message being received.
     */
    public void displayMessage(String sender, String details) {
        String prevMessages = recievedMessage.getText();
        recievedMessage.setText(prevMessages + "\n A message has been recieved by " + sender + ":\n " + details);
    }
}
