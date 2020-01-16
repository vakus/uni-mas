package ica.main;

import ica.GUI.ObserverGUI;
import ica.metaagent.Router;

/**
 * This is the main driver class that uses the GUI and would be given to the end
 * user.
 *
 * @author v8073331
 */
public class GuiMain {

    public static ObserverGUI gui;
    public static Router router;
    public static Thread routerThread;

    /**
     * This is the main class that will run and simply loads the GUI that then
     * takes over from this point and all functions are called by user input
     * from that point onwards.
     *
     * @param args arguments passed by the console
     */
    public static void main(String[] args) {
        gui = new ObserverGUI();
    }
}
