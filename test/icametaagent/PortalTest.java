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
        String n = "Test";
        Portal instance = new Portal("Test");
        instance.addAgent(new MetaAgentImpl(n));
        MetaAgent expResult = new MetaAgentImpl(n);
        String sExpResult = expResult.getName();
        MetaAgent result = instance.getMetaAgent(n);
        String sResult = result.getName();
        assertEquals(sExpResult, sResult);
    }

    
    /**
     * Test of removeAgent method, of class Portal.
     */
    @Test
    public void testRemoveAgent() {
        System.out.println("Testing the remove agent method.");
        String name = "Barry";
        Portal instance = new Portal("Test");
        Portal expResult = instance;
        
        instance.addAgent(new MetaAgentImpl("Barry"));
        instance.removeAgent(name);
        
        String sExpResult = expResult.toString();
        String sResult = instance.toString();
        
        assertEquals(sExpResult, sResult);
    }
    
     /**
     * Test of sendMessage method, of class Portal.
     */
    @Test
    public void testSendMessage() {
        System.out.println("Testing the send message method");
        MetaAgent agent = new MetaAgentImpl("Agent1");
        MetaAgent agent2 = new MetaAgentImpl("Agent1");
        Message message = new Message(agent.getName(), agent2.getName(), MessageType.USER_MSG, "Hello");
        Portal instance = new Portal("Test Portal");
        instance.addAgent(agent);
        instance.addAgent(agent2);
        instance.sendMessage(agent, message);
    }
   
    /**
     * Test of run method, of class Portal.
     */
    
    /*
    @Test
    public void testRun() {
        System.out.println("Testing the run method");
        Portal instance = new Portal("Test");
        instance.run();
    }
    */
    
    public class MetaAgentImpl extends MetaAgent {

        public MetaAgentImpl(String name) {
            super(name);
        }

        public void sendMessage(Message m) {
        }

        public Message receiveMessage() {
            return null;
        }

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void sendMessage(MetaAgent agent, Message msg) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }


}
