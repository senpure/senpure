package com.senpure.io.server.provider;


import com.senpure.base.util.Assert;
import com.senpure.executor.TaskLoopGroup;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class GatewayChannelManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ChannelManager channelManager;
    private static final AtomicInteger atomicCount = new AtomicInteger(0);

    private final Map<Long, Channel> idChannelMap = new ConcurrentHashMap<>();


    private final List<FailMessage> failMessages = new ArrayList<>(128);
    private int defaultMessageRetryTimeLimit = 10000;
    private boolean connecting = false;

    private final ReadWriteLock connectLock = new ReentrantReadWriteLock();

   private  final TaskLoopGroup service ;

    /**
     * GatewayChannelManager 的唯一标识
     */
    private final int gatewayChannelKey;

    private final String gatewayKey;


    public GatewayChannelManager(String gatewayKey, int channelPlanSize,TaskLoopGroup service ) {
        this.gatewayKey = gatewayKey;
        gatewayChannelKey = atomicCount.incrementAndGet();
        if (channelPlanSize <= 1) {
            channelManager = new SingleChannelManager();
        } else {
            channelManager = new MultipleChannelManager(gatewayKey);
        }
        this.service= service;
    }


    public void addChannel(Channel channel) {
        channelManager.addChannel(channel);
        checkFailMessage();
    }

    public void removeChannel(Channel channel) {
        channelManager.removeChannel(channel);
    }

    private void checkFailMessage() {
        synchronized (failMessages) {
            if (failMessages.size() > 0) {
               TaskLoopGroup executor = service;
                if (executor != null) {
                    executor.execute(this::sendFailMessage);
                } else {
                    logger.warn("没有从spring 容器中找到ProducerMessageExecutor");
                    new Thread(this::sendFailMessage).start();
                }
            }
        }

    }

    private void sendFailMessage() {
        List<FailMessage> list = new ArrayList<>(failMessages.size());
        long now = System.currentTimeMillis();
        synchronized (failMessages) {
            if (failMessages.size() > 0) {
                for (FailMessage message : failMessages) {
                    if (now - message.startTime <= message.messageRetryTimeLimit) {
                        list.add(message);
                    } else {
                        logger.warn("超过重试时间限制,不在重新发送 {}", message);
                    }
                }
                failMessages.clear();
            }
        }

        if (list.size() > 0) {
            Channel channel = channelManager.nextChannel();
            if (channel != null) {
                int temp = 1;
                for (FailMessage failMessage : list) {
                    logger.info("重新发送消息{}", failMessage.frame);
                    channel.write(failMessage.frame);
                    if (temp % 100 == 0) {
                        channel.flush();
                        temp = 1;
                    }
                    temp++;
                }
                if (temp > 1) {
                    channel.flush();
                }
            } else {
                synchronized (failMessages) {
                    failMessages.addAll(list);
                }
            }
        }
    }

    //心跳不会走这里
    private void addFailMessage(FailMessage failMessage) {
        synchronized (failMessages) {
            logger.info("加入重新发送队列,等待可用channel {}", failMessage.frame);
            failMessages.add(failMessage);
        }
    }

    public void sendMessage(List<ProviderSendMessage> frames) {

        sendMessage(frames, 100);
    }

    public void sendMessage(List<ProviderSendMessage> frames, int flushValue) {
        Channel channel = channelManager.nextChannel();
        if (channel != null) {
            int temp = 1;
            for (ProviderSendMessage frame : frames) {
                channel.write(frame);
                if (temp % flushValue == 0) {
                    channel.flush();
                    temp = 1;
                }
                temp++;
            }
            if (temp > 1) {
                channel.flush();
            }
        }
    }

    public void sendMessage(ProviderSendMessage frame) {
        Channel channel = channelManager.nextChannel();
        if (channel != null) {
            channel.writeAndFlush(frame);
            return;
        }
        FailMessage failMessage = new FailMessage();
        failMessage.startTime = System.currentTimeMillis();
        failMessage.frame = frame;
        failMessage.messageRetryTimeLimit = defaultMessageRetryTimeLimit;
        addFailMessage(failMessage);
        logger.error("全部channel 不可用 {}", toString());
    }


    public int getGatewayChannelKey() {
        return gatewayChannelKey;
    }


//    private int nextIndex2() {
//        if (channels.size() == 1) {
//            return 0;
//        }
//        int index = atomicIndex.incrementAndGet();
//        if (index >= channels.size()) {
//            boolean reset = atomicIndex.compareAndSet(index, 0);
//            if (!reset) {
//                return nextIndex2();
//            }
//            return 0;
//        }
//        return index;
//    }

    public String getGatewayKey() {
        return gatewayKey;
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

    public int getChannelSize() {
        return channelManager.channelSize();
    }


    public void setDefaultMessageRetryTimeLimit(int defaultMessageRetryTimeLimit) {
        this.defaultMessageRetryTimeLimit = defaultMessageRetryTimeLimit;
    }

    @Override
    public String toString() {
        return "GatewayChannelManager{" +
                "connecting=" + connecting +
                ", gatewayChannelKey=" + gatewayChannelKey +
                ", gatewayKey='" + gatewayKey + '\'' +
                '}';
    }

    private static class FailMessage {
        private long startTime;
        private ProviderSendMessage frame;

        private int messageRetryTimeLimit;

        public void setFrame(ProviderSendMessage frame) {
            this.frame = frame;
        }

    }

    private interface ChannelManager {
        void addChannel(Channel channel);

        void removeChannel(Channel channel);

        Channel nextChannel();

        int channelSize();
    }

    private static class SingleChannelManager implements ChannelManager {
        private Channel channel;

        @Override
        public void addChannel(Channel channel) {
            if (this.channel != null) {
                Assert.error("该模式只允许同时存在一个channel");
            }
            this.channel = channel;
        }

        @Override
        public void removeChannel(Channel channel) {
            if (this.channel.equals(channel)) {
                this.channel = null;
            }
        }

        @Override
        public Channel nextChannel() {
            return channel;
        }

        @Override
        public int channelSize() {
            return channel == null ? 0 : 1;
        }
    }

    private static class MultipleChannelManager implements ChannelManager {

        private List<Channel> channels = new ArrayList<>(16);
        private AtomicInteger atomicIndex = new AtomicInteger(-1);
        private String gatewayKey;
        private Logger logger = LoggerFactory.getLogger(getClass());

        public MultipleChannelManager(String gatewayKey) {
            this.gatewayKey = gatewayKey;
        }

        @Override
        public void addChannel(Channel channel) {
            if (channels.contains(channel)) {
                return;
            }
            channels.add(channel);
        }

        @Override
        public void removeChannel(Channel channel) {
            channels.remove(channel);
        }

        @Override
        public Channel nextChannel() {
            if (channels.size() == 0) {
                logger.warn("{}没有可用得channel ", gatewayKey);
                return null;
            }
            for (int i = 0; i < channels.size(); i++) {
                Channel channel = channels.get(nextIndex());
                if (channel.isWritable()) {
                    return channel;
                }
            }
            return null;
        }

        @Override
        public int channelSize() {
            return channels.size();
        }

        private int nextIndex() {
            if (channels.size() == 1) {
                return 0;
            }
            int index = atomicIndex.incrementAndGet();
            return Math.abs(index % channels.size());

        }
    }
}
