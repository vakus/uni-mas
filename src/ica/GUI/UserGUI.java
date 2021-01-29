package ica.GUI;

import ica.messages.Message;
import ica.messages.MessageType;
import ica.messages.ReceivedMessage;
import ica.metaagent.Portal;
import ica.metaagent.User;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * This is the user GUI class, the purpose of this class is to build the GUI
 * that is used by a user of the program to send messages to other users.
 *
 * @author v8036651
 */
public class UserGUI extends User{

    private final UserInterface iFace;

    /**
     * Constructor for the user GUI class which requires an agent to be passed,
     * this is a user agent and is passed as to allow the send message and other
     * methods that are defined in the user agent class.
     *
     * @param name
     * @param connection
     */
    public UserGUI(String name, Portal connection) {
        super(name, connection);
        JFrame userFrame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int) (screenSize.getWidth() * 0.275), (int) (screenSize.getHeight() * 0.45));

        iFace = new UserInterface(this);
        userFrame = new JFrame(this.getName());
        userFrame.getContentPane().add(iFace.mainPanel);
        userFrame.setSize(frameSize);
        userFrame.setVisible(true);
        userFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        /**
         * A lot of the methods here are used as part of the window listener and
         * the override methods are required.
         */
        userFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent event) {
            }

            @Override
            public void windowClosing(WindowEvent event) {
                connection.messageHandler(UserGUI.this, new Message(UserGUI.this.getName(), "GLOBAL", MessageType.REMOVE_METAAGENT, ""));
                userFrame.setVisible(false);
            }

            @Override
            public void windowClosed(WindowEvent event) {
            }

            @Override
            public void windowIconified(WindowEvent event) {
            }

            @Override
            public void windowDeiconified(WindowEvent event) {
            }

            @Override
            public void windowActivated(WindowEvent event) {
            }

            @Override
            public void windowDeactivated(WindowEvent event) {
            }
        });
        userFrame.setLocationRelativeTo(null);

        userFrame.setTitle(this.getName());
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                ReceivedMessage receivedMessage = messageQueue.take();
                
                Message msg = receivedMessage.getMessage();

                if (msg.getRecipient().equals(this.name)) {
                    iFace.displayMessage(msg.getSender(), msg.getMessageDetails());
                } else {
                    connection.messageHandler(this, new Message(this.name, msg.getSender(), MessageType.ERROR, "Message recieved by wrong agent"));
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
