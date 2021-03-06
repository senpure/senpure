package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.ProviderManager;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.protocol.message.SCStatisticMessage;
import io.netty.channel.Channel;

public class SCStatisticMessageHandler  extends AbstractProviderMessageHandler{

    @Override
    public void execute(Channel channel, GatewayReceiveProviderMessage gatewayReceiveProviderMessage) {
        SCStatisticMessage message = new SCStatisticMessage();
        messageExecutor.readMessage(message, gatewayReceiveProviderMessage);
        String producerKey = ChannelAttributeUtil.getRemoteServerKey(channel);
        String producerName = ChannelAttributeUtil.getRemoteServerName(channel);
        ProviderManager providerManager =   messageExecutor.providerManagerMap.get(producerName);
        if (providerManager != null) {
            Provider provider = providerManager.getProducer(producerKey);
            if (provider != null) {
                provider.updateScore(message.getStatistic().getScore());
            } else {
                logger.warn("{} producer is null", producerKey);
            }
        } else {

            logger.warn("{} producerManager is null", producerName);
        }

    }

    @Override
    public int handleMessageId() {
        return SCStatisticMessage.MESSAGE_ID;
    }
}
