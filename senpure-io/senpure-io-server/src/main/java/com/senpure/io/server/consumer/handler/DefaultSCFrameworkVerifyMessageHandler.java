package com.senpure.io.server.consumer.handler;

import com.senpure.io.server.protocol.message.SCFrameworkVerifyMessage;
import io.netty.channel.Channel;

public class DefaultSCFrameworkVerifyMessageHandler  extends AbstractConsumerMessageHandler<SCFrameworkVerifyMessage> implements  SCFrameworkVerifyMessageHandler {
    @Override
    public int messageId() {
        return SCFrameworkVerifyMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, SCFrameworkVerifyMessage message) throws Exception {

    }

    @Override
    public SCFrameworkVerifyMessage newEmptyMessage() {
        return new SCFrameworkVerifyMessage();
    }
}
