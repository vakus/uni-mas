/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icametaagent;

import icamessages.Message;
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
public class MetaAgentTest {
    
    public MetaAgentTest() {
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
     * Test of getName method, of class MetaAgent, with no values.
     */
    @Test
    public void testGetNameWithNoValues() {
        System.out.println("Testing the get name method with no values");
        MetaAgent instance = new MetaAgentImpl();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class MetaAgent, with values.
     */
    @Test
    public void testGetNameWithValues() {
        System.out.println("Testing the get name method");
        MetaAgent instance = new MetaAgentImpl2();
        String expResult = "Five";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
    
    
    public class MetaAgentImpl extends MetaAgent {

        public MetaAgentImpl() {
            super("");
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
    
    public class MetaAgentImpl2 extends MetaAgent {

        public MetaAgentImpl2() {
            super("Five");
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
