/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import icamessages.MessageType;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author v8077971
 */
public class PortalTest {
    
    public PortalTest() {
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
     * Test of getMetaAgent method, of class Portal.
     */
    @Test
    public void testGetMetaAgent() {
        System.out.println("Testing the get meta agent method.");
        
        String n = "A1";
        Portal instance = new Portal("Test");
        
        instance.addAgent("A1", new User(n,instance));
        User expResult = new User(n, instance);
        String sExpResult = expResult.getName();
        
        User result = new User(instance.getMetaAgent(n).getName(), instance);
        String sResult = result.getName();
        
        assertEquals(sExpResult, sResult);
    }

    
    /**
     * Test of removeAgent method, of class Portal.
     */
    @Test
    public void testRemoveAgent() {
        System.out.println("Testing the remove agent method.");
        Portal instance = new Portal("Test");
        Portal expResult = instance;
        
        instance.addAgent("A1", new User("Barry", instance));
        instance.removeAgent("A1");
        
        String sExpResult = expResult.toString();
        String sResult = instance.toString();
        
        assertEquals(sExpResult, sResult);
    }
    
     /**
     * Test of sendMessage method, of class Portal.
     */
    @Test
    public void testSendMessageAsUserMessage() {
        System.out.println("Testing the send message method");
        Portal instance = new Portal("Test Portal");
        User agent = new User("A1", instance);
        User agent2 = new User("A2", instance);
        Message message = new Message(instance.getName(), instance.getName(), MessageType.USER_MSG, "Hello");
        
        instance.sendMessage(instance, message);
        
        String expResult = "User Message " + "Hello";
        String result = "User Message " + message.getMessageDetails();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of sendMessage method, of class Portal.
     */
    @Test
    public void testSendMessageAsErrorMessage() {
        System.out.println("Testing the send message method");
        Portal instance = new Portal("Test Portal");
        User agent = new User("A1", instance);
        User agent2 = new User("A2", instance);
        Message message = new Message(instance.getName(), instance.getName(), MessageType.ERROR, "Hello");
        
        instance.sendMessage(instance, message);
        
        String expResult = "Error:" + "Hello";
        String result = "Error:" + message.getMessageDetails();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of the isNameAllowed method, of class Portal.
     */
    @Test
    public void testIsNameAllowedWithAnInvalidName(){
        System.out.println("Testing the is name allowed method");
        Portal instance = new Portal("Test Portal");
        User agent1 = new User("Agent", instance);
        User agent2 = new User("Agent", instance);
        
        boolean expResult = false;
        boolean result = instance.isNameAllowed(agent2.getName());
        
        assertTrue(result == expResult);
    }
}