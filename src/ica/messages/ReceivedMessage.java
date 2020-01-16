/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ica.messages;

import ica.metaagent.MetaAgent;

/**
 *
 * @author vakus
 */
public class ReceivedMessage {
    private final MetaAgent source;
    private final Message message;

    public ReceivedMessage(MetaAgent source, Message message) {
        this.source = source;
        this.message = message;
    }

    public MetaAgent getSource() {
        return source;
    }

    public Message getMessage() {
        return message;
    }
    
}