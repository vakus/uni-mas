
import icamessages.Message;
import icamessages.MessageType;
import icametaagent.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author v8073331
 */
public class Main {
    public static void main(String[] args) throws IOException {
        
        Router r1;
        
        Portal p1;
        
        Scanner keyb = new Scanner(System.in);
        System.out.println("Do you want to create router? Y/N");
        if(keyb.nextLine().trim().equalsIgnoreCase("Y")){
            r1 = new Router("Router");
            
            
            p1 = new Portal("Portal-1");
            
            r1.addAgent("Portal-1", p1);
            
            r1.addAgent("a1", p1);
            r1.addAgent("a2", p1);
            
            User a1 = new User("a1", p1);
            User a2 = new User("a2", p1);
            
            p1.addAgent("a1", a1);
            p1.messageHandler(a1, new Message(a1.getName(), "Global", MessageType.ADD_METAAGENT, ""));
            p1.addAgent("a2", a2);
            p1.messageHandler(a1, new Message(a2.getName(), "Global", MessageType.ADD_METAAGENT, ""));
            
            Thread t = new Thread(r1);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            //we must make connection to the router
            
            p1 = new Portal("Portal-2");
            
            System.out.println("Enter the router IP: ");
            String ip = keyb.nextLine().trim();
            
            Socket s = new Socket(ip, 42069);
            SocketAgent sa = new SocketAgent("", p1, s);
            
            Thread t = new Thread(sa);
            t.start();
            sa.messageHandler(p1, new Message(p1.getName(), "GLOBAL", MessageType.ADD_PORTAL, ""));
            
            System.out.println("Press enter to continue");
            keyb.nextLine();
            
            User a3 = new User("a3", p1);
            User a4 = new User("a4", p1);
            
            p1.addAgent("a3", a3);
            p1.messageHandler(a3, new Message(a3.getName(), "Global", MessageType.ADD_METAAGENT, ""));
            p1.addAgent("a4", a4);
            p1.messageHandler(a4, new Message(a4.getName(), "Global", MessageType.ADD_METAAGENT, ""));
            
            
            p1.messageHandler(a4, new Message(a4.getName(), "a1", MessageType.USER_MSG, "HELLO THERE"));
            
        }
        
        
        
    }
}
