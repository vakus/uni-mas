/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;


import ica.main.GuiMain;
import icamessages.Message;
import icamessages.MessageType;
import icametaagent.Portal;
import icametaagent.Router;
import icametaagent.SocketAgent;
import icametaagent.User;
import icamonitors.CMDMonitor;
import icamonitors.GUIMonitor;
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
 * @author v8073331
 */
//JScrollPane scrollPane = new JScrollPane(record);
//        record.setFillsViewportHeight(true);
//        record.setVisible(true);
//        record.setPreferredSize(new Dimension((int)(size.getWidth() / 4) - 1, (int)(size.getHeight() / 10) -1));
//        recordPanel.add(record);
public class ObserverInterface implements ActionListener {

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
    public Object[][] data = new Object[][]{};

    private Portal portal;
    private Router router;

    public ObserverInterface(Dimension d) {
        addAgent = new JButton("Add Agent");
        addAgent.setEnabled(false);
        addPortal = new JButton("Add Portal");
        addSocket = new JButton("Add Socket");
        addSocket.setEnabled(false);
        addRouter = new JButton("Add Router");

        record = new JTable(new DefaultTableModel(data, columnNames));
        scrollPane = new JScrollPane(record);
        buttonsPanel = new JPanel(new GridLayout(2, 2));
        size = d;

        mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.setSize(size);

        buttonSize = new Dimension((int) (size.getWidth() / 4) - 1, (int) (size.getHeight() / 10) - 1);
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addAgent)) {
            String name = JOptionPane.showInputDialog("Please input the name of the user: ");
            //String portalName =  JOptionPane.showInputDialog("Please input mark for test 1: ");
            //Requires check to see if it exists or not also none hardcoded portal
            User agent = new User(name, portal);
            
            portal.messageHandler(agent, new Message(agent.getName(), "GLOBAL", MessageType.ADD_METAAGENT, ""));
            
        } else if (e.getSource().equals(addPortal)) {
            //Not sure if it links to the router yet?
            String portalName = JOptionPane.showInputDialog("Please input the name of the portal: ");
            portal = new Portal(portalName);
            CMDMonitor m2 = new CMDMonitor(portal.getName());
            GUIMonitor mg2 = new GUIMonitor(portal.getName(), GuiMain.gui);
            portal.addObserver(m2);
            portal.addObserver(mg2);
            addAgent.setEnabled(true);
            addSocket.setEnabled(true);
            addPortal.setEnabled(false);
        } else if (e.getSource().equals(addSocket)) {
            String address = JOptionPane.showInputDialog("Please input the address for the socket: ");
            Socket s;
            try {
                s = new Socket(address, 42069);
                SocketAgent a;
                switch(JOptionPane.showOptionDialog(null, "What do you want to connect", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Portal", "Router"}, null)){
                    case JOptionPane.YES_OPTION:
                        //portal
                        System.out.println("creating portal socket");
                        a = new SocketAgent(portal, s);
                        a.start();
                        
                        a.messageHandler(portal, new Message(portal.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));
                        
                        break;
                    case JOptionPane.NO_OPTION:
                        //router
                        System.out.println("creating router socket");
                        a = new SocketAgent(router, s);
                        a.start();
                        
                        a.messageHandler(router, new Message(router.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));
                        
                        break;
                    default:
                        return;
                }
                
                System.out.println("Created");
            } catch (IOException ex) {
                Logger.getLogger(ObserverInterface.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Failed");
            }
        } else if (e.getSource().equals(addRouter)) {
            try {
                String routerName = JOptionPane.showInputDialog("Please input the name of the router: ");
                router = new Router(routerName);
                CMDMonitor m1 = new CMDMonitor(router.getName());
                GUIMonitor mg1 = new GUIMonitor(router.getName(), GuiMain.gui);
                router.addObserver(m1);
                router.addObserver(mg1);
                Thread t = new Thread(router);
                t.start();
                addRouter.setEnabled(false);
                addSocket.setEnabled(true);
            } catch (IOException ex) {
                Logger.getLogger(ObserverInterface.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Could not create router", "error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Nothing has been selected.");
            System.out.println("Error - Not yet implemented.");
        }
    }

    public void update(Message msg, String actual) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        DefaultTableModel model = (DefaultTableModel) record.getModel();
        model.addRow(new Object[]{msg.getSender(), msg.getRecipient(), actual, msg.getMessageType(), date});
        record.setModel(model);
    }

}
