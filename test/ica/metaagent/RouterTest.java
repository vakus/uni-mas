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
import org.junit.Ignore;

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
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToAddPortal() throws IOException {
        System.out.println("Testing the message handler to add a portal");
        Router instance = new Router("R1");
        Portal p = new Portal("P1");
        
        Message message = new Message(p.getName(), "global", MessageType.ADD_PORTAL, "");
        instance.messageHandler(p, message);
        
        String expResult = "P1";
        String result = instance.getMetaAgent(p.getName()).getName();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of messageHandler method, of class Router, to remove a portal
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToRemovePortal() throws IOException {
        System.out.println("Testing the message handler to remove a portal using socket agents");
        Router instance = new Router("R1");
        Portal p = new Portal("P1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(p, s);
        
        p.addAgent("SA", sa);
        
        Message message = new Message(sa.getName(), instance.getName(), MessageType.ADD_PORTAL, "");
        instance.messageHandler(sa, message);
        
        
        message = new Message(sa.getName(), instance.getName(), MessageType.REMOVE_PORTAL, "");
        instance.messageHandler(sa, message);
        
        String expResult = "{}";
        String result = instance.getRoutingTable().toString();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of messageHandler method, of class Router, to remove a portal
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToRemovePortalWithoutSocketAgents() throws IOException {
        System.out.println("Testing the message handler method without using socket agents");
        Router instance = new Router("R1");
        Portal p = new Portal("P1");
        
        Message message = new Message(p.getName(), instance.getName(), MessageType.ADD_PORTAL, "");
        instance.messageHandler(p, message);
        
        
        message = new Message(p.getName(), instance.getName(), MessageType.REMOVE_PORTAL, "");
        instance.messageHandler(p, message);
        
        String expResult = "{P1="+p.toString()+"}";
        String result = instance.getRoutingTable().toString();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of messageHandler method, of class Router, to remove a portal
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToRemovePortalWithInvalidOrigin() throws IOException {
        System.out.println("Testing the message handler method with invalid origin");
        Router instance = new Router("R1");
        Portal p = new Portal("P1");
        
        Message message = new Message(p.getName(), instance.getName(), MessageType.REMOVE_PORTAL, "");
        instance.messageHandler(p, message);
        
        String expResult = "{}";
        String result = instance.getRoutingTable().toString();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
    }
    
    
    /**
     * Test of messageHandler method, of class Router, to add a router
     * This cannot be done at the moment as it requires two computers.
     * @throws java.io.IOException
     */
    @Ignore
    @Test
    public void testMessageHandlerToAddRouter() throws IOException {
        System.out.println("Testing the message handler method to add a router");
        Router instance = new Router("R1");
        Portal p = new Portal("P1");
        
        Message message = new Message(p.getName(), instance.getName(), MessageType.ADD_ROUTER, "");
        instance.messageHandler(p, message);
        
        String expResult = "{}";
        String result = instance.getRoutingTable().toString();
        
        instance.shutdown();
        
        assertTrue(false);
    }
    
    /**
     * Test of messageHandler method, of class Router, to request router address
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToRequestRouterAddress() throws IOException {
        System.out.println("Testing the message handler method to request router address");
        Router instance = new Router("R1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(instance, s);
        
        Message message = new Message(sa.getName(), instance.getName(), MessageType.REQUEST_ROUTER_ADDRESSES, "");
        instance.messageHandler(sa, message);
        
        String expResult = "{}";
        String result = instance.getRoutingTable().toString();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of messageHandler method, of class Router, to request router address with socket agents on router
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToRequestRouterAddressWithSocketAgents() throws IOException {
        System.out.println("Testing the message handler method to request router address with socket agents on router");
        Router instance = new Router("R1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(instance, s);
        
        instance.addAgent("SA", sa);
        
        Message message = new Message(sa.getName(), instance.getName(), MessageType.REQUEST_ROUTER_ADDRESSES, "");
        instance.messageHandler(sa, message);
        
        String expResult = "{SA="+sa.toString()+"}";
        String result = instance.getRoutingTable().toString();
        
        instance.shutdown();
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of messageHandler method, of class Router, to remove a portal
     * @throws java.io.IOException
     */
    @Test
    public void testMessageHandlerToDefaultCase() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method to request router address");
        Router instance = new Router("R1");
        
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        Message message = new Message(instance.getName(), "global", MessageType.ERROR, "Test");
        instance.messageHandler(instance, message);
        
        String expResult = "Invalid origin for message: R1/global/ERROR/Test"+System.getProperty("line.separator");
        instance.shutdown();
        
        assertEquals(expResult, outContent.toString());
        
    }
    
    
}
