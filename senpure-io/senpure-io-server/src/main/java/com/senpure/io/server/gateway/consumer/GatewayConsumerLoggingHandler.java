package com.senpure.io.server.gateway.consumer;

import com.senpure.io.server.MessageFrame;
import com.senpure.io.server.protocol.message.CSHeartMessage;
import com.senpure.io.server.protocol.message.SCHeartMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class GatewayConsumerLoggingHandler extends LoggingHandler {
    private final boolean skipHeart;

    public GatewayConsumerLoggingHandler(LogLevel level) {
        this(level, false);
    }

    public GatewayConsumerLoggingHandler(LogLevel level, boolean skipHeart) {
        super(level);
        this.skipHeart = skipHeart;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

        if (this.logger.isEnabled(this.internalLevel)) {
            if (msg instanceof MessageFrame) {
                MessageFrame frame = (MessageFrame) msg;
                if (!skipHeart || frame.messageId() != SCHeartMessage.MESSAGE_ID) {
                    this.logger.log(this.internalLevel, "{} {}", "WRITE: ", msg);
                }
            } else {
                this.logger.log(this.internalLevel, "{} {}", "WRITE: ", msg);
            }
        }

        ctx.write(msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (this.logger.isEnabled(this.internalLevel)) {
            if (msg instanceof MessageFrame) {
                MessageFrame frame = (MessageFrame) msg;
                if (!skipHeart || frame.messageId() != CSHeartMessage.MESSAGE_ID) {
                    this.logger.log(this.internalLevel, "{} {}", "RECEIVED: ", msg);
                }
            } else {
                this.logger.log(this.internalLevel, "{} {}", "RECEIVED: ", msg);
            }
        }
        ctx.fireChannelRead(msg);

    }
}
