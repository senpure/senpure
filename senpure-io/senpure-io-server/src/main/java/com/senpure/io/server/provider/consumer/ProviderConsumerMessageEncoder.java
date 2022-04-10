package com.senpure.io.server.provider.consumer;


import com.senpure.io.server.AbstractMessageEncoder;
import com.senpure.io.server.provider.ProviderSendMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ProviderConsumerMessageEncoder extends AbstractMessageEncoder<ProviderSendMessage> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProviderSendMessage frame, ByteBuf byteBuf) {

        encode(byteBuf, frame.requestId(), frame.message());
    }
}
