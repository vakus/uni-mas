/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import icamessages.Message;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author v8077971
 * @author v8036651
 */
//JScrollPane scrollPane = new JScrollPane(record);
//        record.setFillsViewportHeight(true);
//        record.setVisible(true);
//        record.setPreferredSize(new Dimension((int)(size.getWidth() / 4) - 1, (int)(size.getHeight() / 10) -1));
//        recordPanel.add(record);
public class ObserverInterface implements ActionListener
{
    private final Dimension buttonSize;
    private final Dimension size;
    private final JButton addAgent = new JButton("Add Agent");
    private final JButton addPortal = new JButton("Add Portal");
    private final JButton addSocket = new JButton("Add Socket");
    private final JButton addRouter = new JButton("Add Router");
    JPanel buttonsPanel; 
    
    final String[] columnNames = {"Sender", " Intended Recipient", "Actual Recipient","Date"};
    public Object[][] data;
    JTable record = new JTable(data,columnNames);
    JTable apples = new JTable();
    JScrollPane scrollPane = new JScrollPane(record);
    
    final JPanel mainPanel;
    
    public ObserverInterface(Dimension d)
    {
        
        data = new Object[][] {{"a1", "a2", "portal-1", "10:34"},{"a2", "a4", "portal-2", "10:35"}};
        
        buttonsPanel = new JPanel(new GridLayout(2,2));
        size = d;
        
        mainPanel = new JPanel(new GridLayout(2,1));
        mainPanel.setSize(size);
        
        buttonSize = new Dimension((int)(size.getWidth() / 4) - 1, (int)(size.getHeight() / 10) -1);
        addAgent.setPreferredSize(buttonSize);
        addAgent.addActionListener(this);
        addPortal.setPreferredSize(buttonSize);
        addPortal.addActionListener(this);
        addSocket.setPreferredSize(buttonSize);
        addSocket.addActionListener(this);
        addRouter.setPreferredSize(buttonSize);
        addRouter.addActionListener(this);
        
        mainPanel.add(scrollPane);
        buttonsPanel.add(addAgent);
        buttonsPanel.add(addPortal);
        buttonsPanel.add(addSocket);
        buttonsPanel.add(addRouter);
        mainPanel.add(buttonsPanel);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(addAgent))
        {
            System.out.println("Add Agent");
            System.out.println("Error - Not yet implemented.");
        }
        else if(e.getSource().equals(addPortal))
        {
            System.out.println("Add Portal");
            System.out.println("Error - Not yet implemented.");
        }
        else if(e.getSource().equals(addSocket))
        {
            System.out.println("Add Socket");
            System.out.println("Error - Not yet implemented.");
        }
        else if(e.getSource().equals(addRouter))
        {
            System.out.println("Add Router");
            System.out.println("Error - Not yet implemented.");
        }
        else
        {
            System.out.println("Nothing has been selected.");
            System.out.println("Error - Not yet implemented.");
        }
    }
    
    public void updateObserver(Message msg)
    {
        
    }
}


