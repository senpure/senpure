package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import io.netty.channel.Channel;

public interface GatewayProviderMessageHandler {


    void execute(Channel channel, GatewayReceiveProviderMessage frame);

    int messageId();

    default boolean stopConsumer() {
        return true;
    }

}
