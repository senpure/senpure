package com.senpure.io.server.provider.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageDecoder;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.provider.ProviderReceivedMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProviderGatewayMessageDecoder extends ByteToMessageDecoder {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageDecoderContext decoderContext;

    public ProviderGatewayMessageDecoder(MessageDecoderContext decoderContext) {
        this.decoderContext = decoderContext;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

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
            int maxIndex = in.readerIndex() + packageLength;
            int messageType = CompressBean.readVar32(in);
            int requestId = CompressBean.readVar32(in);
            int messageId = CompressBean.readVar32(in);
            long channelToken = CompressBean.readVar64(in);
            long userId = CompressBean.readVar64(in);
            MessageDecoder<?> decoder = decoderContext.decoder(messageId);
            if (decoder == null) {
                int headLength = CompressBean.computeVar32Size(messageType);
                headLength += CompressBean.computeVar32Size(requestId);
                headLength += CompressBean.computeVar32Size(messageId);
                headLength += CompressBean.computeVar64Size(channelToken);
                headLength += CompressBean.computeVar64Size(userId);
                int messageLen = packageLength - headLength;
                in.skipBytes(messageLen);
                logger.warn("没有找到消息解码程序{} token:{} userId:{}", MessageIdReader.read(messageId), channelToken, userId);
            } else {
                Message message = decoder.decode(in, maxIndex);
                ProviderReceivedMessage frame = new ProviderReceivedMessage(messageType);
                frame.setRequestId(requestId);
                frame.setMessageId(messageId);
                frame.setToken(channelToken);
                frame.setUserId(userId);
                frame.setMessage(message);
                out.add(frame);
            }

        }

    }


}
