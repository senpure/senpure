package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.Constant;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class FrameworkErrorResponse implements Response {

    private final SCFrameworkErrorMessage message;

    private final Channel channel;

    public static FrameworkErrorResponse timeout(Channel channel, int messageId, int timeout) {
        SCFrameworkErrorMessage message = new SCFrameworkErrorMessage();
        message.setCode(Constant.ERROR_TIMEOUT);
        message.setMessage("同步请求超时[" + messageId + "] :" + timeout);
        message.getArgs().add(String.valueOf(messageId));
        message.getArgs().add(String.valueOf(timeout));
        return new FrameworkErrorResponse(channel, message);
    }

    public static FrameworkErrorResponse interrupted(Channel channel, int messageId, int timeout) {
        SCFrameworkErrorMessage message = new SCFrameworkErrorMessage();
        message.setCode(Constant.ERROR_INTERRUPTED);
        message.setMessage("同步请求线程中断[" + messageId + "] :" + timeout);
        message.getArgs().add(String.valueOf(messageId));
        message.getArgs().add(String.valueOf(timeout));
        return new FrameworkErrorResponse(channel, message);
    }

    public FrameworkErrorResponse(Channel channel, SCFrameworkErrorMessage message) {
        this.channel = channel;
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return false;
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
        return "FrameworkErrorResponse{" +
                "channel=" + channel +
                ", message=" + message +
                '}';
    }
}
