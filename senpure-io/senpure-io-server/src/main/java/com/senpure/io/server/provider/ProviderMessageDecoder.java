package com.senpure.io.server.provider;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageDecoder;
import com.senpure.io.server.MessageDecoderContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProviderMessageDecoder extends ByteToMessageDecoder {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageDecoderContext decoderContext;

    public ProviderMessageDecoder(MessageDecoderContext decoderContext) {
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
            int requestId =CompressBean.readVar32(in);
            int messageId = CompressBean.readVar32(in);
            long channelToken = CompressBean.readVar64(in);
            long userId = CompressBean.readVar64(in);

            MessageDecoder<?> decoder = decoderContext.decoder(messageId);
            Message message=  decoder.decode(in, maxIndex);

            ProviderReceiveMessage frame = new ProviderReceiveMessage();
            frame.setRequestId(requestId);
            frame.setMessageId(messageId);
            frame.setToken(channelToken);
            frame.setUserId(userId);
            if (message == null) {
                int headSize = CompressBean.computeVar32Size(requestId);
                headSize += CompressBean.computeVar32Size(messageId);

                headSize += CompressBean.computeVar64Size(channelToken);
                headSize +=CompressBean.computeVar64Size(userId);
                int messageLen = packageLength - headSize;
                in.skipBytes(messageLen);
                logger.warn("没有找到消息解码程序{} token:{} userId:{}", channelToken, messageId, userId);
            }
            else {
             //   message.read(in, maxIndex);
                frame.setMessage(message);
            }
            out.add(frame);
        }

    }

}
