package com.senpure.io.server.gateway.consumer.handler;

import com.senpure.io.server.gateway.GatewayReceiveConsumerMessage;
import io.netty.channel.Channel;

public class DefaultCSLoginMessageHandler extends AbstractGatewayConsumerMessageHandler implements CSLoginMessageHandler {


    @Override
    public void execute(Channel channel, GatewayReceiveConsumerMessage frame) {
        logger.debug("{} 准备登录 ", frame.token());
        messageExecutor.prepLoginChannels.put(frame.token(), channel);
    }

    @Override
    public int messageId() {
        return messageExecutor.getCsLoginMessageId();
    }

    @Override
    public boolean stopProvider() {
        return false;
    }
}
