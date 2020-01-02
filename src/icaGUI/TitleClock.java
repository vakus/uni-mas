/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author v8077971
 * @author v8036651
 */

public class TitleClock implements Runnable 
{
    private final JFrame myFrame;
    private final Thread timeThread;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    private final Date date = new Date();
    
    public TitleClock(JFrame f)
    {
        myFrame = f;
        timeThread = new Thread(this);
        timeThread.start();
    }
    
    public void run()
    {
        while(myFrame.isVisible())
        {
            myFrame.setTitle(dateFormat.format(new Date()));
            
            try
            {
                Thread.currentThread().sleep(1000);
            }
            catch (InterruptedException ex)
            {
                
            }
        }
    }
}
