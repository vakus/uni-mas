/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.metaagent.MetaAgent;
import ica.metaagent.User;
import ica.messages.Message;
import ica.messages.MessageType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author v8077971
 */
public class UserTest {
    
    public UserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test of messageHandler method, of class User, where the message is to the user
     */
    @Test
    public void testMessageHandlerWithMessageToSelf() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler where the message is to the user");
        Portal p = new Portal("Portal One");
        User instance = new User("User One", p);
        
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        Message msg = new Message(instance.getName(), instance.getName(),MessageType.USER_MSG, "Test");
        instance.messageHandler(instance, msg);
        
        String expResult = "Message (USER_MSG): Test"+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
    }

    /**
     * Test of messageHandler method, of class User, where the message is to another user
     */
    @Test
    public void testMessageHandlerWithMessageToAnotherUser() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler where the message is to another user");
        Portal p = new Portal("Portal One");
        User instance = new User("User One", p);
        User otherAgent = new User("User Two", p);
        
        p.addAgent("User One", instance);
        p.addAgent("User Two", otherAgent);
        
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        Message msg = new Message(instance.getName(), instance.getName(),MessageType.USER_MSG, "Test");
        otherAgent.messageHandler(instance, msg);
        
        
        String expResult = "Message (ERROR): Message recieved by wrong agent"+System.getProperty("line.separator");
        
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
    }
    
    /**
     * Test of sendMessage method, of class User, with a valid recipient
     */
    @Test
    public void testSendMessageWithValidRecipient() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing send message method where the recipient is valid");
        Portal p = new Portal("P1");
        User instance = new User("User One", p);
        String recipient = "User One";
        String details = "Test";
        User instance2 = new User("User Two", p);
        
        String expResult = "Message (USER_MSG): Test"+System.getProperty("line.separator");
        
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        p.addAgent(instance.getName(), instance);
        instance.sendMessage(recipient, details);
        
        assertEquals(expResult, outContent.toString());
    }
    
    /**
     * Test of sendMessage method, of class User, with global as recipient
     */
    @Test
    public void testSendMessageWithGlobalRecipient() {
        System.out.println("Testing send message method where the recipient includes global");
        
        Portal p = new Portal("P1");
        User instance = new User("User One", p);
        String recipient = "global";
        String details = "Test";
        boolean result = true;
        p.addAgent("User One", instance);
        boolean expResult = false;
        try {
            instance.sendMessage(recipient, details);
        }
        catch(IllegalArgumentException ex)
        {
            result = false;
        }
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of sendMessage method, of class User, with a '/' in recipient name
     */
    @Test
    public void testSendMessageWithInvalidRecipient() {
        System.out.println("Testing send message method where the recipient includes '/'");
        
        Portal p = new Portal("P1");
        User instance = new User("User One", p);
        String recipient = "/";
        String details = "Test";
        boolean result = true;
        p.addAgent("User One", instance);
        boolean expResult = false;
        try {
            instance.sendMessage(recipient, details);
        }
        catch(IllegalArgumentException ex)
        {
            result = false;
        }
        
        assertEquals(expResult, result);
    }
}
