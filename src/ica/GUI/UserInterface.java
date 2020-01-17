package ica.GUI;

import ica.metaagent.User;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class is used to layout what will be displayed and how it will be
 * displayed when using the user GUI.
 *
 * @author v8036651
 */
public class UserInterface implements ActionListener {

    private final JButton send;
    private final JTextArea messageText;
    private final JTextArea recievedMessage;
    private final JTextField recipient;
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

        mainPanel = new JPanel(new GridBagLayout());

        send = new JButton("Send");
        send.addActionListener(this);
        
        messageText = new JTextArea();
        recievedMessage = new JTextArea();
        recievedMessage.setEditable(false);
        recipient = new JTextField();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weightx = 0.5;
        constraints.weighty = 0.8;

        constraints.gridwidth = 3;
        mainPanel.add(new JScrollPane(recievedMessage), constraints);

        constraints.gridwidth = 1;
        constraints.gridy++;
        constraints.weighty = 0.1;
        constraints.weightx = 0.2;
        mainPanel.add(new JLabel("Recipient:"), constraints);
        constraints.gridx++;
        constraints.weightx = 0.6;
        mainPanel.add(recipient, constraints);

        constraints.gridy++;
        constraints.gridx = 1;
        constraints.weightx = 0.2;
        mainPanel.add(new JLabel("Message:"), constraints);
        constraints.gridx++;
        constraints.weightx = 0.6;
        mainPanel.add(new JScrollPane(messageText), constraints);
        constraints.gridx++;
        constraints.weightx = 0.2;
        mainPanel.add(send, constraints);
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
                JOptionPane.showMessageDialog(null, "User can not send global message!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                messageDetails = messageText.getText();
                user.sendMessage(recipientName, messageDetails);
                messageText.setText("");
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
        recievedMessage.setEditable(true);
        String prevMessages = recievedMessage.getText();
        recievedMessage.setText(prevMessages + sender + ": " + details + "\n");
        recievedMessage.setEditable(false);
    }
}