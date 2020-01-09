/*
This package holds all the different graphical user interface (GUI) calsses.
 * It is called by the main run methods can has calsses that are passed as parameters
 * to the constructors of other packages. These classes describe the layout for the
 * observer GUI and the layout for the user interface. The methods in these classes
 * are used to help update the GUI's and to display messages. These methods are called
 * within methods of different classes and packages.
 *
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.GUI;

import ica.messages.Message;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * This class is in place to set up the observer GUI class by adding the JTable
 * that will log the message data and more information about about a message,
 * this class also also has the update method that is required for the JTable.
 *
 * @author v8077971
 * @author v8036651
 * @author v8073331
 */
public class ObserverInterface {

    private final Dimension size;
    public JPanel buttonsPanel;

    public final String[] columnNames = {"No", "Direction", "Sender", "Actual Sender", "Recipient", "Actual Recipient", "Message Type", "Date", "Content"};
    public JTable record;
    public JScrollPane scrollPane;
    public final JPanel mainPanel;
    public Object[][] data = new Object[][]{};
    private long msgCount;

    /**
     * Constructor to create the interface for the observer, the main part to
     * this is the creation of the JTable that is sued to monitor the message
     * traffic over the network.
     *
     * @param dimension
     * @author v8036651
     */
    public ObserverInterface(Dimension dimension) {

        msgCount = 0;
        record = new JTable(new DefaultTableModel(data, columnNames));
        scrollPane = new JScrollPane(record);
        size = dimension;

        mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setSize(size);

        mainPanel.add(scrollPane);
    }

    /**
     * Update method that is used to update the JTable when a message is sent,
     * it also writes to a file whenever it updates the table and limits the
     * number of entries in the table to 500.
     *
     * @param msg
     * @param direction
     * @param actualRecipient
     * @param actualSender
     * @author v8036651
     */
    public void update(Message msg, String direction, String actualRecipient, String actualSender) {
        msgCount += 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        DefaultTableModel model = (DefaultTableModel) record.getModel();

        if (model.getRowCount() > 500) {
            model.removeRow(0);
        }

        model.addRow(new Object[]{msgCount, direction, msg.getSender(), actualSender, msg.getRecipient(), actualRecipient, msg.getMessageType(), formatter.format(date), msg.getMessageDetails()});
        record.setModel(model);
        //auto scroll
        record.changeSelection(record.getRowCount() - 1, 0, false, false);
    }

}
