package ica.main;

import ica.GUI.ObserverGUI;
import ica.metaagent.Router;

/*
 * This is the Driver class and is used to run the program in a specific way.
 * 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This is the main driver class that uses the GUI and would be given to the end
 * user.
 *
 * @author v8073331
 */
public class GuiMain {

    public static ObserverGUI gui;
    public static Router router;

    /**
     *This is the main class that will run and simply loads the GUI that then 
     * takes over from this point and all functions are called by user input 
     * from that point onwards.
     * 
     * @param args
     */
    public static void main(String[] args) {
        gui = new ObserverGUI();
    }
}
