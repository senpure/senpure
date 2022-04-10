package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCSuccessMessage;
import io.netty.channel.Channel;

public class DefaultSCSuccessMessageHandler extends AbstractGatewayProviderMessageHandler implements SCSuccessMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        if (frame.getToken() == 0 && frame.getUserIds().length == 0) {
            SCSuccessMessage message = new SCSuccessMessage();
            readMessage(message, frame);
            messageExecutor.receive(channel, frame.requestId(), message);
        } else {
            messageExecutor.sendMessage2Consumer(frame);
        }
    }

    @Override
    public int messageId() {
        return SCSuccessMessage.MESSAGE_ID;
    }
}
