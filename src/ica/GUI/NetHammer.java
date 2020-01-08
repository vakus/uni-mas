
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
 * This class is used to create dialogue which asks for data when NetHammer is
 * ran.
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

    private final JTextField text_ip;
    private final JSpinner number_portal_offset;
    private final JSpinner number_portal;
    private final JSpinner number_agent_offset;
    private final JSpinner number_agent;
    private final JSpinner number_messages;
    private final JSpinner timeout;
    private boolean cancelled;

    public NetHammer() {

        setTitle("NetHammer");
        setModal(true);

        cancelled = true;

        setLayout(new GridBagLayout());

        text_ip = new JTextField("127.0.0.1");
        number_portal = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 1));
        number_portal_offset = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        number_agent = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 1));
        number_agent_offset = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        number_messages = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 1));
        timeout = new JSpinner(new SpinnerNumberModel(10, 1, 3600, 1));

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
        add(new JLabel("Time in seconds to wait after messages were sent:"), constraints);

        constraints.gridx = 2;
        add(timeout, constraints);

        constraints.gridx = 1;
        constraints.gridy = 9;
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

    /**
     * Returns IP entered by user. Before relaying on this data, check that the
     * message was not cancelled via {@link #isCancelled}.
     *
     * @return The IP address entered by user or default "127.0.0.1".
     */
    public String getIP() {
        return text_ip.getText();
    }

    /**
     * Returns the Offset to be used by portals creator. Before relaying on this
     * data, check that the message was not cancelled via {@link #isCancelled}.
     *
     * @return The Offset for portal number.
     */
    public int getPortalsOffset() {
        return (int) number_portal_offset.getValue();
    }

    /**
     * Returns the number of portals to be created. Before relaying on this
     * data, check that the message was not cancelled via {@link #isCancelled}.
     *
     * @return The number of portals to be created.
     */
    public int getPortals() {
        return (int) number_portal.getValue();
    }

    /**
     * Returns the number of agents to be created. Before relaying on this data,
     * check that the message was not cancelled via {@link #isCancelled}
     *
     * @return The number of agents to be created
     */
    public int getAgents() {
        return (int) number_agent.getValue();
    }

    /**
     * Returns the offset to be used by agents creator. Before relaying on this
     * data, check that the message was not cancelled via {@link #isCancelled}
     *
     * @return The offset for agent number.
     */
    public int getAgentsOffset() {
        return (int) number_agent_offset.getValue();
    }

    /**
     * Returns the number of messages to be send. Before relaying on this data,
     * check that the message was not cancelled via {@link #isCancelled}
     *
     * @return The number of message to be sent.
     */
    public int getMessages() {
        return (int) number_messages.getValue();
    }

    /**
     * Returns the time in seconds to wait after the messages were sent. Before
     * relaying on this data, check that the message was not cancelled via
     * {@link #isCancelled}
     *
     * @return The time in seconds to wait after submitting all messages.
     */
    public int getTimeout() {
        return (int) timeout.getValue();
    }

    /**
     * Returns whatever the user have cancelled the event by closing the window
     * or pressing the "Cancel" button. If the test is supposed to still run and
     * the user entered valid values then this function should return false.
     *
     * @return {@code True} if user exited dialogue, or pressed cancel.
     * {@code False} if user wants to continue with the test
     */
    public boolean isCancelled() {
        return cancelled;
    }

}
