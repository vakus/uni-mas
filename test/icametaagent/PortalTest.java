/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
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
     * Test of getMetaAget method, of class Portal.
     */
    @Test
    public void testGetMetaAgent() {
        System.out.println("Testing the get meta agent method, with no values.");
        String n = "Test";
        Portal instance = new Portal("Test");
        instance.addAgent(new MetaAgentImpl(n));
        MetaAgent expResult = new MetaAgentImpl(n);
        MetaAgent result = instance.getMetaAgent(n);
        assertEquals(expResult, result);
    }

    /**
     * Test of addAgent method, of class Portal.
     */
    @Test
    public void testAddAgent() {
        System.out.println("addAgent");
        MetaAgent meta = null;
        Portal instance = null;
        instance.addAgent(meta);
    }

    /**
     * Test of removeAgent method, of class Portal.
     */
    @Test
    public void testRemoveAgent() {
        System.out.println("removeAgent");
        String name = "";
        Portal instance = null;
        instance.removeAgent(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendMessage method, of class Portal.
     */
    @Test
    public void testSendMessage() {
        System.out.println("sendMessage");
        Message m = null;
        Portal instance = null;
        instance.sendMessage(m);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of receiveMessage method, of class Portal.
     */
    @Test
    public void testReceiveMessage() {
        System.out.println("receiveMessage");
        Portal instance = null;
        Message expResult = null;
        Message result = instance.receiveMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class Portal.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Portal instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    
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
        
    }
    
}
