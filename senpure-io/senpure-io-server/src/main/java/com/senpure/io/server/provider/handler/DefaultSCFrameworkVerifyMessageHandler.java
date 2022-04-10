package com.senpure.io.server.provider.handler;

import com.senpure.io.server.protocol.message.SCFrameworkVerifyMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class DefaultSCFrameworkVerifyMessageHandler extends AbstractProviderSCMessageHandler<SCFrameworkVerifyMessage>
        implements SCFrameworkVerifyMessageHandler{
    @Override
    public int messageId() {
        return SCFrameworkVerifyMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, long token, long userId, SCFrameworkVerifyMessage message) throws Exception {

    }

    @Nonnull
    @Override
    public SCFrameworkVerifyMessage newEmptyMessage() {
        return new SCFrameworkVerifyMessage();
    }
}
