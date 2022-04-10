package com.senpure.io.server.consumer;


import com.senpure.base.util.Assert;
import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageDecoder;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

;


public class ConsumerMessageDecoder extends ByteToMessageDecoder {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageDecoderContext decoderContext;

    public ConsumerMessageDecoder(MessageDecoderContext decoderContext) {
        this.decoderContext = decoderContext;
    }

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
            int maxIndex = in.readerIndex() + packageLength;
            int messageType = CompressBean.readVar32(in);
            int requestId = CompressBean.readVar32(in);
            int messageId = CompressBean.readVar32(in);

            MessageDecoder<?> decoder = decoderContext.decoder(messageId);
            if (decoder == null) {
                int headSize =
                        CompressBean.computeVar32Size(messageType) +
                                CompressBean.computeVar32Size(requestId) + CompressBean.computeVar32Size(messageId);
                int messageLength = packageLength - headSize;
                in.skipBytes(messageLength);
                logger.warn("没有找到消息解码程序 messageId {} skipLength {}", MessageIdReader.read(messageId),messageLength);
            } else {
                try {
                    Message message = decoder.decode(in, maxIndex);
                    ConsumerMessage frame = new ConsumerMessage(message);
                    //远程messageType 可能不同步
                    frame.setRequestId(requestId);
                    out.add(frame);
                } catch (Exception e) {
                    ctx.close();
                    logger.debug("二进制转换为消息失败 messageId {}", MessageIdReader.read(messageId));
                    logger.error("error", e);
                }
            }
        }
    }


}
