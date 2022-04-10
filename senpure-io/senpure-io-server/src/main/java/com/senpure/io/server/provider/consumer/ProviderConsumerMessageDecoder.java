package com.senpure.io.server.provider.consumer;


import com.senpure.base.util.Assert;
import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.MessageDecoder;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.provider.ProviderReceivedMessage;
import com.senpure.io.server.provider.ProviderSendMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ProviderConsumerMessageDecoder extends ByteToMessageDecoder {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    private final MessageDecoderContext decoderContext;

    public ProviderConsumerMessageDecoder(MessageDecoderContext decoderContext) {
        this.decoderContext = decoderContext;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        int preIndex = in.readerIndex();
        int packageLength = CompressBean.tryReadVar32(in);
        int readerIndex = in.readerIndex();
        if (preIndex == readerIndex) {
            return;
        }
        if (packageLength == 0) {
            Assert.error("错误，数据包长度不能为0");
        }
        if (packageLength > in.readableBytes()) {
            this.logger.trace("数据不够一个数据包 packageLength ={} ,readableBytes={}", packageLength, in.readableBytes());
            in.resetReaderIndex();
        } else {
            int maxIndex = readerIndex + packageLength;
            int requestId = CompressBean.readVar32(in);
            int messageId = CompressBean.readVar32(in);

            MessageDecoder<?> decoder = decoderContext.decoder(messageId);
            if (decoder == null) {
                int headSize = CompressBean.computeVar32Size(requestId) + CompressBean.computeVar32Size(messageId);
                int messageLength = packageLength - headSize;
                in.skipBytes(messageLength);
                logger.warn("没有找到消息解码程序 messageId {}", messageId);

                Channel channel = ctx.channel();
                SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                errorMessage.setCode(Constant.ERROR_NOT_HANDLE_REQUEST);
                errorMessage.getArgs().add(String.valueOf(messageId));
                errorMessage.setMessage("服务器没有处理程序:" + MessageIdReader.read(messageId));
                ProviderSendMessage frame = new ProviderSendMessage(errorMessage);
                frame.setRequestId(requestId);
                frame.setToken(ChannelAttributeUtil.getToken(channel));
                channel.writeAndFlush(frame);

            } else {
                try {
                    Message message = decoder.decode(in,maxIndex);
                    ProviderReceivedMessage frame = new ProviderReceivedMessage(1);
                    frame.setRequestId(requestId);
                    frame.setMessageId(messageId);
                    Channel channel = ctx.channel();
                    long token = ChannelAttributeUtil.getToken(channel);
                    Long userId = ChannelAttributeUtil.getUserId(channel);
                    if (userId != null) {
                        frame.setUserId(userId);
                    }
                    frame.setToken(token);
                    frame.setMessage(message);
                    out.add(frame);
                } catch (Exception e) {
                    ctx.close();
                    logger.debug("二进制转换为消息失败 messageId {}", messageId);
                    logger.error("error", e);
                }
            }

        }
    }


}
