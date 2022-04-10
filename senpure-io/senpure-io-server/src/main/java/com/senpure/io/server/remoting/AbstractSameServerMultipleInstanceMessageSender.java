package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageFrame;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

/**
 * 同一个服会有多个实例相连
 */
public abstract class AbstractSameServerMultipleInstanceMessageSender<T extends MessageFrame> extends AbstractMessageSender<T> implements SameServerMultipleInstanceMessageSender {


    public String getRemoteServerKey(String host, int port) {
        return host + ":" + port;
    }

    @Override
    public void sendMessage(ServerInstanceMessageFrameSender server, Message message) {

        server.sendMessage(createMessage(message));
    }

    @Override
    public void sendMessage(ServerInstanceMessageFrameSender server, Message message, ResponseCallback callback) {
        server.sendMessage(createMessage(message, nextRequestId()), callback);
    }

    @Override
    public void sendMessage(ServerInstanceMessageFrameSender server, Message message, ResponseCallback callback, int timeout) {
        server.sendMessage(createMessage(message, nextRequestId()), callback, timeout);
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(ServerInstanceMessageFrameSender server, Message message) {
        return server.sendSyncMessage(createMessage(message, nextRequestId()));
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(ServerInstanceMessageFrameSender server, Message message, int timeout) {
        return server.sendSyncMessage(createMessage(message, nextRequestId()), timeout);
    }

    @Override
    public void respondMessage(ServerInstanceMessageFrameSender server, Message message) {

        server.sendMessage(createMessage(message, requestId()));
    }

    @Override
    public void respondMessage(ServerInstanceMessageFrameSender server, Message message, int requestId) {
        server.sendMessage(createMessage(message, requestId));
    }

    @Override
    public void sendMessage(Channel channel, Message message) {

        getFrameSender(channel).sendMessage(channel, createMessage(message));
    }

    @Override
    public void sendMessage(Channel channel, Message message, ResponseCallback callback) {

        getFrameSender(channel).sendMessage(channel, createMessage(message), callback);
    }

    @Override
    public void sendMessage(Channel channel, Message message, ResponseCallback callback, int timeout) {
        getFrameSender(channel).sendMessage(channel, createMessage(message), callback, timeout);
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(Channel channel, Message message) {
        return getFrameSender(channel).sendSyncMessage(channel, createMessage(message));

    }

    @Nonnull
    @Override
    public Response sendSyncMessage(Channel channel, Message message, int timeout) {
        return getFrameSender(channel).sendSyncMessage(channel, createMessage(message), timeout);
    }

    @Override
    public void respondMessage(Channel channel, Message message) {

        getFrameSender(channel).sendMessage(channel, createMessage(message, nextRequestId()));
    }

    @Override
    public void respondMessage(Channel channel, Message message, int requestId) {
        getFrameSender(channel).sendMessage(channel, createMessage(message, requestId));
    }

    public abstract MessageFrameSender getFrameSender(Channel channel);
}
