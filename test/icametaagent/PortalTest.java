/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
import java.io.IOException;
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
     * Test of the addAgent method, of class Portal, where a socket agent is added to a portal.
     * @throws IOException 
     */
    @Test
    public void testAddSocketAgentToRouter() throws IOException{
        Router instance = new Router("Test");
        Socket s = new Socket();
        SocketAgent SA1 = new SocketAgent("SA1", instance, s);
        
        instance.addAgent("SA1", SA1);
        
        String expResult = "SA1";
        String result = instance.getMetaAgent(SA1.getName()).getName();
        assertEquals(expResult,result);
    }
    
    /**
     * Test of the addAgent method, of class Portal, where a socket agent is added to a portal.
     * @throws IOException 
     */
    @Test
    public void testAddSocketAgentToPortal() throws IOException{
        Portal P1 = new Portal("P1");
        Portal P2 = new Portal("P2");
        Socket S1 = new Socket();
        
        SocketAgent SA1 = new SocketAgent("SA1", P2, S1);
        SocketAgent SA2 = new SocketAgent("SA2", P2, S1);
        
        P1.addAgent("SA1", SA1);
        
        String expResult = "SA1";
        String result = P1.getMetaAgent(SA1.getName()).getName();
        assertEquals(expResult,result);
    }
    
    /**
     * Test of the addAgent method, of class Portal, where a socket agent is added to a portal.
     * @throws IOException 
     */
    @Test
    public void testAddSocketAgentToPortalWithSocketAgents() throws IOException{
        Portal P1 = new Portal("P1");
        Portal P2 = new Portal("P2");
        Socket S1 = new Socket();
        
        SocketAgent SA1 = new SocketAgent("SA1", P2, S1);
        SocketAgent SA2 = new SocketAgent("SA2", P2, S1);
        SocketAgent SA3 = new SocketAgent("SA3", P1, S1);
        SocketAgent SA4 = new SocketAgent("SA4", P1, S1);
        SocketAgent SA5 = new SocketAgent("SA5", P1, S1);
        
        P1.addAgent("SA1", SA1);
        
        String expResult = "SA1";
        String result = P1.getMetaAgent(SA1.getName()).getName();
        assertEquals(expResult,result);
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
     * Test of messageHandler method, of class Portal.
     */
    @Ignore
    @Test
    public void testMessageHandler() {
        System.out.println("messageHandler");
        MetaAgent agent = null;
        Message message = null;
        Portal instance = null;
        instance.messageHandler(agent, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of the isNameAllowed method, of class Portal, with an invalid name
     */
    @Test
    public void testIsNameAllowedWithAValidName(){
        System.out.println("Testing the is name allowed method");
        Portal instance = new Portal("Test Portal");
        User agent1 = new User("Agent", instance);
        User agent2 = new User("Agent2", instance);
        
        instance.addAgent("Agent", agent1);
        
        boolean expResult = true;
        boolean result = instance.isNameAllowed(agent2.getName());
        assertTrue(result == expResult);
    }
    
    /**
     * Test of the isNameAllowed method, of class Portal, with an invalid name
     */
    @Test
    public void testIsNameAllowedWithAnInvalidName(){
        System.out.println("Testing the is name allowed method");
        Portal instance = new Portal("Test Portal");
        User agent1 = new User("Agent", instance);
        User agent2 = new User("Agent", instance);
        
        instance.addAgent("Agent", agent1);
        
        boolean expResult = false;
        boolean result = instance.isNameAllowed(agent2.getName());
        assertTrue(result == expResult);
    }
    
}
