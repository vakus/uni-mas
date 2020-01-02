/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import icametaagent.User;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

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
        userFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        userFrame.setLocationRelativeTo(null);
        
        userFrame.setTitle(user.getName());
       
    }
    
    
    public void recievedMessage (String sender, String details)
    {
        iFace.displayMessage(sender, details);
    }
}
