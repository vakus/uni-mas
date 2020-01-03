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
public class ObserverInterface {

    private final Dimension size;
    JPanel buttonsPanel;

    final String[] columnNames = {"Sender", "Recipient", "Actual Recipient", "Message Type", "Date"};
    JTable record;
    JScrollPane scrollPane;
    final JPanel mainPanel;
    public Object[][] data = new Object[][]{};

    private Portal portal;
    private Router router;

    public ObserverInterface(Dimension d) {

        record = new JTable(new DefaultTableModel(data, columnNames));
        scrollPane = new JScrollPane(record);
        size = d;

        mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setSize(size);


        mainPanel.add(scrollPane);
    }

    public void update(Message msg, String actual) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        DefaultTableModel model = (DefaultTableModel) record.getModel();
        model.addRow(new Object[]{msg.getSender(), msg.getRecipient(), actual, msg.getMessageType(), formatter.format(date)});
        record.setModel(model);
        //auto scroll
        record.changeSelection(record.getRowCount()-1, 0, false, false);
    }

}
