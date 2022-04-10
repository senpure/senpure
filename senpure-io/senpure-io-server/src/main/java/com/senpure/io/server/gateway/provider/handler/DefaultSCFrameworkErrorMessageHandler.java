package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import io.netty.channel.Channel;

public class DefaultSCFrameworkErrorMessageHandler extends AbstractGatewayProviderMessageHandler implements SCFrameworkErrorMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        if (frame.getToken() == 0 && frame.getUserIds().length == 0) {
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            readMessage(errorMessage, frame);
            messageExecutor.receive(channel, frame.requestId(), errorMessage);
        } else {
            messageExecutor.sendMessage2Consumer(frame);
        }
    }

    @Override
    public int messageId() {
        return SCFrameworkErrorMessage.MESSAGE_ID;
    }
}
