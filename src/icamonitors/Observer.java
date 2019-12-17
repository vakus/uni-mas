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
public class Observer 
{
    private ArrayList<Monitor> observers;

    /**
     * Constructor for the observer object.
     * @param observers 
     * @author v8036651
     */
    public Observer(ArrayList observers) 
    {
        this.observers = observers;
    }

    /**
     * Getter for the array list of observers.
     * @return 
     * @author v8036651
     */
    public ArrayList getObservers() 
    {
        return observers;
    }

    /**
     * Setter for the array list of observers.
     * @param observers 
     * @author v8036651
     */
    public void setObservers(ArrayList observers) 
    {
        this.observers = observers;
    }
    
    /**
     * Updates all the monitors that are in use when called, this is done upon sending a message.
     * @param msg 
     * @author v8036651
     * @author v8073331
     */
    public void updateSender(Message msg)
    {
        observers.forEach((Monitor o) -> {
            try{
                o.SentMessage(msg);
            }catch(Exception e){
            }
        });
    }
    
    /**
     * Updates all the monitors that are in use when called, this is done upon receiving a message.
     * @param msg 
     * @author v8036651
     * @author v8073331
     */
    public void updateReceiver(Message msg)
    {
        observers.forEach((Monitor o) -> {
            try{
                o.ReceivedMessage(msg);
            }catch(Exception e){
            }
        });
    }
    
}
