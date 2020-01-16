/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.NetHammer;

import com.sun.glass.events.KeyEvent;
import ica.GUI.ObserverGUI;
import ica.messages.Message;
import ica.messages.MessageType;
import ica.metaagent.Portal;
import ica.metaagent.SocketAgent;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author vakus
 */
public class NetHammer {
    
    public static void addMenu(JMenuBar menubar){
        JMenu menuNetHammer = new JMenu("NetHammer");
        menuNetHammer.setMnemonic(KeyEvent.VK_N);

        JMenuItem netHammerStart = new JMenuItem("Start NetHammer");
        netHammerStart.setMnemonic(KeyEvent.VK_S);
        netHammerStart.addActionListener((ActionEvent e) -> {
            NetHammerDialogue hammer = new NetHammerDialogue();
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
                    JOptionPane.showMessageDialog(null, "Initialisation of NetHammer was unsuccessful", "NetHammer", JOptionPane.ERROR_MESSAGE);
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

                JOptionPane.showMessageDialog(null, "NetHammer stress test ready. Press OK when you are ready to send messages", "NetHammer", JOptionPane.INFORMATION_MESSAGE);

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
                    Thread.sleep(hammer.getTimeout() * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObserverGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (Portal portal : portals) {
                    portal.shutdown();
                }

                for (NetHammerUser user : users) {
                    user.shutdown();
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
                JOptionPane.showMessageDialog(null, "NetHammer Results:\nMin: " + min + "ms\nMax: " + max + "ms\nAverage: " + (averageTotal / users.length) + "ms", "NetHammer Result", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menuNetHammer.add(netHammerStart);

        menubar.add(menuNetHammer);
    }
}