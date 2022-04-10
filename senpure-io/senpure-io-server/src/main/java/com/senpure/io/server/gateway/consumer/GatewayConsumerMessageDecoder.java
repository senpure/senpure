package com.senpure.io.server.gateway.consumer;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.CompressBean;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayReceiveConsumerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class GatewayConsumerMessageDecoder extends ByteToMessageDecoder {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        int preIndex = in.readerIndex();
        int packageLength = CompressBean.tryReadVar32(in);
        if (preIndex == in.readerIndex()) {
            return;
        }
        if (packageLength == 0) {
            Assert.error("错误，数据包长度不能为0");
        }
        if (packageLength > in.readableBytes()) {
            this.logger.info("数据不够一个数据包 packageLength ={} ,readableBytes={}", packageLength, in.readableBytes());
            in.resetReaderIndex();
        } else {
            int messageType = CompressBean.readVar32(in);
            int requestId = CompressBean.readVar32(in);
            int messageId = CompressBean.readVar32(in);
            int messageLength = packageLength - (CompressBean.computeVar32Size(messageType)+CompressBean.computeVar32Size(requestId) + CompressBean.computeVar32Size(messageId));
            byte[] data = new byte[messageLength];
            in.readBytes(data);
            GatewayReceiveConsumerMessage frame = new GatewayReceiveConsumerMessage(messageLength,data);
            frame.setMessageId(messageId);
            frame.setMessageType(messageType);
            frame.setRequestId(requestId);
            Channel channel = ctx.channel();
            frame.setToken(ChannelAttributeUtil.getToken(channel));
            Long userId = ChannelAttributeUtil.getUserId(channel);
            if (userId != null) {
                frame.setUserId(userId);
            }
            out.add(frame);
           // GatewayReceiveConsumerMessage transfer = new GatewayReceiveConsumerMessage();
          //  transfer.setRequestId(requestId);
           // transfer.setData(data);
           // transfer.setMessageId(messageId);

           // out.add(transfer);
        }
    }


}
