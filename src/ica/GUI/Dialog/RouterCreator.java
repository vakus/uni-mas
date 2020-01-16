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
public class RouterCreator extends JDialog {

    private JTextField routerName;
    private JSpinner port;

    private boolean cancelled;

    public RouterCreator() {

        cancelled = true;

        setModal(true);
        setTitle("Create Router");

        setLayout(new GridBagLayout());

        routerName = new JTextField("router");
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

        add(new JLabel("Router Name:"), constraints);

        constraints.gridx++;
        add(routerName, constraints);

        constraints.gridy++;
        constraints.gridx = 1;
        add(new JLabel("Port number:"), constraints);

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
    
    public boolean isCancelled(){
        return cancelled;
    }
    
    public String getRouterName(){
        return routerName.getText();
    }
    
    public int getRouterPort(){
        return (int) port.getValue();
    }
}
