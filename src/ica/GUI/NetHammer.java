
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author v8073331
 */
public class NetHammer extends JDialog {

    private static final String DISCLAIMER
            = "<html>You are about to run NetHammer stress test on the network.<br/>"
            + "This may result in heavy CPU and bandwidth usage.<br/>"
            + "It is adviced to use NetHammer on networks without any Portals "
            + "or Agents, as NetHammer can not test those.<br/>"
            + "ONLY RUN NETHAMMER ON AUTHORIZED AND NON-CRITICAL NETWORKS AS IT"
            + " MAY CAUSE DENIAL OF SERVICE.<br/>"
            + "IT IS ILLEGAL TO RUN NETHAMMER ON UN-AUTHORIZED NETWORKS.<br/>"
            + "After running NetHammer, the program may appear to hang for few "
            + "seconds.</html>";

    private JTextField text_ip;
    private JSpinner number_portal_offset;
    private JSpinner number_portal;
    private JSpinner number_agent_offset;
    private JSpinner number_agent;
    private JSpinner number_messages;
    private JSpinner number_agents_total;
    private boolean cancelled;

    public NetHammer() {

        setTitle("NetHammer");
        setModal(true);

        cancelled = true;

        setLayout(new GridBagLayout());

        text_ip = new JTextField();
        number_portal = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 1));
        number_portal_offset = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        number_agent = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 1));
        number_agent_offset = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        number_messages = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 1));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        add(new JLabel(DISCLAIMER), constraints);

        constraints.gridwidth = 1;

        constraints.gridy = 2;
        add(new JLabel("Router IP address:"), constraints);

        constraints.gridx = 2;
        add(text_ip, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        add(new JLabel("Number of Portals:"), constraints);

        constraints.gridx = 2;
        add(number_portal, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        add(new JLabel("Portal Offset:"), constraints);

        constraints.gridx = 2;
        add(number_portal_offset, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        add(new JLabel("Number of Agents:"), constraints);

        constraints.gridx = 2;
        add(number_agent, constraints);

        constraints.gridx = 1;
        constraints.gridy = 6;
        add(new JLabel("Agents Offset:"), constraints);

        constraints.gridx = 2;
        add(number_agent_offset, constraints);

        constraints.gridx = 1;
        constraints.gridy = 7;
        add(new JLabel("Number of Messages:"), constraints);

        constraints.gridx = 2;
        add(number_messages, constraints);

        constraints.gridx = 1;
        constraints.gridy = 8;
        JButton btn_ok = new JButton("Initiate NetHammer");
        btn_ok.setForeground(Color.RED);
        btn_ok.addActionListener((ActionEvent e) -> {
            cancelled = false;
            setVisible(false);
        });
        add(btn_ok, constraints);

        constraints.gridx = 2;
        JButton btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });
        add(btn_cancel, constraints);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getIP() {
        return text_ip.getText();
    }

    public int getPortalsOffset() {
        return (int) number_portal_offset.getValue();
    }

    public int getPortals() {
        return (int) number_portal.getValue();
    }

    public int getAgents() {
        return (int) number_agent.getValue();
    }

    public int getAgentsOffset() {
        return (int) number_agent_offset.getValue();
    }

    public int getMessages() {
        return (int) number_messages.getValue();
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
