package com.senpure.io.server.provider.handler;

import com.senpure.io.server.protocol.message.SCRegisterProviderMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class DefaultSCRegisterProviderMessageHandler extends AbstractProviderSCMessageHandler<SCRegisterProviderMessage> implements SCRegisterProviderMessageHandler {
    @Override
    public int messageId() {
        return SCRegisterProviderMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, long token, long userId, SCRegisterProviderMessage message) throws Exception {

    }

    @Nonnull
    @Override
    public SCRegisterProviderMessage newEmptyMessage() {
        return new SCRegisterProviderMessage();
    }
}
