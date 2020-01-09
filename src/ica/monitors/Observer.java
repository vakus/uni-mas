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
     * @param observers list of already existing observers to be used.
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
     * @return list of observers which monitoring the node.
     * @author v8036651
     */
    public ArrayList<Monitor> getObservers() {
        return observers;
    }

    /**
     * Setter for the array list of observers.
     *
     * @param observers list of observers to be monitoring the node.
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
     * Updates all the monitors that are in use when called, this is done upon
     * sending a message.
     *
     * @param msg message which is being send
     * @param actualSender the name of the node which the message is being sent
     * to
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
     * @param msg the message which is being send
     * @param actualSender the name of the node which the message came from
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
