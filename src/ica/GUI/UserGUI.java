package ica.GUI;

import ica.messages.Message;
import ica.messages.MessageType;
import ica.metaagent.User;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 * This is the user GUI class, the purpose of this class is to build the GUI
 * that is used by a user of the program to send messages to other users.
 *
 * @author v8036651
 */
public class UserGUI {

    private User user;
    private UserInterface iFace;

    /**
     * Constructor for the user GUI class which requires an agent to be passed,
     * this is a user agent and is passed as to allow the send message and other
     * methods that are defined in the user agent class.
     *
     * @param agent the user agent which is being connected to the GUI
     */
    public UserGUI(User agent) {
        user = agent;
        JFrame userFrame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int) (screenSize.getWidth() * 0.275), (int) (screenSize.getHeight() * 0.45));

        iFace = new UserInterface(user);
        userFrame = new JFrame(user.getName());
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
                user.connection.messageHandler(user, new Message(user.getName(), "GLOBAL", MessageType.REMOVE_METAAGENT, ""));
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

        userFrame.setTitle(user.getName());
    }

    /**
     * This method is called when a message is received by a user agent and
     * calls the update method from the suer interface which updates the JTable
     * with the new message details.
     *
     * @param sender the source of the message
     * @param details the message content
     */
    public void recivedMessage(String sender, String details) {
        iFace.displayMessage(sender, details);
    }
}
