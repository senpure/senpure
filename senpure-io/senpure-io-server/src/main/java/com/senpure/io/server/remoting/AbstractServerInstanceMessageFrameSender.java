package com.senpure.io.server.remoting;

import com.senpure.base.util.Assert;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.Constant;
import com.senpure.io.server.MessageFrame;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public abstract class AbstractServerInstanceMessageFrameSender extends AbstractMessageFrameSender implements ServerInstanceMessageFrameSender {

    protected TaskLoopGroup service;
    protected int defaultWaitSendTimeout = 10000;
    protected String remoteServerKey;
    protected final List<WaitSendMessage> waitSendMessages = new ArrayList<>();

    protected ChannelService channelService;
    private boolean connecting = false;
    private final ReadWriteLock connectLock = new ReentrantReadWriteLock();

    /**
     * 认证通过
     */
    private boolean frameworkVerifyPassed;

    public AbstractServerInstanceMessageFrameSender() {
    }

    public AbstractServerInstanceMessageFrameSender(TaskLoopGroup service) {
        this.service = service;
    }


    @Override
    public void verifyWorkable() {
        Assert.notNull(futureService);
        Assert.notNull(remoteServerKey);
        Assert.notNull(channelService);
    }

    public void checkWaitSendMessage() {
        synchronized (waitSendMessages) {
            if (waitSendMessages.size() > 0) {
                TaskLoopGroup executor = service;
                if (executor != null) {
                    executor.execute(this::sendWaitMessage);
                } else {
                    sendWaitMessage();
                }
            }
        }
    }

    protected void addWaitMessage(WaitSendMessage waitSendMessage) {
        synchronized (waitSendMessages) {
            logger.info("加入重新发送队列,等待可用channel {}", waitSendMessage.getFrame());
            waitSendMessages.add(waitSendMessage);
        }
    }

    protected void sendWaitMessage() {
        List<WaitSendMessage> list = new ArrayList<>(waitSendMessages.size());
        long now = System.currentTimeMillis();
        synchronized (waitSendMessages) {
            if (waitSendMessages.size() > 0) {
                for (WaitSendMessage message : waitSendMessages) {
                    if (now - message.firstSendTime <= message.waitSendTimeout) {
                        list.add(message);
                    } else {
                        logger.warn("超过重试时间限制,不再重新发送 {} {}", message.waitSendTimeout, message.frame);
                    }
                }
                waitSendMessages.clear();
            }
        }

        if (list.size() > 0) {
            Channel channel = channelService.nextChannel();
            if (channel != null) {
                int count = 1;
                for (WaitSendMessage failMessage : list) {
                    if (failMessage.timeout == 0) {        //handler 处理
                        logger.info("重新发送消息{}", failMessage.frame);
                        channel.write(failMessage.frame);
                    } else if (failMessage.callback != null) {//异步回调
                        ResponseFuture future = futureService.future(channel, failMessage.frame.requestId(), failMessage.frame.message(), failMessage.timeout);
                        future.setCallback(failMessage.callback);
                        logger.info("重新发送异步回调消息{}", failMessage.frame);
                        channel.write(failMessage.frame);

                    } else if (failMessage.synchronizer != null) {//同步回调
                        logger.info("重新发送同步回调消息{}", failMessage.frame);
                        ResponseFuture future = futureService.future(channel, failMessage.frame.requestId(), failMessage.frame.message(), failMessage.timeout);
                        channel.write(failMessage.frame);
                        Synchronizer synchronizer = failMessage.synchronizer;
                        try {
                            synchronizer.lock.lock();
                            synchronizer.future = future;
                            synchronizer.done.signal();
                        } finally {
                            synchronizer.lock.unlock();
                        }

                    }
                    if (count % 100 == 0) {
                        channel.flush();
                        count = 1;
                    }
                    count++;
                }
                if (count > 1) {
                    channel.flush();
                }
            } else {
                synchronized (waitSendMessages) {
                    waitSendMessages.addAll(list);
                }
            }
        }

    }


    public void addChannel(Channel channel) {
        channelService.addChannel(channel);
        checkWaitSendMessage();

    }

    public boolean removeChannel(Channel channel) {
        return channelService.removeChannel(channel);
    }

    public int getChannelSize() {
        return channelService.channelSize();
    }

    public boolean isConnecting() {
        boolean temp;
        connectLock.readLock().lock();
        temp = connecting;
        connectLock.readLock().unlock();
        return temp;
    }

    public void setConnecting(boolean connecting) {
        connectLock.writeLock().lock();
        this.connecting = connecting;
        connectLock.writeLock().unlock();

    }

    @Override
    public void sendMessage(List<MessageFrame> frames) {
        Channel channel = channelService.nextChannel();
        if (channel != null) {
            sendMessage(channel, frames, defaultFlushFrames);
        } else {
            for (MessageFrame frame : frames) {
                WaitSendMessage waitSendMessage = new WaitSendMessage();
                waitSendMessage.setFirstSendTime(System.currentTimeMillis());
                waitSendMessage.setFrame(frame);
                waitSendMessage.setWaitSendTimeout(defaultWaitSendTimeout);
                addWaitMessage(waitSendMessage);
            }
        }
    }


    @Override
    public void sendMessage(MessageFrame frame) {
        Channel channel = channelService.nextChannel();
        if (channel != null) {
            sendMessage(channel, frame);
        } else {
            WaitSendMessage waitSendMessage = new WaitSendMessage();
            waitSendMessage.setFirstSendTime(System.currentTimeMillis());
            waitSendMessage.setFrame(frame);
            waitSendMessage.setWaitSendTimeout(defaultWaitSendTimeout);
            addWaitMessage(waitSendMessage);
        }
    }


    @Override
    public void sendMessage(MessageFrame frame, ResponseCallback callback) {

        sendMessage(frame, callback, defaultTimeout);
    }


    @Override
    public void sendMessage(MessageFrame frame, ResponseCallback callback, int timeout) {

        Channel channel = channelService.nextChannel();
        if (channel != null) {

            sendMessage(channel, frame, callback, timeout);
        } else {
            WaitSendMessage waitSendMessage = new WaitSendMessage();
            waitSendMessage.setFirstSendTime(System.currentTimeMillis());
            waitSendMessage.setFrame(frame);
            waitSendMessage.setTimeout(timeout);
            waitSendMessage.setWaitSendTimeout(defaultWaitSendTimeout);
            waitSendMessage.setCallback(callback);
            addWaitMessage(waitSendMessage);
        }
    }


    @Nonnull
    @Override
    public Response sendSyncMessage(MessageFrame frame) {
        return sendSyncMessage(frame, defaultTimeout);
    }


    @Nonnull
    @Override
    public Response sendSyncMessage(MessageFrame frame, int timeout) {
        return sendSyncMessage(frame, timeout, defaultWaitSendTimeout);
    }

    @Nonnull
    protected Response sendSyncMessage(MessageFrame frame, int timeout, int waitSendTimeout) {
        Channel channel = channelService.nextChannel();
        if (channel != null) {
            return sendSyncMessage(channel, frame, timeout);
        } else if (waitSendTimeout > 0) {
            Synchronizer synchronizer = new Synchronizer();
            WaitSendMessage waitSendMessage = new WaitSendMessage();
            waitSendMessage.setFirstSendTime(System.currentTimeMillis());
            waitSendMessage.setFrame(frame);
            waitSendMessage.setTimeout(timeout);
            waitSendMessage.setWaitSendTimeout(waitSendTimeout);
            waitSendMessage.setSynchronizer(synchronizer);
            addWaitMessage(waitSendMessage);
            try {
                synchronizer.lock.lock();
                if (synchronizer.done.await(waitSendTimeout, TimeUnit.MILLISECONDS)) {
                    return synchronizer.future.get(timeout);
                }
                return FrameworkErrorResponse.timeout(EmptyChannel.EMPTY_CHANNEL, frame.message().messageId(), waitSendTimeout);

            } catch (InterruptedException e) {
                return FrameworkErrorResponse.interrupted(EmptyChannel.EMPTY_CHANNEL, frame.message().messageId(), timeout);

            } finally {
                synchronizer.lock.unlock();
            }
        } else {

            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_CHANNEL_NOT_AVAILABLE);
            errorMessage.setMessage(remoteServerKey + "没有可用的channel");
            errorMessage.getArgs().add(remoteServerKey);

            return new FrameworkErrorResponse(EmptyChannel.EMPTY_CHANNEL, errorMessage);
        }

    }


    public int getDefaultWaitSendTimeout() {
        return defaultWaitSendTimeout;
    }

    public void setDefaultWaitSendTimeout(int defaultWaitSendTimeout) {
        this.defaultWaitSendTimeout = defaultWaitSendTimeout;
    }


    public String getRemoteServerKey() {
        return remoteServerKey;
    }

    public void setRemoteServerKey(String remoteServerKey) {
        this.remoteServerKey = remoteServerKey;
    }

    public List<WaitSendMessage> getWaitSendMessages() {
        return waitSendMessages;
    }

    public FutureService getFutureService() {
        return futureService;
    }


    public ChannelService getChannelService() {
        return channelService;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public boolean isFrameworkVerifyPassed() {
        return frameworkVerifyPassed;
    }

    public void setFrameworkVerifyPassed(boolean frameworkVerifyPassed) {
        this.frameworkVerifyPassed = frameworkVerifyPassed;
    }

    protected static class WaitSendMessage {
        private MessageFrame frame;
        private long firstSendTime;
        private ResponseCallback callback;
        //等于0表示不需要服务器返回
        private int timeout;
        private int waitSendTimeout;
        private Synchronizer synchronizer;

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public MessageFrame getFrame() {
            return frame;
        }

        public void setFrame(MessageFrame frame) {
            this.frame = frame;
        }

        public long getFirstSendTime() {
            return firstSendTime;
        }

        public void setFirstSendTime(long firstSendTime) {
            this.firstSendTime = firstSendTime;
        }

        public Synchronizer getSynchronizer() {
            return synchronizer;
        }

        public void setSynchronizer(Synchronizer synchronizer) {
            this.synchronizer = synchronizer;
        }

        public ResponseCallback getCallback() {
            return callback;
        }

        public void setCallback(ResponseCallback callback) {
            this.callback = callback;
        }

        public int getWaitSendTimeout() {
            return waitSendTimeout;
        }

        public void setWaitSendTimeout(int waitSendTimeout) {
            this.waitSendTimeout = waitSendTimeout;
        }
    }


    protected static class Synchronizer {
        private final Lock lock = new ReentrantLock();
        private final Condition done = lock.newCondition();
        private volatile ResponseFuture future;

    }
}
