package com.senpure.io.server.provider.handler;


import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.protocol.message.CSRelationUserGatewayMessage;
import com.senpure.io.server.protocol.message.SCRelationUserGatewayMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

/**
 * 关联用户与网关处理器
 *
 * @author senpure
 * @time 2018-10-17 14:59:15
 */
public class DefaultCSRelationUserGatewayMessageHandler extends AbstractFrameworkNecessaryMessageHandler<CSRelationUserGatewayMessage> implements CSRelationUserGatewayMessageHandler  {



    @Override
    public void execute(Channel channel,  CSRelationUserGatewayMessage message) {
        String gatewayKey = ChannelAttributeUtil.getRemoteServerKey(channel);
        logger.debug("关联网关 与用户 gatewayKey :{}  token :{}  userId :{}", gatewayKey, message.getToken(), message.getUserId());
        if (message.getUserId() > 0) {
           messageSender.relationUser(gatewayKey, message.getUserId(), message.getRelationToken());
        }
        if (message.getToken()!= 0) {
            messageSender.relationToken(gatewayKey, message.getToken(), message.getRelationToken());
        }

        SCRelationUserGatewayMessage scMessage = new SCRelationUserGatewayMessage();
        scMessage.setRelationToken(message.getRelationToken());
        scMessage.setToken(message.getToken());
        scMessage.setUserId(message.getUserId());
        if (message.getUserId() > 0) {
            messageSender.respondMessage(message.getUserId(), scMessage);
        } else {
            messageSender.respondMessageByToken(message.getToken(), scMessage);
        }

    }

    /**
     * new 一个空对象
     */
    @Nonnull
    @Override
    public CSRelationUserGatewayMessage newEmptyMessage() {
        return new CSRelationUserGatewayMessage();
    }


    @Override
    public int messageId() {
        return CSRelationUserGatewayMessage.MESSAGE_ID;
    }


}