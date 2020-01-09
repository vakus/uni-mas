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
import ica.monitors.CMDMonitor;
import ica.monitors.Monitor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
     * Test of the addAgent method, of class Portal, where the agent that already exists.
     */
    @Test
    public void testAddAgentWithExistingAgent() {
        System.out.println("Testing the Add Agent to Portal method");
        Portal P1 = new Portal("P1");
        User U1 = new User("U1", P1);
        User U2 = new User("U1", P1);
        
        P1.addAgent(U1.getName(), U1);
        boolean expResult = false;
        boolean result = true;
        
        try{
            P1.addAgent(U2.getName(), U2);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        
        assertTrue(expResult == result);
    }
    
    /**
     * Test of the addAgent method, of class Portal, where a socket agent is added to a portal.
     * @throws IOException 
     */
    @Test
    public void testAddSocketAgent() throws IOException{
        System.out.println("Testing the Add Socket Agent to Router method");
        Portal p = new Portal("P1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(p, s);
        
        boolean expResult = true;
        boolean result = true;
        
        try{
            p.addAgent("SA 1", sa);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        
        assertTrue(expResult == result);
    }
    
    /**
     * Test of the addAgent method, of class Portal, where a agent is added to socket agent of a portal.
     * @throws IOException 
     */
    @Test
    public void testAddAgentToSocketAgent() throws IOException{
        System.out.println("Testing the Add Socket Agent to Router method");
        Portal p = new Portal("P1");
        Socket s = new Socket();
        User u = new User("SA 1", p);
        SocketAgent sa = new SocketAgent(p, s);
        boolean expResult = false;
        boolean result = true;
        
        try{
            p.addAgent("SA 1", sa);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        
        try{
            p.addAgent("SA 1", u);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        
        assertTrue(expResult == result);
    }
    
    /**
     * Test of the addAgent method, of class Portal, where two socket agents are added to a portal.
     * @throws IOException 
     */
    @Test
    public void testAddTwoSocketAgents() throws IOException{
        System.out.println("Testing the Add Socket Agent to Router method");
        Portal p = new Portal("P1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(p, s);
        SocketAgent sa2 = new SocketAgent(p, s);
        boolean expResult = false;
        boolean result = true;
        
        try{
            p.addAgent("SA 1", sa);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        
        try{
            p.addAgent("SA 2", sa2);
        }
        catch(IllegalArgumentException ex){
            result = false;
        }
        assertTrue(expResult == result);
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
    public void testMessageHandlerForUserMsgToGlobal() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method, where the recipient is 'global'");
        Portal p = new Portal("P1");
        User u = new User("U1", p );
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        p.addAgent("U1", u);
        
        Message message = new Message(u.getName(), "global", MessageType.USER_MSG, "Test");
        p.messageHandler(u, message);
        
        String expResult = "UserMessage: Test"+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
    }
    
    /**
     * Test of messageHandler method, of class Portal.
     */
    @Test
    public void testMessageHandlerForUserMsgToGlobalWithInvalidOrigin() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method, where the recipient is 'global', but with invalid origin");
        Portal p = new Portal("P1");
        User u = new User("U1", p );
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        
        Message message = new Message(u.getName(), "global", MessageType.USER_MSG, "Test");
        p.messageHandler(u, message);
        
        String expResult = "Invalid origin for message: U1/global/USER_MSG/Test"+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
    }
    
    /**
     * Test of messageHandler method, of class Portal, using the load Table message type
     */
    @Test
    public void testMessageHandlerForLoadTableWithContents() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method, where the messsage is load table");
        Portal p = new Portal("P1");
        User u = new User("U1", p );
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(p, s);
        
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        p.addAgent("U1", u);
        p.addAgent("S1", sa);
        
        Message message = new Message(u.getName(), "global", MessageType.LOAD_TABLE, "Test");
        p.messageHandler(sa, message);
        
        String expResult = "Error: Can not load routing table, as it is not empty."+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
    }
    
    /**
     * Test of messageHandler method, of class Portal, using the load Table message type
     */
    @Test
    public void testMessageHandlerForLoadTableWhenEmpty() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method, where the messsage is load table, which is empty");
        Portal p = new Portal("P1");
        User u = new User("U1", p );
        
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        
        Message message = new Message(u.getName(), "global", MessageType.LOAD_TABLE, "Test");
        p.messageHandler(u, message);
        
        String expResult = "Error: Can not load routing table, as it is not empty."+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
        System.out.println(p.getMetaAgent("U1"));
    }
    
    /**
     * Test of messageHandler method, of class Portal.
     */
    @Test
    public void testMessageHandlerForErrorMsgToGlobal() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method, where the messsage is an error");
        Portal p = new Portal("P1");
        User u = new User("U1", p );
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        p.addAgent("U1", u);
        
        Message message = new Message(u.getName(), "global", MessageType.ERROR, "Test");
        p.messageHandler(u, message);
        
        String expResult = "Error: Test"+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
    }
    
    /**
     * Test of messageHandler method, of class Portal.
     */
    @Test
    public void testMessageHandlerForErrorMsgToGlobalWithInvalidOrigin() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        
        System.out.println("Testing the message handler method, where the messsage is an error, but with invalid origin");
        Portal p = new Portal("P1");
        User u = new User("U1", p );
        System.setOut(null);
        System.setOut(new PrintStream(outContent));
        
        
        Message message = new Message(u.getName(), "global", MessageType.ERROR, "Test");
        p.messageHandler(u, message);
        
        String expResult = "Invalid origin for message: U1/global/ERROR/Test"+System.getProperty("line.separator");
        assertEquals(expResult, outContent.toString());
        System.out.println(outContent.toString());
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
    public void testIsNameAllowedWhereNameExists(){
        System.out.println("Testing the is name allowed method, where the name exists");
        Portal instance = new Portal("Test Portal");
        User agent1 = new User("Agent", instance);
        User agent2 = new User("Agent", instance);
        
        instance.addAgent("Agent", agent1);
        
        boolean expResult = false;
        boolean result = instance.isNameAllowed(agent2.getName());
        assertTrue(result == expResult);
    }

    /**
     * Test of the isNameAllowed method, of class Portal, with an invalid name
     */
    @Test
    public void testIsNameAllowedWhereNameIsInvalid(){
        System.out.println("Testing the is name allowed method, where the name is invalid");
        
        Portal instance = new Portal("Test Portal");
        User agent1 = new User("Agent", instance);
        
        instance.addAgent("Agent", agent1);
        
        boolean expResult = false;
        boolean result = instance.isNameAllowed("global");
        
        assertTrue(result == expResult);
    }
    
    /**
     * Test of isMessageOriginCorrect method, of class Portal.
     */
    @Test
    public void testIsMessageOriginCorrect() {
        System.out.println("Testing the is message origin correct method");
        Portal p = new Portal("P1");
        User u1 = new User("U1", p);
        
        p.addAgent(u1.getName(), u1);
        Message msg = new Message(u1.getName(), u1.getName(), MessageType.USER_MSG, "Test");
        boolean expResult = true;
        boolean result = p.isMessageOriginCorrect(u1, msg);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of addObserver method, of class Portal.
     */
    @Test
    public void testAddObserver() {
        System.out.println("Testing the add observer method");
        CMDMonitor m = new CMDMonitor("M1");
        Portal instance = new Portal("P1");
        instance.addObserver(m);
    }

    /**
     * Test of shutdown method, of class Portal.
     */
    @Test
    public void testShutdown() {
        System.out.println("Testing the shutdown method");
        Portal instance = new Portal("P1");
        instance.shutdown();
    }
    
    /**
     * Test of shutdown method, of class Portal, with socket agents
     */
    @Test
    public void testShutdownWithSocketAgent() {
        System.out.println("Testing the shutdown method with a socket agent on the portal");
        Portal instance = new Portal("P1");
        Socket s = new Socket();
        SocketAgent sa = new SocketAgent(instance, s);
        
        instance.addAgent("SA1",sa);
        instance.shutdown();
    }
}
