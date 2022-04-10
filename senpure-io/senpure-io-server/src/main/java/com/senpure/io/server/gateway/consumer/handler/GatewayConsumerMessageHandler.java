package com.senpure.io.server.gateway.consumer.handler;

import com.senpure.io.server.gateway.GatewayReceiveConsumerMessage;
import io.netty.channel.Channel;

public interface GatewayConsumerMessageHandler {

    void execute(Channel channel, GatewayReceiveConsumerMessage frame);

    int messageId();

    default boolean stopProvider() {
        return true;
    }
}
