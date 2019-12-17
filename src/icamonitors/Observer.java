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
     * 
     * @param observers 
     * @author v8036651
     */
    public Observer(ArrayList observers) 
    {
        this.observers = observers;
    }

    /**
     * 
     * @return 
     * @author v8036651
     */
    public ArrayList getObservers() 
    {
        return observers;
    }

    /**
     * 
     * @param observers 
     * @author v8036651
     */
    public void setObservers(ArrayList observers) 
    {
        this.observers = observers;
    }
    
    /**
     * 
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
     * 
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
