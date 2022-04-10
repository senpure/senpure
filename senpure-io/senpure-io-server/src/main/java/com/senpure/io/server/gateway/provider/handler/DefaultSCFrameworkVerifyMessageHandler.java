package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkVerifyMessage;
import io.netty.channel.Channel;

public class DefaultSCFrameworkVerifyMessageHandler extends AbstractGatewayProviderMessageHandler implements SCFrameworkVerifyMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {


        SCFrameworkVerifyMessage message = new SCFrameworkVerifyMessage();
        readMessage(message, frame);
        int requestId = frame.requestId();
        if (requestId != 0) {
            messageExecutor.receive(channel, requestId, message);
        }
    }


    @Override
    public int messageId() {
        return SCFrameworkVerifyMessage.MESSAGE_ID;
    }


}
