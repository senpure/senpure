package com.senpure.io.server.consumer;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.AbstractMessageFrame;

/**
 * ConsumerMessage
 *
 * @author senpure
 * @time 2019-06-27 17:43:29
 */
public class ConsumerMessage extends AbstractMessageFrame {


    public ConsumerMessage(Message message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ConsumerMessage{" +
                "requestId=" + requestId +
                ", message=" + message +
                '}';
    }
}
