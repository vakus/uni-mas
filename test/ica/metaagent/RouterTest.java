/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.messages.Message;
import ica.messages.MessageType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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
public class RouterTest {
    
    public RouterTest() {
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
     * Test of addAgent method, of class Router, with a valid agent
     */
    @Test
    public void testAddAgent() throws IOException {
        System.out.println("Testing the  add agent method with a valid agent");
        Router instance = new Router("R1");
        User u = new User("U1", instance);
        
        instance.addAgent(u.getName(), u);
        String expResult = "U1";
        String result = instance.getMetaAgent(u.getName()).getName();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of the addAgent method, of class Router, where the agent that already exists.
     * @throws java.io.IOException
     */
    @Test
    public void testAddAgentWithExistingAgent() throws IOException {
        System.out.println("Testing the Add Agent to Router method, with the existing agent.");
        
        Router instance = new Router("R1");
        User u = new User("u", instance);
        
        instance.addAgent(u.getName(), u);
        boolean expResult = false;
        boolean result = true;
        
        try{
           instance.addAgent(u.getName(), u);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        
        instance.shutdown();
        
        assertTrue(expResult == result);
        
    }
    
    
    /**
     * Test of addAgent method, of class Router, with a valid agent
     */
    @Test
    public void testAddSocketAgent() throws IOException {
        System.out.println("Testing the add agent method with a valid agent");
        Router instance = new Router("R1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(instance, s);
        
        boolean expResult = true;
        boolean result = true;
        
        try{
            instance.addAgent("SA 1", sa);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        instance.shutdown();
        
        assertTrue(expResult == result);
        
    }
    
    /**
     * Test of addAgent method, of class Router, with a valid agent
     */
    @Test
    public void testAddSocketAgentThatAlreadyExists() throws IOException {
        System.out.println("Testing the add agent method with a socket agent that already exists");
        Router instance = new Router("R1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(instance, s);
        
        boolean expResult = false;
        boolean result = true;
        
            instance.addAgent("SA 1", sa);
            
        try{
            instance.addAgent("SA 1", sa);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        instance.shutdown();
        
        assertTrue(expResult == result);
        
    }
    
    /**
     * Test of messageHandler method, of class Router.
     */
    @Test
    public void testMessageHandler() {
        System.out.println("messageHandler");
        MetaAgent agent = null;
        Message message = null;
        Router instance = null;
        instance.messageHandler(agent, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
