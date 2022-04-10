package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCIdNameMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;

public class DefaultSCIdNameMessageHandler extends AbstractGatewayProviderMessageHandler  implements  SCIdNameMessageHandler{
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        SCIdNameMessage message = new SCIdNameMessage();
        messageExecutor.readMessage(message, frame);
        MessageIdReader.relation(message.getIdNames());
    }

    @Override
    public int messageId() {
        return SCIdNameMessage.MESSAGE_ID;
    }
}
