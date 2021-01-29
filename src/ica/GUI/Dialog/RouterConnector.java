/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.GUI.Dialog;

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
 * @author vakus
 */
public class RouterConnector extends JDialog {

    private JTextField routerIP;
    private JSpinner port;

    private boolean cancelled;

    public RouterConnector() {

        cancelled = true;

        setModal(true);
        setTitle("Connect to Router");

        setLayout(new GridBagLayout());

        routerIP = new JTextField("127.0.0.1");
        port = new JSpinner(new SpinnerNumberModel(42069, 0, 65535, 1));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        
        add(new JLabel("Router IP:"), constraints);
        constraints.gridx++;
        
        add(routerIP, constraints);
        
        constraints.gridy++;
        constraints.gridx = 1;
        
        add(new JLabel("Router Port:"), constraints);
        
        constraints.gridx++;
        add(port, constraints);
        
        constraints.gridy++;
        constraints.gridx = 1;
        JButton btn_ok = new JButton("Create");
        btn_ok.addActionListener((ActionEvent e) -> {
            cancelled = false;
            setVisible(false);
        });

        add(btn_ok, constraints);

        constraints.gridx++;
        JButton btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener((ActionEvent e) -> {
            setVisible(false);
        });
        add(btn_cancel, constraints);

        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public String getRouterIP() {
        return routerIP.getText();
    }

    public int getRouterPort() {
        return (int) port.getValue();
    }

    public boolean isCancelled() {
        return cancelled;
    }
    
    
}
