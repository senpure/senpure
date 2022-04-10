package com.senpure.io.server.provider.handler;


import com.senpure.io.server.protocol.message.CSBreakUserGatewayMessage;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;


/**
 * 断开用户与网关处理器
 *
 * @author senpure
 * @time 2018-10-19 16:14:32
 */
public class DefaultCSBreakUserGatewayMessageHandler extends AbstractFrameworkNecessaryMessageHandler<CSBreakUserGatewayMessage>
        implements CSBreakUserGatewayMessageHandler {



    @Override
    public void execute(Channel channel, CSBreakUserGatewayMessage message) {
        if (message.getToken() != 0) {
            messageSender.breakToken(message.getToken(), message.getRelationToken());
        }
        messageSender.breakUser(message.getUserId(), message.getRelationToken());
    }


    /**
     * new 一个空对象
     */
    @Nonnull
    @Override
    public CSBreakUserGatewayMessage newEmptyMessage() {
        return new CSBreakUserGatewayMessage();
    }

    @Override
    public int messageId() {
        return CSBreakUserGatewayMessage.MESSAGE_ID;
    }
}