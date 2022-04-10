package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class DefaultResponse implements Response {
    private final Message message;
    private final Channel channel;

    private final boolean success;

    public DefaultResponse(boolean success, @Nonnull Channel channel, @Nonnull Message message) {
        this.channel = channel;
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }



    @SuppressWarnings({"unchecked"})
    @Nonnull
    @Override
    public <T extends Message> T getMessage() {
        return (T) message;
    }


    @Nonnull
    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "DefaultResponse{" +
                "success=" + success +
                ", channel=" + channel +
                ", message=" + message +
                '}';
    }
}
