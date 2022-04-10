package com.senpure.io.server.provider.handler;

import com.senpure.io.server.protocol.message.SCSuccessMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class DefaultSCSuccessMessageHandler extends AbstractProviderSCMessageHandler<SCSuccessMessage> implements SCSuccessMessageHandler {
    @Override
    public int messageId() {
        return SCSuccessMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, long token, long userId, SCSuccessMessage message) throws Exception {

    }

    @Nonnull
    @Override
    public SCSuccessMessage newEmptyMessage() {
        return new SCSuccessMessage();
    }
}
