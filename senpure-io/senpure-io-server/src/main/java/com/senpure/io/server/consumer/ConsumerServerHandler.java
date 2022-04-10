package com.senpure.io.server.consumer;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.protocol.message.CSHeartMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsumerServerHandler extends SimpleChannelInboundHandler<ConsumerMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    // private RemoteServerManager remoteServerManager;
    private ConsumerMessageExecutor messageExecutor;

    private ProviderManager providerManager;


    public ConsumerServerHandler(ConsumerMessageExecutor messageExecutor, ProviderManager providerManager) {
        this.providerManager = providerManager;
        this.messageExecutor = messageExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConsumerMessage frame) throws Exception {

        messageExecutor.execute(ctx.channel(), frame);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String remoteServerKey = ChannelAttributeUtil.getRemoteServerKey(ctx.channel());
        logger.info("{} :{} 远程服务器连接断开 ", remoteServerKey, ctx.channel());
        providerManager.getDefaultFrameSender().removeChannel(ctx.channel());
        // providerManager.getRemoteServer().
//        remoteServerManager.getRemoteServerChannelManager(ChannelAttributeUtil.
//                getRemoteServerKey(ctx.channel())).removeChannel(ctx.channel());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        //ctx.flush();
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            if (channel.isWritable()) {
                logger.trace("维持服务器心跳{} : {}", ChannelAttributeUtil.getRemoteServerKey(channel), channel);
                CSHeartMessage heartMessage = new CSHeartMessage();
                ConsumerMessage frame = new ConsumerMessage(heartMessage);
                frame.setRequestId(0);
                channel.writeAndFlush(frame);
            } else {
                logger.warn("服务器心跳失败并且channel不可用{}:{}", ChannelAttributeUtil.getRemoteServerKey(channel), channel);
                channel.close();
            }

            return;
        }
        super.userEventTriggered(ctx, evt);
    }
}
