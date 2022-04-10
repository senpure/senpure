package com.senpure.io.server.consumer.handler;


import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import io.netty.channel.Channel;

/**
 * SCInnerErrorMessageHandler
 *
 * @author senpure
 * @time 2019-07-02 17:02:44
 */
public class DefaultSCFrameworkErrorMessageHandler extends AbstractConsumerMessageHandler<SCFrameworkErrorMessage> implements SCFrameworkErrorMessageHandler {
    @Override
    public void execute(Channel channel, SCFrameworkErrorMessage message) throws Exception {
        logger.error(message.toString());
    }

    @Override
    public int messageId() {
        return SCFrameworkErrorMessage.MESSAGE_ID;
    }

    @Override
    public SCFrameworkErrorMessage newEmptyMessage() {
        return new SCFrameworkErrorMessage();
    }
}
