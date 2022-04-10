package com.senpure.io.server.gateway.provider;

import com.senpure.base.util.Assert;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.GatewayMessageExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class GatewayProviderServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private String readableServerName = "网关服务器[provider]";

    private GatewayMessageExecutor messageExecutor;
    private ServerProperties.GatewayProperties properties;

    public boolean start() {
        Assert.notNull(messageExecutor);
        Assert.notNull(properties);
        Assert.notNull(properties.getProvider());
        ServerProperties.GatewayProperties.ProviderProperties provider = properties.getProvider();
        logger.debug("启动 {} provider 模块，监听端口号 {}", properties.getReadableName(), provider.getPort());
        readableServerName = properties.getReadableName() + "[provider][" + provider.getPort() + "]";
        SslContext sslCtx = null;
        // Configure SSL.
        if (provider.isSsl()) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (Exception e) {
                logger.error("使用ssl出错", e);
            }
        }
        try {
            // Configure the server.
            bossGroup = new NioEventLoopGroup(provider.getIoBossThreadPoolSize());
            workerGroup = new NioEventLoopGroup(provider.getIoWorkThreadPoolSize());
            ServerBootstrap b = new ServerBootstrap();
            SslContext finalSslCtx = sslCtx;
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            if (finalSslCtx != null) {
                                p.addLast(finalSslCtx.newHandler(ch.alloc()));
                            }
                            p.addLast(new GatewayProviderMessageDecoder());
                            p.addLast(new GatewayProviderMessageEncoder());
                            p.addLast(new GatewayProviderLoggingHandler(LogLevel.DEBUG, provider.isSkipHeart()));
                            if (provider.isEnableHeartCheck()) {
                                p.addLast(new IdleStateHandler(provider.getReaderIdleTime(), 0L, 0L, TimeUnit.MILLISECONDS));
                            }
                            p.addLast(new GatewayProviderServerHandler(messageExecutor));

                        }
                    });
            // Start the server.
            b.bind(provider.getPort()).sync();
            logger.info("{} 启动完成", getReadableServerName());
        } catch (Exception e) {
            logger.error("启动 " + getReadableServerName() + " 失败", e);
            destroy();
            return false;
        }

        return true;
    }


    public void destroy() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        logger.info("关闭{}并释放资源 ", getReadableServerName());

    }

    public String getReadableServerName() {
        return readableServerName;
    }


    public void setMessageExecutor(GatewayMessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }


    public void setProperties(ServerProperties.GatewayProperties properties) {
        this.properties = properties;
    }
}
