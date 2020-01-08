/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.GUI;

import ica.messages.Message;
import ica.messages.MessageType;
import ica.metaagent.User;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author v8036651
 */
public class UserGUI 
{
    private User user;
    private UserInterface iFace;

    public UserGUI(User agent) 
    {
        user = agent;
        JFrame userFrame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int)(screenSize.getWidth() * 0.275), (int)(screenSize.getHeight() * 0.45));
        
        iFace = new UserInterface(user,frameSize);
        userFrame = new JFrame(user.getName());
        userFrame.getContentPane().add(iFace.mainPanel);
        userFrame.setSize(frameSize);
        userFrame.setVisible(true);
        userFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        userFrame.addWindowListener(new WindowListener(){
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                user.connection.messageHandler(user, new Message(user.getName(), "GLOBAL", MessageType.REMOVE_METAAGENT, ""));
                userFrame.setVisible(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        userFrame.setLocationRelativeTo(null);
        
        userFrame.setTitle(user.getName());
       
    }
    
    
    public void recievedMessage (String sender, String details)
    {
        iFace.displayMessage(sender, details);
    }
}
