package ica.GUI;

import ica.GUI.Dialog.RouterCreator;
import com.sun.glass.events.KeyEvent;
import ica.GUI.Dialog.RouterConnector;
import ica.NetHammer.NetHammer;
import ica.main.GuiMain;
import ica.messages.Message;
import ica.messages.MessageType;
import ica.metaagent.Portal;
import ica.metaagent.Router;
import ica.metaagent.SocketAgent;
import ica.monitors.CMDMonitor;
import ica.monitors.GUIMonitor;
import java.awt.event.ActionEvent;
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
 * This class is to be used to create the observer graphical user interface that
 * is being used to register the messages that are being sent throughout the
 * network, This also sets up the drop down menu bars and adds the action
 * listener to each option.
 *
 * @author v8036651
 * @author v8077971
 * @author v8073331
 */
public class ObserverGUI {

    private final JFrame mainFrame;
    private final ObserverInterface iFace;
    private final JMenuBar menubar;

    private JMenuItem routerCreate;
    private JMenuItem routerConnect;
    private JMenuItem routerStop;

    /**
     * Observer GUI constructor, this sets up the observer view that monitors
     * the messages that are being sent over the network.
     *
     * @author v8036651
     * @author v8073331
     */
    public ObserverGUI() {
        iFace = new ObserverInterface();
        mainFrame = new JFrame("Observer");
        mainFrame.add(iFace.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);

        menubar = new JMenuBar();

        JMenu menuRouter = new JMenu("Router");
        menuRouter.setMnemonic(KeyEvent.VK_R);

        routerCreate = new JMenuItem("Create Router");
        routerCreate.setMnemonic(KeyEvent.VK_C);
        routerCreate.addActionListener((ActionEvent e) -> {
            try {

                RouterCreator rc = new RouterCreator();
                if (!rc.isCancelled()) {
                    Router router = new Router(rc.getRouterName(), rc.getRouterPort());
                    Thread routerThread = new Thread(router, router.getName());
                    routerThread.start();
                    //CMDMonitor m1 = new CMDMonitor(router.getName());
                    GUIMonitor mg1 = new GUIMonitor(router.getName(), GuiMain.gui);
                    //router.addObserver(m1);
                    router.addObserver(mg1);

                    JMenu routersMenu = new JMenu(router.getName());

                    JMenuItem routerConnect = new JMenuItem("Connect to Router");

                    routerConnect.setMnemonic(KeyEvent.VK_O);
                    routerConnect.addActionListener((ActionEvent e1) -> {

                        try {

                            RouterConnector rc1 = new RouterConnector();
                            if (!rc.isCancelled()) {
                                Socket s = new Socket(rc1.getRouterIP(), rc1.getRouterPort());
                                SocketAgent a = new SocketAgent(router, s);
                                a.start();

                                a.messageHandler(router, new Message(router.getName(), "GLOBAL", MessageType.REQUEST_ROUTER_ADDRESSES, ""));
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Could not connect to router", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    JMenuItem routerStop = new JMenuItem("Stop Router");

                    routerStop.setMnemonic(KeyEvent.VK_S);
                    routerStop.addActionListener((ActionEvent e1) -> {
                        router.shutdown();
                        menuRouter.remove(routersMenu);
                    });

                    routersMenu.add(routerConnect);
                    routersMenu.add(routerStop);
                    menuRouter.add(routersMenu);

                }
            } catch (IOException ex) {
                Logger.getLogger(ObserverInterface.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Could not create router", "error", JOptionPane.ERROR_MESSAGE);
            }
        });
        menuRouter.add(routerCreate);
        menuRouter.addSeparator();

        menubar.add(menuRouter);

        JMenu menuPortal = new JMenu("Portal");
        menuPortal.setMnemonic(KeyEvent.VK_P);

        JMenuItem portalCreate = new JMenuItem("Create Portal");
        portalCreate.setMnemonic(KeyEvent.VK_C);
        portalCreate.addActionListener((ActionEvent e) -> {

            String portalName = JOptionPane.showInputDialog("Please input the name of the portal: ");
            Portal portal = new Portal(portalName);
            //CMDMonitor monitor2 = new CMDMonitor(portal.getName());
            GUIMonitor monitorgui2 = new GUIMonitor(portal.getName(), GuiMain.gui);
            //portal.addObserver(monitor2);
            portal.addObserver(monitorgui2);

            JMenu portalsMenu = new JMenu(portalName);

            JMenuItem portalsConnect = new JMenuItem("Connect to Router");
            portalsConnect.setMnemonic(KeyEvent.VK_C);
            portalsConnect.addActionListener((ActionEvent e1) -> {
                try {

                    RouterConnector rc = new RouterConnector();
                    if (!rc.isCancelled()) {
                        Socket socket = new Socket(rc.getRouterIP(), rc.getRouterPort());
                        SocketAgent socketAgent = new SocketAgent(portal, socket);
                        socketAgent.start();

                        socketAgent.messageHandler(portal, new Message(portal.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));
                    }
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
                //Add check here
                if (portal.isNameAllowed(name)) {
                    UserGUI agent = new UserGUI(name, portal);
                    portal.messageHandler(agent, new Message(agent.getName(), "GLOBAL", MessageType.ADD_METAAGENT, ""));
                } else {
                    JOptionPane.showMessageDialog(null, "Could not create agent as one already exists with that name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

        NetHammer.addMenu(menubar);

        mainFrame.setJMenuBar(menubar);

        TitleClock clock = new TitleClock(mainFrame);

        mainFrame.setVisible(true);
    }

    /**
     * This method updates the JTable that is logging the messages that are
     * being sent across the network regardless of the message type.
     *
     * @param msg the message which should be logged into the table
     * @param direction information whatever the message is being send or
     * received
     * @param actualRecipient the name of the node which received the message
     * @param actualSender the name of the node which forwarded the message
     * @author v8036651
     */
    public void updateTable(Message msg, String direction, String actualRecipient, String actualSender) {
        iFace.update(msg, direction, actualRecipient, actualSender);
    }
}
