/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import icametaagent.User;
import java.awt.BorderLayout;
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
 *
 * @author v8036651
 */
public class UserInterface implements ActionListener
{
    private final JButton send = new JButton("Send");
    private final JTextArea messageText = new JTextArea();
    private final JTextArea recievedMessage = new JTextArea();
    private final JTextField recipient = new JTextField();
    private final Dimension buttonSize = new Dimension(200,100);
    private final Dimension textFieldSize = new Dimension(200,100);
    private final Dimension textAreaSize = new Dimension(200,100);
    private final Dimension size;
    private String messageDetails;
    private String recipientName;
    private final User user;
    
    final JPanel mainPanel;
    
    /**
     *
     * @param agent
     * @param d
     */
    public UserInterface(User agent, Dimension d) 
    {
        user = agent;
        size = d;
        
        mainPanel = new JPanel(new GridLayout(4,2));
        mainPanel.setSize(size);
        
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
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(send))
        {
            recipientName = recipient.getText();
            if (recipientName.equals("GLOBAL")||recipientName.equals("Global")||recipientName.equals("global"))
            {
                System.out.println("Error - User cannot send a global message!");
            }
            else
            {
                messageDetails = messageText.getText();
                user.sendMessage(recipientName, messageDetails);
                System.out.println("Sent Message");
            }
        }
    }
    
    public void displayMessage (String sender, String details)
    {
        recievedMessage.setText("A message has been recieved by "
                + sender + " - "
                         + details);
    }
}

