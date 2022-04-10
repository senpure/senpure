package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.CSBreakUserGatewayMessage;
import com.senpure.io.server.protocol.message.SCBreakUserGatewayMessage;
import io.netty.channel.Channel;

public class DefaultCSBreakUserGatewayMessageHandler extends AbstractGatewayProviderMessageHandler implements CSBreakUserGatewayMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {

        CSBreakUserGatewayMessage message = new CSBreakUserGatewayMessage();
        readMessage(message, frame);
        String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
        ProviderManager providerManager = messageExecutor.getProviderManager(serverName);
        if (providerManager != null) {
            Channel userChannel = null;
            long token = message.getToken();
            if (token != 0) {
                userChannel = messageExecutor.tokenChannel.get(message.getToken());
            }
            if (userChannel == null) {
                userChannel = messageExecutor.userClientChannel.get(message.getUserId());

            }
            if (userChannel != null) {
                if (token == 0) {
                    token = ChannelAttributeUtil.getToken(userChannel);
                }
                long userId = message.getUserId();
                if (userId == 0) {
                    Long u = ChannelAttributeUtil.getUserId(userChannel);
                    if (u != null) {
                        userId = u;
                    }
                }

                providerManager.consumerLeaveProducer(userChannel, token, userId, message.getRelationToken());

            } else {
                logger.warn("userChannel is null");
            }

        }
        //只有收到了消息就返回一个成功消息
        messageExecutor.responseMessage2Producer(frame.requestId(), channel, new SCBreakUserGatewayMessage());


    }

    @Override
    public int messageId() {
        return CSBreakUserGatewayMessage.MESSAGE_ID;
    }
}
