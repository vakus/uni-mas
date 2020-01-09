/*
 * This package is used for monitoring what is being sent across the network in 
 * different ways, it adds observers to each MetaAgent when they are created which 
 * refer back to the observers that are defined.
 *
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.monitors;

import ica.messages.Message;
import java.util.ArrayList;

/**
 * This class is for the observer, this registers a change in an object and
 * reports it back to the monitor depending on what the change is allowing the
 * monitor to be updated.
 *
 * @author v8036651
 */
public class Observer {

    private ArrayList<Monitor> observers;

    /**
     * Constructor for the observer object, this is used when it is being passed
     * an array of observers that are pre defined.
     *
     * @param observers
     * @author v8036651
     */
    public Observer(ArrayList<Monitor> observers) {
        this.observers = observers;
    }

    /**
     * constructor for an observer when not passed any observers already.
     */
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
     * @param observer observer to add
     * @author v8073331
     */
    public void addObserver(Monitor observer) {
        this.observers.add(observer);
    }

    /**
     * Updates all the monitors thate in use when called, this is done upon
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
    public void updateReceiver(Message msg, String actualSender) {
        observers.forEach((Monitor o) -> {
            try {
                o.ReceivedMessage(msg, actualSender);
            } catch (Exception e) {
            }
        });
    }

}
