package com.senpure.io.server.gateway;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.AbstractMessageFrame;
import com.senpure.io.server.support.MessageIdReader;

public class GatewayLocalSendConsumerMessage  extends AbstractMessageFrame implements GatewaySendConsumerMessage{


    public GatewayLocalSendConsumerMessage(Message message) {
        super(message);
    }

    @Override
    public String toString() {
        return "GatewayLocalSendConsumerMessage{" +
                "messageId=" + MessageIdReader.read(messageId) +
                ",requestId =" + requestId +
                '}';
    }
}
