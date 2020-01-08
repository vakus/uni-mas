/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.GUI;

import com.sun.glass.events.KeyEvent;
import ica.main.GuiMain;
import ica.messages.Message;
import ica.messages.MessageType;
import ica.metaagent.NetHammerUser;
import ica.metaagent.Portal;
import ica.metaagent.Router;
import ica.metaagent.SocketAgent;
import ica.metaagent.User;
import ica.monitors.CMDMonitor;
import ica.monitors.GUIMonitor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
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
                //GuiMain.router.addObserver(m1);
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

                a.messageHandler(GuiMain.router, new Message(GuiMain.router.getName(), "GLOBAL", MessageType.REQUEST_ROUTER_ADDRESSES, ""));
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

        JMenu menuNetHammer = new JMenu("NetHammer");
        menuNetHammer.setMnemonic(KeyEvent.VK_N);

        JMenuItem netHammerStart = new JMenuItem("Start NetHammer");
        netHammerStart.setMnemonic(KeyEvent.VK_S);
        netHammerStart.addActionListener((ActionEvent e) -> {
            NetHammer hammer = new NetHammer();
            System.out.println("Here");
            if (!hammer.isCancelled()) {

                Portal[] portals = new Portal[hammer.getPortals()];
                try {
                    for (int x = 0; x < hammer.getPortals(); x++) {

                        Portal portal = new Portal("p-" + (x + hammer.getPortalsOffset()));
                        Socket socket = new Socket(hammer.getIP(), 42069);
                        SocketAgent sA = new SocketAgent(portal, socket);
                        sA.start();

                        sA.messageHandler(portal, new Message(portal.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));

                        portals[x] = portal;

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(mainFrame, "Initialisation of NetHammer was unsuccessful", "NetHammer", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                NetHammerUser[] users = new NetHammerUser[hammer.getAgents() * hammer.getPortals()];

                for (int x = 0; x < hammer.getPortals(); x++) {
                    for (int y = 0; y < hammer.getAgents(); y++) {
                        NetHammerUser user = new NetHammerUser("a-" + ((x * hammer.getAgents()) + y + hammer.getAgentsOffset()), portals[x]);

                        portals[x].messageHandler(user, new Message(user.getName(), "GLOBAL", MessageType.ADD_METAAGENT, ""));

                        users[x * hammer.getAgents() + y] = user;
                    }
                }

                Random rand = new Random();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                int numberOfAgentsTotal = 0;
                ArrayList<String> keyList = new ArrayList<>(portals[0].getRoutingTable().keySet());

                for (int x = 0; x < keyList.size(); x++) {
                    if (keyList.get(x).startsWith("a-")) {
                        numberOfAgentsTotal++;
                    }
                }

                for (int x = 0; x < hammer.getAgents() * hammer.getPortals(); x++) {
                    for (int y = 0; y < hammer.getMessages(); y++) {
                        int randUser = rand.nextInt(numberOfAgentsTotal);

                        users[x].connection.messageHandler(users[x], new Message(users[x].getName(), ("a-" + randUser), MessageType.USER_MSG, String.valueOf(System.currentTimeMillis())));
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (int x = 0; x < portals.length; x++) {
                    portals[x].shutdown();
                }

                //calculate the results and display them
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                long averageTotal = 0;
                for (int x = 0; x < users.length; x++) {
                    if (users[x].getMaxTime() > max) {
                        max = users[x].getMaxTime();
                    }
                    if (users[x].getMinTime() < min) {
                        min = users[x].getMinTime();
                    }
                    averageTotal += users[x].getAverageTime();
                }
                JOptionPane.showMessageDialog(mainFrame, "NetHammer Results:\nMin: " + min + "ms\nMax: " + max + "ms\nAverage: " + (averageTotal / users.length) + "ms", "NetHammer Result", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menuNetHammer.add(netHammerStart);

        menubar.add(menuNetHammer);

        mainFrame.setJMenuBar(menubar);

        new TitleClock(mainFrame);
    }

    public void updateTable(Message msg, String direction, String actualRecipient, String actualSender) {
        iFace.update(msg, direction, actualRecipient, actualSender);
    }
}
