package com.senpure.io.server.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.protocol.message.CSMatchingConsumerMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCMatchingConsumerMessage;
import com.senpure.io.server.provider.matching.Matchable;
import com.senpure.io.server.provider.matching.MatchableManager;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

public class DefaultCSMatchingConsumerMessageHandler extends AbstractFrameworkNecessaryMessageHandler<CSMatchingConsumerMessage> implements CSMatchingConsumerMessageHandler {

    @Resource
    protected MatchableManager matchableManager;

    @Override
    public int messageId() {
        return CSMatchingConsumerMessage.MESSAGE_ID;
    }

    @Override
    public void execute(Channel channel, CSMatchingConsumerMessage message) {
        Matchable matchable = matchableManager.createMatchable();
        if (matchable != null) {
            matchable.match(message.getConsumers(), message.getTimeout());
            SCMatchingConsumerMessage consumerMessage = new SCMatchingConsumerMessage();
            consumerMessage.setMatchableId(matchable.matchableId());
            consumerMessage.setServerName(message.getServerName());
            consumerMessage.setServerKey(ChannelAttributeUtil.getLocalServerKey(channel));
            //直接表示请求成功
            messageSender.respondMessage(channel, consumerMessage);
        } else {
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_PROVIDER_ERROR);
            errorMessage.setMessage("服务器匹配失败");
            messageSender.respondMessage(channel, errorMessage);
        }


    }

    @Nonnull
    @Override
    public CSMatchingConsumerMessage newEmptyMessage() {
        return new CSMatchingConsumerMessage();
    }
}
