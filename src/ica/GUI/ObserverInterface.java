/*
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

    final String[] columnNames = {"No", "Direction", "Sender", "Actual Sender", "Recipient", "Actual Recipient", "Message Type", "Date", "Content"};
    JTable record;
    JScrollPane scrollPane;
    final JPanel mainPanel;
    public Object[][] data = new Object[][]{};
    private long msgcnt;


    public ObserverInterface(Dimension d) {

        
        msgcnt = 0;
        record = new JTable(new DefaultTableModel(data, columnNames));
        scrollPane = new JScrollPane(record);
        size = d;

        mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setSize(size);


        mainPanel.add(scrollPane);
    }

    public void update(Message msg, String direction, String actualRecipient, String actualSender) {
        msgcnt += 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        DefaultTableModel model = (DefaultTableModel) record.getModel();
        
        if(model.getRowCount() > 500){
            model.removeRow(0);
        }
        
        model.addRow(new Object[]{msgcnt, direction, msg.getSender(), actualSender, msg.getRecipient(), actualRecipient, msg.getMessageType(), formatter.format(date), msg.getMessageDetails()});
        record.setModel(model);
        //auto scroll
        record.changeSelection(record.getRowCount()-1, 0, false, false);
    }

}
