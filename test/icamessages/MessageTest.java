/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icamessages;

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
public class MessageTest {
    
    public MessageTest() {
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
     * Test of getSender method, of class Message, where there are no values.
     */
    @Test
    public void testGetSenderWithNoValues() {
        System.out.println("Testing the get sender method with no values.");
        Message instance = new Message();
        String expResult = "error";
        String result = instance.getSender();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSender method, of class Message, where there are values.
     */
    @Test
    public void testGetSenderWithValues() {
        System.out.println("Testing the get sender method with values.");
        Message instance = new Message("Abe", "Barry", MessageType.USER_MSG, "Testing");
        String expResult = "Abe";
        String result = instance.getSender();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRecipient method, of class Message, where there are no values.
     */
    @Test
    public void testGetRecipientWithNoValues() {
        System.out.println("Testing the get recipient method with no values.");
        Message instance = new Message();
        String expResult = "error";
        String result = instance.getRecipient();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getRecipient method, of class Message, where there are values.
     */
    @Test
    public void testGetRecipientWithValues() {
        System.out.println("Testing the get recipient method with values.");
        Message instance = new Message("Abe", "Barry", MessageType.USER_MSG, "Testing");
        String expResult = "Barry";
        String result = instance.getRecipient();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageType method, of class Message, where there are no values.
     */
    @Test
    public void testGetMessageTypeWithNoValues() {
        System.out.println("Testing the get message type method with no values.");
        Message instance = new Message();
        MessageType expResult = MessageType.ERROR;
        MessageType result = instance.getMessageType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageType method, of class Message, where it is a user message.
     */
    @Test
    public void testGetMessageTypeWithAUserMessage() {
        System.out.println("Testing the get message type method with a user message.");
        Message instance = new Message("Abe", "Barry", MessageType.USER_MSG, "Testing");
        MessageType expResult = MessageType.USER_MSG;
        MessageType result = instance.getMessageType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageDetails method, of class Message, where there are no values.
     */
    @Test
    public void testGetMessageDetailsWithNoValues() {
        System.out.println("Testing the get message details method with no values.");
        Message instance = new Message();
        String expResult = "error";
        String result = instance.getMessageDetails();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMessageDetails method, of class Message, where there are values.
     */
    @Test
    public void testGetMessageDetailsWithValues() {
        System.out.println("Testing the get message details method with values.");
        Message instance = new Message("Abe", "Barry", MessageType.USER_MSG, "Testing");
        String expResult = "Testing";
        String result = instance.getMessageDetails();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class Message, where there are no values.
     */
    @Test
    public void testToStringWithNoValues() {
        System.out.println("Testing the to string method with no values.");
        Message instance = new Message();
        String expResult = "error/error/ERROR/error";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Message, where there are values.
     */
    @Test
    public void testToStringWithValues() {
        System.out.println("Testing the to string method with values.");
        Message instance = new Message("Abe", "Barry", MessageType.USER_MSG, "Testing");
        String expResult = "Abe/Barry/USER_MSG/Testing";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of parseMessage method, of class Message, where there are no values.
     */
    @Test
    public void testParseMessage() {
        System.out.println("Testing the parse message method");
        String msg = "Abe/Barry/USER_MSG/Testing";
        Message expResult = new Message("Abe", "Barry", MessageType.USER_MSG, "Testing");
        Message result = Message.parseMessage(msg);
        assertEquals(expResult.toString(), result.toString());
    }
    
}
