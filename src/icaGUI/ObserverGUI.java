/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author v8036651
 * @author v8036651
 */

/**
 * 
 * @author v8077971
 */
public class ObserverGUI
{
    public ObserverGUI()
    {
        JFrame mainFrame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int)(screenSize.getWidth() * 0.375), (int)(screenSize.getHeight() * 0.75));
        
        ObserverInterface iFace = new ObserverInterface(frameSize);
        mainFrame = new JFrame("Observer");
        mainFrame.getContentPane().add(iFace.mainPanel);
        mainFrame.setSize(frameSize);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        
        TitleClock clock = new TitleClock(mainFrame);
        clock.run();
        
    }
}
