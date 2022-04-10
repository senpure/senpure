package com.senpure.io.server.provider.gateway;

import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.provider.ProviderSendMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProviderGatewayMessageEncoder extends MessageToByteEncoder<ProviderSendMessage> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void encode(ChannelHandlerContext ctx, ProviderSendMessage frame, ByteBuf out) {
        Message message = frame.message();
        int dataLength = message.serializedSize();
        int headLength = CompressBean.computeVar32Size(frame.messageType());
        headLength += CompressBean.computeVar32Size(frame.requestId());
        headLength += CompressBean.computeVar32Size(frame.messageId());
        headLength += CompressBean.computeVar64Size(frame.getToken());
        headLength += CompressBean.computeVar32Size(frame.getUserIds().length);
        for (Long userId : frame.getUserIds()) {
            headLength += CompressBean.computeVar64Size(userId);
        }
        int packageLength = headLength + dataLength;
        out.ensureWritable(CompressBean.computeVar32Size(packageLength) + packageLength);

        CompressBean.writeVar32(out, packageLength);
        CompressBean.writeVar32(out, frame.messageType());
        CompressBean.writeVar32(out, frame.requestId());
        CompressBean.writeVar32(out, frame.messageId());
        CompressBean.writeVar64(out, frame.getToken());
        CompressBean.writeVar32(out, frame.getUserIds().length);
        for (Long userId : frame.getUserIds()) {
            CompressBean.writeVar64(out, userId);
        }
        message.write(out);
    }
}
