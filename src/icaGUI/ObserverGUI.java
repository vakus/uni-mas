/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icaGUI;

import com.sun.glass.events.KeyEvent;
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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author v8036651
 * @author v8077971
 * @author v8073331
 */
public class ObserverGUI {

    private JFrame mainFrame;
    private ObserverInterface iFace;
    private Dimension frameSize;
    private Dimension screenSize;
    private JMenuBar menubar;

    private JMenuItem routerCreate;
    private JMenuItem routerConnect;
    private JMenuItem routerStop;

    public ObserverGUI() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = new Dimension((int) (screenSize.getWidth() * 0.375), (int) (screenSize.getHeight() * 0.45));

        iFace = new ObserverInterface(frameSize);
        mainFrame = new JFrame("Observer");
        mainFrame.getContentPane().add(iFace.mainPanel);
        mainFrame.setSize(frameSize);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        menubar = new JMenuBar();

        JMenu menuRouter = new JMenu("Router");
        menuRouter.setMnemonic(KeyEvent.VK_R);

        routerCreate = new JMenuItem("Create Router");
        routerCreate.setMnemonic(KeyEvent.VK_C);
        routerCreate.addActionListener((ActionEvent e) -> {
            try {
                String routerName = JOptionPane.showInputDialog("Please input the name of the router: ");
                GuiMain.router = new Router(routerName);
                CMDMonitor m1 = new CMDMonitor(GuiMain.router.getName());
                GUIMonitor mg1 = new GUIMonitor(GuiMain.router.getName(), GuiMain.gui);
                GuiMain.router.addObserver(m1);
                GuiMain.router.addObserver(mg1);
                Thread t = new Thread(GuiMain.router);
                t.start();
                routerCreate.setEnabled(false);
                routerConnect.setEnabled(true);
                routerStop.setEnabled(true);
            } catch (IOException ex) {
                Logger.getLogger(ObserverInterface.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Could not create router", "error", JOptionPane.ERROR_MESSAGE);
            }
        });
        menuRouter.add(routerCreate);

        routerConnect = new JMenuItem("Connect to Router");
        routerConnect.setMnemonic(KeyEvent.VK_O);
        routerConnect.setEnabled(false);
        routerConnect.addActionListener((ActionEvent e) -> {

            try {
                String address = JOptionPane.showInputDialog("Please input the address for the socket: ");
                Socket s = new Socket(address, 42069);
                SocketAgent a = new SocketAgent(GuiMain.router, s);
                a.start();

                a.messageHandler(GuiMain.router, new Message(GuiMain.router.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));
            } catch (IOException ex) {
                Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Could not connect router", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        menuRouter.add(routerConnect);

        routerStop = new JMenuItem("Stop Router");
        routerStop.setMnemonic(KeyEvent.VK_S);
        routerStop.setEnabled(false);
        routerStop.addActionListener((ActionEvent e) -> {
            //no clue for now how to disconnect router cleanly from network of routers
        });
        menuRouter.add(routerStop);

        menubar.add(menuRouter);

        JMenu menuPortal = new JMenu("Portal");
        menuPortal.setMnemonic(KeyEvent.VK_P);

        JMenuItem portalCreate = new JMenuItem("Create Portal");
        portalCreate.setMnemonic(KeyEvent.VK_C);
        portalCreate.addActionListener((ActionEvent e) -> {

            String portalName = JOptionPane.showInputDialog("Please input the name of the portal: ");
            Portal portal = new Portal(portalName);
            CMDMonitor m2 = new CMDMonitor(portal.getName());
            GUIMonitor mg2 = new GUIMonitor(portal.getName(), GuiMain.gui);
            portal.addObserver(m2);
            portal.addObserver(mg2);

            JMenu portalsMenu = new JMenu(portalName);

            JMenuItem portalsConnect = new JMenuItem("Connect to Router");
            portalsConnect.setMnemonic(KeyEvent.VK_C);
            portalsConnect.addActionListener((ActionEvent e1) -> {
                try {
                    String address = JOptionPane.showInputDialog(null, "Please input the address for the socket: ", "127.0.0.1");
                    Socket s = new Socket(address, 42069);
                    SocketAgent a = new SocketAgent(portal, s);
                    a.start();

                    a.messageHandler(portal, new Message(portal.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));
                } catch (IOException ex) {
                    Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Could not connect router", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            portalsMenu.add(portalsConnect);

            JMenuItem portalsAddAgent = new JMenuItem("Add Agent");
            portalsAddAgent.setMnemonic(KeyEvent.VK_A);
            portalsAddAgent.addActionListener((ActionEvent e1) -> {
                String name = JOptionPane.showInputDialog("Please input the name of the user: ");
                User agent = new User(name, portal);

                portal.messageHandler(agent, new Message(agent.getName(), "GLOBAL", MessageType.ADD_METAAGENT, ""));
            });
            portalsMenu.add(portalsAddAgent);

            JMenuItem portalsStop = new JMenuItem("Stop Portal");
            portalsStop.setMnemonic(KeyEvent.VK_S);
            portalsStop.addActionListener((ActionEvent e1) -> {
                portal.shutdown();
                menuPortal.remove(portalsMenu);
            });
            portalsMenu.add(portalsStop);

            menuPortal.add(portalsMenu);
        });
        menuPortal.add(portalCreate);

        menuPortal.addSeparator();

        menubar.add(menuPortal);

        mainFrame.setJMenuBar(menubar);

        TitleClock clock = new TitleClock(mainFrame);
        //clock.run();

    }

    public void updateTable(Message msg, String actual) {
        iFace.update(msg, actual);
    }
}
