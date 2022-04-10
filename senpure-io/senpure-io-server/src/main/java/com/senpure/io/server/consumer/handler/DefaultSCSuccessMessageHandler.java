package com.senpure.io.server.consumer.handler;

import com.senpure.io.server.protocol.message.SCSuccessMessage;
import io.netty.channel.Channel;

public class DefaultSCSuccessMessageHandler extends AbstractConsumerMessageHandler<SCSuccessMessage> implements SCSuccessMessageHandler {
    @Override
    public int messageId() {
        return SCSuccessMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, SCSuccessMessage message) throws Exception {

    }

    @Override
    public SCSuccessMessage newEmptyMessage() {
        return new SCSuccessMessage();
    }
}
