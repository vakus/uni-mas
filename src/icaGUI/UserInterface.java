/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import icametaagent.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
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
    private final JTextField recipient = new JTextField();
    private final Dimension buttonSize = new Dimension(200,100);;
    private final Dimension textFieldSize = new Dimension(200,100);;
    private final Dimension textAreaSize = new Dimension(200,100);;
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
        BorderLayout bLayout = new BorderLayout(25,25);
        
        mainPanel = new JPanel();
        mainPanel.setSize(size);
        
        send.addActionListener(this);
        send.setPreferredSize(buttonSize);
        recipient.setPreferredSize(textFieldSize);
        messageText.setPreferredSize(textAreaSize);
        mainPanel.add(recipient);
        mainPanel.add(send);
        mainPanel.add(messageText);
    }
    
    public void mouseClicked(MouseEvent e)
    {
        
    }
    
    
    public void leftButtonClicked(MouseEvent e, int index)
    {
        //Required to stop Netbeans from complaining.
    }
    
    public void rightButtonClicked(MouseEvent e, int index)
    {
        
    }
    
    public void middleButtonClicked(MouseEvent e, int index)
    {
        
    }
    
    public void mousePressed(MouseEvent e)
    {
        
    }
    
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(send))
        {
            recipientName = recipient.getText();
            messageDetails = messageText.getText();
            user.sendMessage(recipientName, messageDetails);
            System.out.println("Sent Message");
        }
    }
    
}
