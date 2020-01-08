/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.metaagent.MetaAgent;
import ica.messages.Message;
import ica.messages.MessageType;
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
        MetaAgent instance = new MetaAgentImpl("");
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class MetaAgent, with values.
     */
    @Test
    public void testGetNameWithValues() {
        System.out.println("Testing the get name method with values");
        MetaAgent instance = new MetaAgentImpl("Five");
        String expResult = "Five";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    
    public class MetaAgentImpl extends MetaAgent {

        public MetaAgentImpl(String name) {
            super(name);
        }

        @Override
        public void messageHandler(MetaAgent agent, Message msg) {
        }
    }
    
}
