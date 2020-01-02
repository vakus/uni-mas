/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import icamessages.Message;
import icametaagent.Portal;
import icametaagent.Router;
import icametaagent.SocketAgent;
import icametaagent.User;
import icamonitors.CMDMonitor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    
    final String[] columnNames = {"Sender", "Intended Recipient", "Actual Recipient", "Message Type", "Date"};
    JTable record;
    JScrollPane scrollPane;
    final JPanel mainPanel;
    public Object[][] data = new Object[][] {};
    
    public ObserverInterface(Dimension d)
    {
        addAgent = new JButton("Add Agent");
        addPortal = new JButton("Add Portal");
        addSocket = new JButton("Add Socket");
        addRouter = new JButton("Add Router");
        
        record = new JTable(new DefaultTableModel(data,columnNames));     
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
            String name =  JOptionPane.showInputDialog("Please input the name of the user: ");
            //String portalName =  JOptionPane.showInputDialog("Please input mark for test 1: ");
            //Requires check to see if it exists or not also none hardcoded portal
            Portal portal = new Portal("Portal-3");
            User agent = new User(name, portal);
            CMDMonitor m2 = new CMDMonitor(portal.getName());
            portal.addObserver(m2);
        }
        else if(e.getSource().equals(addPortal))
        {
            //Not sure if it links to the router yet?
            String portalName =  JOptionPane.showInputDialog("Please input the name of the portal: ");
            Portal p1 = new Portal(portalName);
            CMDMonitor m2 = new CMDMonitor(p1.getName());
            p1.addObserver(m2);
        }
        else if(e.getSource().equals(addSocket))
        {
            String address =  JOptionPane.showInputDialog("Please input the address for the socket: ");
            Socket s;
            try {
                s = new Socket(address, 42069);
            SocketAgent a = new SocketAgent(new Portal("Portal-3"), s);
                System.out.println("Created");
            } catch (IOException ex) {
                Logger.getLogger(ObserverInterface.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Failed");
            }
        }
        else if(e.getSource().equals(addRouter))
        {
            try {
                String routerName =  JOptionPane.showInputDialog("Please input the name of the router: ");
                Router r1 = new Router(routerName);
                CMDMonitor m1 = new CMDMonitor(r1.getName());
                r1.addObserver(m1);
                Thread t = new Thread(r1);
                t.start();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ObserverInterface.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Did not add router.");
            }
        }
        else
        {
            System.out.println("Nothing has been selected.");
            System.out.println("Error - Not yet implemented.");
        }
    }
    public void update (Message msg, String actual)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        DefaultTableModel model = (DefaultTableModel) record.getModel();
        model.addRow(new Object[]{msg.getSender(), msg.getRecipient(),actual, msg.getMessageType(),date});
        record.setModel(model);
    }
    
    
}