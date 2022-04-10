package com.senpure.io.server.remoting;


import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture implements ResponseFuture {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int requestId;

    // private final int messageId;

    private final int timeout;
    private final Channel channel;
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();

    private final Message message;
    private Response response;
    private ResponseCallback callback;
    private final long sendTime;
    private ScheduledFuture<?> scheduledFuture;
    public DefaultFuture(Channel channel, int requestId, Message message, int timeout) {

        this.channel = channel;
        this.requestId = requestId;
        this.message = message;
        this.timeout = timeout;
        this.sendTime = System.currentTimeMillis();

    }

    public void doReceived(Response response) {
        lock.lock();
        try {
            this.response = response;
            done.signal();
        } finally {
            lock.unlock();
        }
        if (callback != null) {
            invokeCallback(callback);
        }
    }

    @Override
    public void setCallback(@Nonnull ResponseCallback callback) {
        if (isDone()) {
            invokeCallback(callback);
        } else {
            boolean isdone = false;
            lock.lock();
            try {
                if (!isDone()) {
                    this.callback = callback;
                } else {
                    isdone = true;
                }
            } finally {
                lock.unlock();
            }
            if (isdone) {
                invokeCallback(callback);
            }
        }
    }

    private void invokeCallback(ResponseCallback callback) {

        if (callback == null) {
            throw new NullPointerException("回调不能为空");
        }
        if (response == null) {
            throw new NullPointerException("结果不能为空");
        }

        try {
            callback.execute(response);
        } catch (Exception e) {
            logger.error("执行回调出错", e);
        }

    }

    @Nonnull
    @Override
    public Response get() {
        return get(timeout);
    }

    @Nonnull
    @Override
    public Response get(int timeout) {
        if (timeout <= 0) {
            timeout = 500;
        }
        if (!isDone()) {
            lock.lock();
            try {
                if (!done.await(timeout, TimeUnit.MILLISECONDS)) {
                    int messageId = message.messageId();
                    return FrameworkErrorResponse.timeout(channel, messageId, timeout);
                }
            } catch (InterruptedException e) {
                int messageId = message.messageId();
                return FrameworkErrorResponse.interrupted(channel, messageId, timeout);
            } finally {
                lock.unlock();
            }
        }
        return response;
    }


    @Override
    public boolean isDone() {
        return response != null;
    }

    public long getSendTime() {
        return sendTime;
    }

    public int getTimeout() {
        return timeout;
    }

    public Channel getChannel() {
        return channel;
    }

    public int getRequestId() {
        return requestId;
    }

    public Message getMessage() {
        return message;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
