/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author v8077971
 * @author v8036651
 */
public final class ObserverInterface implements MouseListener, ActionListener, Serializable
{
    private final  JButton[] buttons = new JButton[5];
    
    private final String[] buttonNames = {"Add Agent", "Add Portal", "Add Socket", "Add Router"};
    private final Dimension buttonSize;
    private final Dimension size;
    
    final JPanel buttonsPanel;
    final JPanel mainPanel;
    
    
    
    public ObserverInterface(Dimension d)
    {
        size = d;
        
        
        
        BorderLayout bLayout = new BorderLayout(25,25);
        GridLayout options = new GridLayout(1,3,1,1);
        GridLayout agentsLayout = new GridLayout(5,5,1,1);
        
        mainPanel = new JPanel();
        mainPanel.setSize(size);
        
        buttonSize = new Dimension((int)(size.getWidth() / 4) - 1, (int)(size.getHeight() / 10) -1);
        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(options);
        
        
        
        loadPanels();
    }
    
    
    protected void loadPanels()
    {
        for(int i = 0; i < 4; i++)
        {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setVisible(true);
            buttons[i].setPreferredSize(buttonSize);
            buttonsPanel.add(buttons[i]);
            buttons[i].addActionListener(this);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
    
    public void leftButtonClicked(MouseEvent e, int index)
    {
        //Required to stop Netbeans from complaining.
    }
    
    public void rightButtonClicked(MouseEvent e, int index)
    {
        //See line 100
    }
    
    public void middleButtonClicked(MouseEvent e, int index)
    {
        //See line 100
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        //See line 100
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        //See Line 100
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        //See Line 100
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        //See Line 100
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        for(int i = 0; i < buttons.length; i++)
        {
            if(e.getSource().equals(buttons[i]))
            {
                switch(i)
                {
                    case 0:
                        System.out.println("Add a Agent");
                        break;
                    case 1:
                        System.out.println("Add a Portal");
                        break;
                    case 2:
                        System.out.println("Add a socket");
                        break;
                    case 3:
                        System.out.println("Add a router");
                        break;
                    default:
                        System.out.println("You fucked up");
                        break;
                }
            }
        }
    }
}


