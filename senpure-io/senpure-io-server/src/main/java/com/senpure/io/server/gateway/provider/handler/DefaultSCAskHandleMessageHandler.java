package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.WaitAskTask;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.SCAskHandleMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;

public class DefaultSCAskHandleMessageHandler extends AbstractGatewayProviderMessageHandler implements SCAskHandleMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        //todo 改成和请求关联一样的同步请求
        SCAskHandleMessage message = new SCAskHandleMessage();
        messageExecutor.readMessage(message, frame);
        WaitAskTask waitAskTask = messageExecutor.waitAskMap.get(message.getAskToken());
        if (waitAskTask != null) {
            if (message.isHandle()) {
                String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
                String serverKey = ChannelAttributeUtil.getRemoteServerKey(channel);
                logger.debug("{} {} 可以处理 {} 值位 {} 的请求", serverName, serverKey,
                        MessageIdReader.read(waitAskTask.getFromMessageId()), message.getAskValue());
                ProviderManager providerManager = messageExecutor.getProviderManager(serverName);
                if (providerManager != null) {
                    for (Provider useProvider : providerManager.getUseProviders()) {
                        if (useProvider.getRemoteServerKey().equalsIgnoreCase(serverKey)) {
                            waitAskTask.answer(providerManager, useProvider, true);
                            return;
                        }
                    }
                }

            }
            if (logger.isDebugEnabled()) {
                String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
                String serverKey = ChannelAttributeUtil.getRemoteServerKey(channel);
                logger.debug("{} {} 无法处理 {} 值位 {} 的请求", serverName, serverKey,
                        MessageIdReader.read(waitAskTask.getFromMessageId()), message.getAskValue());
            }
            waitAskTask.answer(null, null, false);
        }
    }

    @Override
    public int messageId() {
        return SCAskHandleMessage.MESSAGE_ID;
    }
}
