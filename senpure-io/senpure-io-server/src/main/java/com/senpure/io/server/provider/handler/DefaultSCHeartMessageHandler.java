package com.senpure.io.server.provider.handler;

import com.senpure.io.server.protocol.message.SCHeartMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class DefaultSCHeartMessageHandler extends AbstractProviderSCMessageHandler<SCHeartMessage> implements SCHeartMessageHandler {
    @Override
    public int messageId() {
        return SCHeartMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, long token, long userId, SCHeartMessage message) throws Exception {

    }

    @Nonnull
    @Override
    public SCHeartMessage newEmptyMessage() {
        return new SCHeartMessage();
    }
}
