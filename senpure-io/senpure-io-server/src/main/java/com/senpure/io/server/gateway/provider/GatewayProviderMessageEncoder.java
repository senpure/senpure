package com.senpure.io.server.gateway.provider;

import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.gateway.GatewaySendProviderMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将客户端发到网关的消息，重新编码，发送给具体的服务器
 */
public class GatewayProviderMessageEncoder extends MessageToByteEncoder<GatewaySendProviderMessage> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void encode(ChannelHandlerContext ctx, GatewaySendProviderMessage frame, ByteBuf out) {
//        int messageFrom = frame.getMessageFrom();
//        if (messageFrom == GatewaySendProviderMessage3.MESSAGE_FROM_CONSUMER) {
//            encodeRemoteMessage(frame, out);
//        } else if (messageFrom == GatewaySendProviderMessage3.MESSAGE_FROM_GATEWAY) {
//            encodeLocalMessage(frame, out);
//        } else {
//            encodeRemoteMessage(frame, out);
//        }

        encodeLocalMessage(frame, out);
    }

    private void encodeLocalMessage(GatewaySendProviderMessage frame, ByteBuf out) {
        int headLength = CompressBean.computeVar32Size(frame.messageType());
        headLength += CompressBean.computeVar32Size(frame.requestId());
        headLength += CompressBean.computeVar32Size(frame.messageId());
        headLength += CompressBean.computeVar64Size(frame.token());
        headLength += CompressBean.computeVar64Size(frame.userId());
        Message message = frame.message();
        int packageLength = headLength + message.serializedSize();
        out.ensureWritable(CompressBean.computeVar32Size(packageLength) + packageLength);
        CompressBean.writeVar32(out, packageLength);
        CompressBean.writeVar32(out, frame.messageType());
        CompressBean.writeVar32(out, frame.requestId());
        CompressBean.writeVar32(out, frame.messageId());
        CompressBean.writeVar64(out, frame.token());
        CompressBean.writeVar64(out, frame.userId());
        message.write(out);
    }


}
