package com.senpure.io.server.provider.gateway;


import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.protocol.message.CSHeartMessage;
import com.senpure.io.server.provider.ProviderMessageExecutor;
import com.senpure.io.server.provider.ProviderReceivedMessage;
import com.senpure.io.server.provider.ProviderSendMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class ProviderGatewayServerHandler extends SimpleChannelInboundHandler<ProviderReceivedMessage> {


    private final ProviderMessageExecutor messageExecutor;
    private final GatewayManager gatewayManager;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ProviderGatewayServerHandler(ProviderMessageExecutor messageExecutor, GatewayManager gatewayManager) {
        this.messageExecutor = messageExecutor;
        this.gatewayManager = gatewayManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProviderReceivedMessage msg) {
        messageExecutor.execute(ctx.channel(), msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("{} :{} 网关连接断开 ", ChannelAttributeUtil.getRemoteServerKey(ctx.channel()), ctx.channel());
        Gateway gateway = gatewayManager.getGateway(ChannelAttributeUtil.getRemoteServerKey(ctx.channel()));
        if (gateway != null) {
            gateway.removeChannel(ctx.channel());
        } else {
            logger.error("{} :{} 网关连接断开 error ", ChannelAttributeUtil.getRemoteServerKey(ctx.channel()), ctx.channel());
        }
        // Objects.requireNonNull(gatewayManager.getGatewayChannelServer(ChannelAttributeUtil.getRemoteServerKey(ctx.channel()))).removeChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //解决强断的错误 远程主机强迫关闭了一个现有的连接
        ctx.flush();

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            if (channel.isWritable()) {
                logger.trace("维持网关心跳{} : {}", ChannelAttributeUtil.getRemoteServerKey(channel), channel);
                CSHeartMessage heartMessage = new  CSHeartMessage();
                ProviderSendMessage frame=  gatewayManager.createMessageByToken(0L, heartMessage);
                channel.writeAndFlush(frame);
            } else {
                logger.warn("网关心跳失败并且channel不可用{}:{}", ChannelAttributeUtil.getRemoteServerKey(channel), channel);
                channel.close();
            }

            return;
        }
        super.userEventTriggered(ctx, evt);
    }
}
