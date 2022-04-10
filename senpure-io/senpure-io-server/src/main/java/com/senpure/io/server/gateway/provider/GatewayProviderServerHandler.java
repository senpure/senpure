package com.senpure.io.server.gateway.provider;


import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayMessageExecutor;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GatewayProviderServerHandler extends SimpleChannelInboundHandler<GatewayReceiveProviderMessage> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GatewayMessageExecutor messageExecutor;

    //  private SourceOffline sourceOffline;

    public GatewayProviderServerHandler(GatewayMessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GatewayReceiveProviderMessage msg) {
        messageExecutor.execute(ctx.channel(), msg);
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        //解决强断的错误 远程主机强迫关闭了一个现有的连接
//        ctx.flush();
//    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
        logger.debug("{} {} {} 断开连接", serverName, ChannelAttributeUtil.getRemoteServerKey(channel), channel);
        if (serverName != null) {
            ProviderManager providerManager = messageExecutor.getProviderManager(serverName);
            if (providerManager != null) {
                messageExecutor.execute(() -> providerManager.providerOffLine(channel));
            }

        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            logger.info("服务器{} :{}  :{} 心跳失败", channel, ChannelAttributeUtil.getRemoteServerName(channel), ChannelAttributeUtil.getRemoteServerKey(channel));
            ctx.close();
            return;
        }
        super.userEventTriggered(ctx, evt);
    }
}
