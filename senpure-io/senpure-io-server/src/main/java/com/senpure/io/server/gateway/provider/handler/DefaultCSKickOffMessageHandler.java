package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.CSKickOffMessage;
import io.netty.channel.Channel;

public class DefaultCSKickOffMessageHandler extends AbstractGatewayProviderMessageHandler  implements CSKickOffMessageHandler{
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        CSKickOffMessage message = new CSKickOffMessage ();
        messageExecutor.readMessage(message, frame);
        long tempUserId = message.getUserId();
        Channel userChannel = null;
        if (tempUserId > 0) {
            userChannel =   messageExecutor.userClientChannel.get(tempUserId);
        }
        if (userChannel == null) {
            userChannel =   messageExecutor.tokenChannel.get(message.getToken());
        }
        if (userChannel != null) {
            logger.info("{} token:{} uerId:{} 踢下线", userChannel, ChannelAttributeUtil.getToken(userChannel), ChannelAttributeUtil.getUserId(userChannel));
            userChannel.close();
        } else {
            logger.info("{} 踢下线失败，找不到channel", message);
        }
    }

    @Override
    public int messageId() {
        return CSKickOffMessage .MESSAGE_ID;
    }
}
