package com.senpure.io.server.provider.handler;

import com.senpure.io.server.protocol.message.SCFrameworkVerifyProviderMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class DefaultSCFrameworkVerifyProviderMessageHandler extends AbstractProviderSCMessageHandler<SCFrameworkVerifyProviderMessage> implements SCFrameworkVerifyProviderMessageHandler {

    @Override
    public int messageId() {
        return SCFrameworkVerifyProviderMessage.MESSAGE_ID;
    }


    @Override
    public void execute(Channel channel, long token, long userId, SCFrameworkVerifyProviderMessage message) throws Exception {

    }

    @Nonnull
    @Override
    public SCFrameworkVerifyProviderMessage newEmptyMessage() {
        return new SCFrameworkVerifyProviderMessage();
    }
}
