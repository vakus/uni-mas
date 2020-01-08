/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.metaagent;

import ica.metaagent.SocketAgent;
import ica.metaagent.MetaAgent;
import ica.messages.Message;
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
public class SocketAgentTest {
    
    public SocketAgentTest() {
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
     * Test of messageHandler method, of class SocketAgent.
     */
    @Test
    public void testMessageHandler() {
        System.out.println("Testing the message handler method");
        MetaAgent agent = null;
        Message msg = null;
        SocketAgent instance = null;
        instance.messageHandler(agent, msg);
    }
    
}
