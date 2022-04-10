package com.senpure.io.server.gateway.provider;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.CompressBean;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 将服务器转发到网关的消息，解析出来
 */
public class GatewayProviderMessageDecoder extends ByteToMessageDecoder {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

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
            long token = CompressBean.readVar64(in);
            int userLen = CompressBean.readVar32(in);
            Long[] userIds = new Long[userLen];
            for (int i = 0; i < userLen; i++) {
                userIds[i] = CompressBean.readVar64(in);
            }
            int headLength = CompressBean.computeVar32Size(messageType);
            headLength += CompressBean.computeVar32Size(requestId);
            headLength += CompressBean.computeVar32Size(messageId);

            headLength += CompressBean.computeVar64Size(token);
            headLength += CompressBean.computeVar32Size(userLen);
            for (Long userId : userIds) {
                headLength += CompressBean.computeVar64Size(userId);
            }

            int messageLength = packageLength - headLength;
            byte[] data = new byte[messageLength];
            in.readBytes(data);

            GatewayReceiveProviderMessage frame = new GatewayReceiveProviderMessage(messageLength, data);
            frame.setRequestId(requestId);
            frame.setMessageId(messageId);
            frame.setToken(token);
            frame.setMessageType(messageType);
            //  frame.setData(data);
            // frame.setToken(token);
            frame.setUserIds(userIds);

            out.add(frame);
        }
    }


}
