/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.metaagent.MetaAgent;
import ica.metaagent.SocketAgent;
import ica.metaagent.User;
import ica.metaagent.Portal;
import ica.metaagent.Router;
import ica.messages.Message;
import ica.messages.MessageType;
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;
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
        System.out.println("Testing the Add Socket Agent to Router method");
        Router R1 = new Router("Test");
        Socket S1 = new Socket();
        SocketAgent SA1 = new SocketAgent(R1, S1);
        
        R1.addAgent(SA1.getName(), SA1);
        
        String expResult = "SA 1";
        String result = R1.getMetaAgent("SA 1").getName();
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

    /**
     * Test of isMessageOriginCorrect method, of class Portal.
     */
    @Test
    public void testIsMessageOriginCorrect() {
        System.out.println("Test the is message origin correct method");
        Portal instance = new Portal("Fuck you, piece of shit");
        User agent = new User("Fuck my life", instance);
        User agent2 = new User("Fuck everything", instance);
        
        instance.addAgent(agent.getName(), agent);
        Message msg = new Message(agent.getName(), agent.getName(), MessageType.USER_MSG, "FUCK YOU PIECE OF SHIT");
        boolean expResult = true;
        boolean result = instance.isMessageOriginCorrect(agent, msg);
        assertEquals(expResult, result);
    }
    
}
