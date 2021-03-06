package com.senpure.io.server.direct;


import com.senpure.io.server.AbstractMessageEncoder;
import com.senpure.io.server.provider.ProviderSendMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class DirectMessageEncoder extends AbstractMessageEncoder<ProviderSendMessage> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProviderSendMessage message, ByteBuf byteBuf) {

        encode(byteBuf, message.getRequestId(), message.getMessage());
    }
}
