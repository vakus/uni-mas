package ica.messages;

/**
 * This is the Message class that is a key component of the system that is
 * passed between methods and other classes as it stores different data about
 * the user, the agent and the target of the message.
 *
 * @author v8243060
 */
public class Message {

    /**
     * String name of the sender of the message.
     */
    private final String sender;

    /**
     * String name of the recipient of the message If "global" then the message
     * is a broadcast.
     */
    private final String recipient;

    /**
     * Enum type of the message.
     */
    private final MessageType type;

    /**
     * Contains the message itself.
     */
    private final String messageDetails;

    /**
     * Constructor for message class
     *
     * @param sender String name of the sender
     * @param recipeint String name of the recipient
     * @param type {@link MessageType} representing type of the message
     * @param details String message details
     * @author v8243060
     */
    public Message(String sender, String recipeint, MessageType type, String details) {
        this.sender = sender;
        this.recipient = recipeint;
        this.type = type;
        this.messageDetails = details;
    }

    /**
     * Retrieve sender of the message
     *
     * @return String name of the sender
     * @author v8243060
     */
    public String getSender() {
        return sender;
    }

    /**
     * Retrieve recipient of the message
     *
     * @return String name of the recipient
     * @author v8243060
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Retrieve message type
     *
     * @return {@link MessageType} Enum representing the type of the message
     * @author v8243060
     */
    public MessageType getMessageType() {
        return type;
    }

    /**
     * Retrieve message details
     *
     * @return String message details
     * @author v8243060
     */
    public String getMessageDetails() {
        return messageDetails;
    }

    /**
     * toString() overridden method which allows the message to be passed
     * between the portals
     *
     * @return String of all variables
     * @author v8243060
     */
    @Override
    public String toString() {
        return sender + "/" + recipient + "/" + type.toString() + "/" + messageDetails;
    }

    /**
     * Converts message back from the toString() method
     *
     * @param msg The string that should be parsed to Message
     * @return Message object created from the string
     * @author v8243060
     */
    public static Message parseMessage(String msg) {
        String[] msgArr = msg.split("/", 4);
        String sender = msgArr[0];
        String recipient = msgArr[1];
        MessageType type = MessageType.valueOf(msgArr[2].toUpperCase());
        String details = msgArr[3];

        return new Message(sender, recipient, type, details);
    }
}
