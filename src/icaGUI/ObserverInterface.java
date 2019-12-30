/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import icamessages.Message;
import java.awt.Dimension;
import java.awt.GridLayout;
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
    private final JButton addAgent;
    private final JButton addPortal;
    private final JButton addSocket;
    private final JButton addRouter;
    JPanel buttonsPanel; 
    
    final String[] columnNames = {"Sender", "Intended Recipient", "Actual Recipient", "Date"};
    
    public Object[][] data;
    JTable record;
    JScrollPane scrollPane;
    
    final JPanel mainPanel;
    
    public ObserverInterface(Dimension d)
    {
     
        addAgent = new JButton("Add Agent");
        addPortal = new JButton("Add Portal");
        addSocket = new JButton("Add Socket");
        addRouter = new JButton("Add Router");
        
        data = new Object[][] {};
        
        record = new JTable(data,columnNames);
        
        scrollPane = new JScrollPane(record);
        
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