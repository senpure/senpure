package com.senpure.io.server.gateway.consumer.handler;

import com.senpure.io.server.gateway.GatewayReceiveConsumerMessage;
import com.senpure.io.server.protocol.message.CSHeartMessage;
import com.senpure.io.server.protocol.message.SCHeartMessage;
import io.netty.channel.Channel;

public class DefaultCSHeartMessageHandler extends AbstractGatewayConsumerMessageHandler implements CSHeartMessageHandler {

    @Override
    public void execute(Channel channel, GatewayReceiveConsumerMessage frame) {
        SCHeartMessage heartMessage = new SCHeartMessage();
        messageExecutor.responseMessage2Consumer(frame.requestId(), frame.token(), heartMessage);
    }

    @Override
    public int messageId() {
        return CSHeartMessage.MESSAGE_ID;
    }
}
