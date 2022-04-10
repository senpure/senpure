package com.senpure.io.server.consumer;



import com.senpure.io.server.AbstractMessageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;


public class ConsumerMessageEncoder extends AbstractMessageEncoder<ConsumerMessage> {

    //包长int ,消息Id int, 二进制数据
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ConsumerMessage frame, ByteBuf out) {

        encode(out, frame.requestId(), frame.message());
    }


}
