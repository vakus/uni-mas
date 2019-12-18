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
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author v8036651
 */
public class UserInterface 
{
    private final JButton send = new JButton();
    private final JTextArea messageText = new JTextArea();
    private final JTextField recipient = new JTextField();
    private final Dimension buttonSize;
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
        
        buttonSize = new Dimension((int)(size.getWidth() / 4) - 1, (int)(size.getHeight() / 10) -1);
        send.setPreferredSize(buttonSize);
        mainPanel.add(recipient);
        mainPanel.add(send);
        mainPanel.add(messageText);
    }
    
    public void mouseClicked(MouseEvent e)
    {
        if(e.getSource().equals(send))
        {
            recipientName = recipient.getText();
            messageDetails = messageText.getText();
            user.sendMessage(recipientName, messageDetails);
        }
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
        
    }
    
}
