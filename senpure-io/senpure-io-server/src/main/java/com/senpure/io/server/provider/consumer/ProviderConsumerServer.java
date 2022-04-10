package com.senpure.io.server.provider.consumer;

import com.senpure.base.util.Assert;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.provider.ProviderLoggingHandler;
import com.senpure.io.server.provider.ProviderMessageExecutor;
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

public class ProviderConsumerServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    private ServerProperties.ProviderProperties properties;


    private String serverName = "ProviderServer";
    private String readableServerName = "ProviderServer";
    private boolean setReadableServerName = false;
    private ProviderMessageExecutor messageExecutor;
    private boolean addLoggingHandler = true;

    private MessageDecoderContext decoderContext;

    private ConsumerManager consumerManager;

    public boolean start() {
        Assert.notNull(messageExecutor);
        Assert.notNull(consumerManager);
        Assert.notNull(properties);
        Assert.notNull(properties.getConsumer());
        Assert.notNull(decoderContext);
        ServerProperties.ProviderProperties.ConsumerProperties consumer = properties.getConsumer();
        logger.debug("启动 {} ，监听端口号 {}", properties.getReadableName(), consumer.getPort());
        readableServerName = properties.getReadableName() + "[SC][" + consumer.getPort() + "]";
        SslContext sslCtx = null;
        if (consumer.isSsl()) {
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
                            p.addLast(new ProviderConsumerMessageDecoder(decoderContext));
                            p.addLast(new ProviderConsumerMessageEncoder());
                            if (addLoggingHandler) {


                                p.addLast(new ProviderLoggingHandler(LogLevel.DEBUG, consumer.isInFormat(), consumer.isOutFormat(), consumer.isSkipHeart()));

                            }
                            if (consumer.isEnableHeartCheck()) {
                                p.addLast(new IdleStateHandler(consumer.getReaderIdleTime(), 0L, 0L, TimeUnit.MILLISECONDS));
                            }
                            p.addLast(new ProviderConsumerServerHandler(messageExecutor, consumerManager));

                        }
                    });
            // Start the server.
            b.bind(consumer.getPort()).sync();
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

    public void setProperties(ServerProperties.ProviderProperties properties) {
        this.properties = properties;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setReadableServerName(String readableServerName) {
        this.readableServerName = readableServerName;
    }

    public void setSetReadableServerName(boolean setReadableServerName) {
        this.setReadableServerName = setReadableServerName;
    }

    public void setMessageExecutor(ProviderMessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    public void setAddLoggingHandler(boolean addLoggingHandler) {
        this.addLoggingHandler = addLoggingHandler;
    }

    public void setDecoderContext(MessageDecoderContext decoderContext) {
        this.decoderContext = decoderContext;
    }

    public void setClientManager(ConsumerManager consumerManager) {
        this.consumerManager = consumerManager;
    }
}
