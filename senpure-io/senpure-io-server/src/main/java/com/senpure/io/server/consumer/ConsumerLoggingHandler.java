package com.senpure.io.server.consumer;

import com.senpure.io.server.protocol.message.CSHeartMessage;
import com.senpure.io.server.protocol.message.SCHeartMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * ConsumerLoggingHandler
 *
 * @author senpure
 * @time 2019-08-06 14:59:59
 */
public class ConsumerLoggingHandler extends LoggingHandler {

    private final boolean outFormat;

    private final boolean inFormat;

    private final boolean skipHeart;

    public ConsumerLoggingHandler(LogLevel level, boolean inFormat, boolean outFormat) {
        this(level, inFormat, outFormat, false);
    }

    public ConsumerLoggingHandler(LogLevel level, boolean inFormat, boolean outFormat, boolean skipHeart) {
        super(level);
        this.inFormat = inFormat;
        this.outFormat = outFormat;
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
            if (msg instanceof ConsumerMessage) {
                ConsumerMessage frame = (ConsumerMessage) msg;
                if (!skipHeart || frame.messageId() != CSHeartMessage.MESSAGE_ID) {
                    if (outFormat) {
                        this.logger.log(this.internalLevel, "{} {}{}",
                                "WRITE", "\n", frame.message().toString(null));
                        //this.logger.log(this.internalLevel, this.format(ctx, ChannelAttributeUtil.getChannelPlayerStr(ctx.channel())+" WRITE", "\n"+((Message) msg).toString(null)));
                    } else {
                        this.logger.log(this.internalLevel, "{} {}", "WRITE: ", msg);
                    }
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
            if (msg instanceof ConsumerMessage) {
                ConsumerMessage frame = (ConsumerMessage) msg;
                if (!skipHeart || frame.messageId() != SCHeartMessage.MESSAGE_ID) {
                    if (inFormat) {
                        this.logger.log(this.internalLevel, "{} {}{}",
                                "RECEIVED", "\n", frame.message().toString(null));
                        // this.logger.log(this.internalLevel, this.format(ctx, ChannelAttributeUtil.getChannelPlayerStr(ctx.channel()) + " RECEIVED", "\n" + ((Message) msg).toString(null)));

                    } else {
                        this.logger.log(this.internalLevel, "{} {}", "RECEIVED: ", msg);
                    }
                }
            } else {
                this.logger.log(this.internalLevel, "{} {}", "RECEIVED: ", msg);
            }
        }
        ctx.fireChannelRead(msg);

    }
}
