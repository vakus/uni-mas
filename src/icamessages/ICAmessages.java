/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamessages;

/**
 *
 * @author v8243060
 */
public class ICAmessages {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Message m = new Message ("a", "b", MessageType.USER_MSG, "hello/lol");
        String ms = m.toString();
        
        Message m2 = Message.parseMessage(ms);
        System.out.println(m2.getSender());
        System.out.println(m2.getRecipient());
        System.out.println(m2.getMessageType().toString());
        System.out.println(m2.getMessageDetails());
        
    }
    
}
