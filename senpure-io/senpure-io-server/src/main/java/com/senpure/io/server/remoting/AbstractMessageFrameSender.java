package com.senpure.io.server.remoting;

import com.senpure.base.util.Assert;
import com.senpure.io.server.MessageFrame;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class AbstractMessageFrameSender implements MessageFrameSender {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected FutureService futureService;

    protected int defaultTimeout = 500;
    protected int defaultFlushFrames = 100;

    public AbstractMessageFrameSender() {
    }


    public void verifyWorkable() {

        Assert.notNull(futureService);

    }

    @Override
    public void sendMessage(Channel channel, MessageFrame frame) {
        channel.writeAndFlush(frame);
    }

    @Override
    public void sendMessage(Channel channel, List<MessageFrame> frames) {

        sendMessage(channel, frames, defaultFlushFrames);
    }

    protected void sendMessage(Channel channel, List<MessageFrame> frames, int flushFrames) {

        int temp = 1;
        for (MessageFrame frame : frames) {
            channel.write(frame);
            if (temp % flushFrames == 0) {
                channel.flush();
                temp = 1;
            }
            temp++;
        }
        if (temp > 1) {
            channel.flush();
        }

    }

    @Override
    public void sendMessage(Channel channel, MessageFrame frame, ResponseCallback callback) {

        sendMessage(channel, frame, callback, defaultTimeout);
    }

    @Override
    public void sendMessage(Channel channel, MessageFrame frame, ResponseCallback callback, int timeout) {
        ResponseFuture future = futureService.future(channel, frame.requestId(), frame.message(), timeout);
        future.setCallback(callback);
        channel.writeAndFlush(frame);
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(Channel channel, MessageFrame frame) {
        return sendSyncMessage(channel, frame, defaultTimeout);
    }

    @Nonnull
    @Override
    public Response sendSyncMessage(Channel channel, MessageFrame frame, int timeout) {
        ResponseFuture future = futureService.future(channel, frame.requestId(), frame.message(),timeout);
        if (channel.isWritable()) {
            channel.writeAndFlush(frame);
        }
        return future.get(timeout);
    }

    public void setFutureService(FutureService futureService) {
        this.futureService = futureService;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setDefaultFlushFrames(int defaultFlushFrames) {
        this.defaultFlushFrames = defaultFlushFrames;
    }
}
