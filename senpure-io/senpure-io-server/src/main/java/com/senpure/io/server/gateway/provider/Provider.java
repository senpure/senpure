package com.senpure.io.server.gateway.provider;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.remoting.AbstractServerInstanceMessageFrameSender;
import com.senpure.io.server.remoting.ChannelService;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class Provider extends AbstractServerInstanceMessageFrameSender {
    private final Statistic statistic = new Statistic();

    public Provider(TaskLoopGroup service) {
        super(service);
    }

    public static AttributeKey<Boolean> providerAddChannel = AttributeKey.valueOf("providerAddChannel'");

    public synchronized void addChannel(Channel channel) {

        Boolean add = ChannelAttributeUtil.get(channel, providerAddChannel);
        if (add != null && add) {
            logger.info("{}已经加入了关管理", channel);
            return;
        }
        if (getChannelSize() == 1 && channelService instanceof ChannelService.SingleChannelService) {
            ChannelService m = new ChannelService.MultipleChannelService(getRemoteServerKey());
            logger.info("升级channelService ---》》 ChannelService.MultipleChannelService");
            logger.info("升级channelService ---》》 ChannelService.MultipleChannelService");
            logger.info("升级channelService ---》》 ChannelService.MultipleChannelService");
            m.addChannel(channelService.nextChannel());
            setChannelService(m);
        }
        if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
            logger.info("add Channel {}  {} {}", channel, ChannelAttributeUtil.getRemoteServerName(channel), ChannelAttributeUtil.getRemoteServerKey(channel));

            super.addChannel(channel);
            ChannelAttributeUtil.set(channel, providerAddChannel, true);
        } else {
            logger.info("Channel不可用 skip  {}  {} {}", channel, ChannelAttributeUtil.getRemoteServerName(channel), ChannelAttributeUtil.getRemoteServerKey(channel));

        }

    }

    public synchronized boolean offline(Channel channel) {
        boolean removed = removeChannel(channel);
        if (removed) {
            logger.info("{} {}与网关断开连接", ChannelAttributeUtil.getRemoteServerName(channel), channel);
        }

        return removed;
    }

    public synchronized void updateScore(int score) {
        this.statistic.setScore(score);

    }

    public Statistic getStatistic() {
        return statistic;
    }

    public static class Statistic {
        //provider 汇报的分数
        int score;
        //生产者链接数量
        int consumerCount;

        public void consumerIncr() {
            consumerCount++;
        }

        public void consumerDecr() {
            consumerCount--;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public int getConsumerCount() {
            return consumerCount;
        }
    }
}
