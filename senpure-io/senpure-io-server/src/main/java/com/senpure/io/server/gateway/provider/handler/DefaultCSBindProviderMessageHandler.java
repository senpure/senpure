package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.CSBindProviderMessage;
import com.senpure.io.server.protocol.message.SCBindProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCMatchingSuccessMessage;
import io.netty.channel.Channel;

public class DefaultCSBindProviderMessageHandler extends AbstractGatewayProviderMessageHandler implements CSBindProviderMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        CSBindProviderMessage message = new CSBindProviderMessage();
        readMessage(message, frame);
        ProviderManager providerManager = messageExecutor.getProviderManager(message.getServerName());
        if (providerManager != null) {
            Provider provider = providerManager.getProvider(message.getServerKey());
            if (provider != null) {
                long token = message.getToken();
                long userId = message.getUserId();
                Channel userChannel = messageExecutor.tokenChannel.get(token);
                if (userChannel == null) {
                    userChannel = messageExecutor.userClientChannel.get(userId);
                    if (userChannel == null) {
                        logger.error("user channel is null {} {}", token, userId);
                        SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                        errorMessage.setCode(Constant.ERROR_GATEWAY_ERROR);
                        errorMessage.setMessage("系统错误，匹配失败");
                        messageExecutor.responseMessage2Consumer(frame.requestId(), token, errorMessage);
                        return;
                    }
                }

                if (token == 0) {
                    token = ChannelAttributeUtil.getToken(channel);
                }

                if (userId == 0) {
                    Long u = ChannelAttributeUtil.getUserId(channel);
                    if (u != null) {
                        userId = u;
                    }
                }

                Provider old = providerManager.provider(token);
                if (old != null && old != provider) {
                    logger.warn("匹配错误，已经有了关联");
                    SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                    errorMessage.setCode(Constant.ERROR_GATEWAY_ERROR);
                    errorMessage.setMessage("系统错误，匹配失败");
                    messageExecutor.responseMessage2Consumer(frame.requestId(), token, errorMessage);
                    return;
                }

                long finalToken = token;
                providerManager.relation(provider, token, userId, (cs, sc) -> {
                    providerManager.bind(finalToken, cs.getRelationToken(), provider);
                    messageExecutor.responseMessage2Producer(frame.requestId(), channel, new SCBindProviderMessage());
                });


            }
        }
    }

    @Override
    public int messageId() {
        return SCMatchingSuccessMessage.MESSAGE_ID;
    }

}
