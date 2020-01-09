package ica.messages;

/**
 * This enum represents a type of a message.
 *
 * @author v8243060
 */
public enum MessageType {

    /**
     * To help show that it is a message sent by a user rather than a system
     * message.
     */
    USER_MSG,
    /**
     * Message type to be used when adding a new agent to a portal.
     */
    ADD_METAAGENT,
    /**
     * Message type to be used when removing a new agent to a portal.
     */
    REMOVE_METAAGENT,
    /**
     * Only to be used when an error is encountered.
     */
    ERROR,
    /**
     * Helps to show that a message is for adding a portal and not a user agent.
     */
    ADD_PORTAL,
    /**
     * Helps to show that a message is for removing a portal and not a user
     * agent.
     */
    REMOVE_PORTAL,
    /**
     * Used during adding portals and routers to match the connections tables.
     */
    LOAD_TABLE,
    /**
     * Used for adding the router address to the address ArrayList when
     * connecting to another router.
     */
    ADD_ROUTER,
    /**
     * Used to send the addresses of every router to the new router.
     */
    LOAD_ADDRESSES,
    /**
     * Used to request the router addresses list from a router.
     */
    REQUEST_ROUTER_ADDRESSES
}
