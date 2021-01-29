package ica.GUI;

import ica.messages.Message;
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

    public final String[] columnNames = {"No", "Direction", "Sender", "Actual Sender", "Recipient", "Actual Recipient", "Message Type", "Date", "Content"};
    public JTable record;
    public final JPanel mainPanel;
    public Object[][] data;
    private long msgCount;

    /**
     * Constructor to create the interface for the observer, the main part to
     * this is the creation of the JTable that is sued to monitor the message
     * traffic over the network.
     *
     * @author v8036651
     */
    public ObserverInterface() {

        data = new Object[][]{};

        msgCount = 0;
        record = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(record);

        mainPanel = new JPanel(new GridLayout(1, 1));

        mainPanel.add(scrollPane);
    }

    /**
     * Update method that is used to update the JTable when a message is sent,
     * it also writes to a file whenever it updates the table and limits the
     * number of entries in the table to 500.
     *
     * @param msg the message which is being logged
     * @param direction information whatever the message is being send or
     * received
     * @param actualRecipient the name of the node which received the message
     * @param actualSender the name of the node which have forwarded the message
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
