/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamonitors;

import icamessages.Message;
import java.util.ArrayList;

/**
 *
 * @author v8036651
 */
public class Observer {

    private ArrayList<Monitor> observers;

    /**
     * Constructor for the observer object.
     *
     * @param observers
     * @author v8036651
     */
    public Observer(ArrayList<Monitor> observers) {
        this.observers = observers;
    }

    public Observer() {
        this.observers = new ArrayList<>();
    }

    /**
     * Getter for the array list of observers.
     *
     * @return
     * @author v8036651
     */
    public ArrayList<Monitor> getObservers() {
        return observers;
    }

    /**
     * Setter for the array list of observers.
     *
     * @param observers
     * @author v8036651
     */
    public void setObservers(ArrayList<Monitor> observers) {
        this.observers = observers;
    }

    /**
     * adds observer into the list
     *
     * @param obs observer to add
     * @author v8073331
     */
    public void addObserver(Monitor obs) {
        this.observers.add(obs);
    }

    /**
     * Updates all the monitors that are in use when called, this is done upon
     * sending a message.
     *
     * @param msg
     * @param actualSender
     * @author v8036651
     * @author v8073331
     */
    public void updateSender(Message msg, String actualSender) {
        observers.forEach((Monitor o) -> {
            try {
                o.SentMessage(msg, actualSender);
            } catch (Exception e) {
            }
        });
    }

    /**
     * Updates all the monitors that are in use when called, this is done upon
     * receiving a message.
     *
     * @param msg
     * @param actualSender
     * @author v8036651
     * @author v8073331
     */
    public void updateReceiver(Message msg,  String actualSender) {
        observers.forEach((Monitor o) -> {
            try {
                o.ReceivedMessage(msg, actualSender);
            } catch (Exception e) {
            }
        });
    }

}
