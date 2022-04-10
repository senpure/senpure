package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCBreakUserGatewayMessage;
import io.netty.channel.Channel;

public class DefaultSCBreakUserGatewayMessageHandler extends AbstractGatewayProviderMessageHandler  implements  SCBreakUserGatewayMessageHandler{
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        if (frame.requestId() != 0) {
            SCBreakUserGatewayMessage message = new SCBreakUserGatewayMessage();
            readMessage(message, frame);
            messageExecutor.receive(channel, frame.requestId(), message);
        }

    }

    @Override
    public int messageId() {
        return SCBreakUserGatewayMessage.MESSAGE_ID;
    }
}
