package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.CSMessageForwardMessage;
import io.netty.channel.Channel;

public class DefaultCSMessageForwardMessageHandler extends AbstractGatewayProviderMessageHandler implements  CSMessageForwardMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        CSMessageForwardMessage message = new CSMessageForwardMessage();
        messageExecutor.readMessage(message, frame);
        if (message.getServerName() != null) {
            ProviderManager providerManager = messageExecutor.getProviderManager(message.getServerName());
            if (providerManager != null) {
                providerManager.sendMessage2Consumer(message.getServerKey(), message.getId(), message.getData());
            } else {
                messageExecutor.providerManagerForEach(each -> each.sendMessage2Consumer(message.getServerKey(), message.getId(), message.getData()));

            }
        } else {

            messageExecutor.providerManagerForEach(providerManager -> providerManager.sendMessage2Consumer(message.getServerKey(), message.getId(), message.getData()));

        }
    }

    @Override
    public int messageId() {
        return CSMessageForwardMessage.MESSAGE_ID;
    }
}
