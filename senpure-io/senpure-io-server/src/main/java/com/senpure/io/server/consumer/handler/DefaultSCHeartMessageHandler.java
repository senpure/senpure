package com.senpure.io.server.consumer.handler;


import com.senpure.io.server.protocol.message.SCHeartMessage;
import io.netty.channel.Channel;

/**
 * SCHeartMessageHandler
 *
 * @author senpure
 * @time 2019-03-19 14:59:03
 */
public class DefaultSCHeartMessageHandler extends AbstractConsumerMessageHandler<SCHeartMessage> implements SCHeartMessageHandler {
    @Override
    public void execute(Channel channel, SCHeartMessage message) throws Exception {
        logger.trace("heartMessage {}", message.toString());
    }

    @Override
    public SCHeartMessage newEmptyMessage() {
        return new SCHeartMessage();
    }

    @Override
    public int messageId() {
        return SCHeartMessage.MESSAGE_ID;
    }
}
