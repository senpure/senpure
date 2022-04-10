package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageFrame;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

/**
 * 同一个服会只有一个实例相连
 */
public abstract class AbstractSameServerSingleInstanceMessageSender<T extends MessageFrame, R extends ServerInstanceMessageFrameSender> extends AbstractSameServerMultipleInstanceMessageSender<T> implements SameServerSingleInstanceMessageSender {


    protected R defaultFrameSender;

    public void setDefaultFrameSender(R defaultFrameSender) {
        this.defaultFrameSender = defaultFrameSender;
    }

    public R getDefaultFrameSender() {
        return defaultFrameSender;
    }

    @Override
    public void sendMessage(Message message) {

        defaultFrameSender.sendMessage(createMessage(message));
    }

    @Override
    public void sendMessage(Message message, ResponseCallback callback) {
        defaultFrameSender.sendMessage(createMessage(message, nextRequestId()), callback);
    }

    @Override
    public void sendMessage(Message message, ResponseCallback callback, int timeout) {
        defaultFrameSender.sendMessage(createMessage(message, nextRequestId()), callback, timeout);
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(Message message) {
        return defaultFrameSender.sendSyncMessage(createMessage(message, nextRequestId()));
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(Message message, int timeout) {
        return defaultFrameSender.sendSyncMessage(createMessage(message, nextRequestId()), timeout);
    }

    @Override
    public void respondMessage(Message message) {


        defaultFrameSender.sendMessage(createMessage(message, requestId()));
    }

    @Override
    public void respondMessage(Message message, int requestId) {
        defaultFrameSender.sendMessage(createMessage(message, requestId));
    }

    @Override
    public ServerInstanceMessageFrameSender getFrameSender(Channel channel) {
        return defaultFrameSender;
    }
}
