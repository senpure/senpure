package com.senpure.io.server.gateway.consumer;

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


public class GatewayConsumerServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private String readableName = "网关服务器[CS]";
    private GatewayMessageExecutor messageExecutor;
    private ServerProperties.GatewayProperties properties;

    public boolean start() {
        Assert.notNull(messageExecutor);
        Assert.notNull(properties);
        Assert.notNull(properties.getConsumer());
        ServerProperties.GatewayProperties.ConsumerProperties consumer = properties.getConsumer();
        logger.info("启动 {} consumer模块，监听端口号 {}", properties.getReadableName(), properties.getConsumer().getPort());
        readableName = properties.getReadableName() + "[consumer][" + properties.getConsumer().getPort() + "]";
        // Configure SSL.
        SslContext sslCtx = null;
        if (properties.getConsumer().isSsl()) {
            try {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } catch (Exception e) {
                logger.error("使用ssl出错", e);
            }
        }
        try {
            // Configure the server.
            bossGroup = new NioEventLoopGroup(consumer.getIoBossThreadPoolSize());
            workerGroup = new NioEventLoopGroup(consumer.getIoWorkThreadPoolSize());
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
                            p.addLast(new GatewayConsumerMessageDecoder());
                            p.addLast(new GatewayConsumerMessageEncoder());
                            p.addLast(new GatewayConsumerLoggingHandler(LogLevel.DEBUG, consumer.isSkipHeart()));
                            if (consumer.isEnableHeartCheck()) {
                                p.addLast(new IdleStateHandler(consumer.getReaderIdleTime(), 0L, 0L, TimeUnit.MILLISECONDS));
                            }
                            p.addLast(new GatewayConsumerServerHandler(messageExecutor));

                        }
                    });
            // Start the server.
            b.bind(consumer.getPort()).sync();
            logger.info("{} 启动完成", getReadableName());
        } catch (Exception e) {
            logger.error("启动 " + getReadableName() + " 失败", e);
            destroy();
            return false;
        }
        return true;
    }


    private String getReadableName() {
        return readableName;
    }


    public void setMessageExecutor(GatewayMessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }


    public void setProperties(ServerProperties.GatewayProperties properties) {
        this.properties = properties;
    }

    public void destroy() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        logger.debug("关闭{}并释放资源 ", readableName);

    }


}
