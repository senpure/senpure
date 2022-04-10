package com.senpure.io.server.provider.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.provider.ProviderLoggingHandler;
import com.senpure.io.server.provider.ProviderMessageExecutor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class ProviderGatewayServer {
    protected static Logger logger = LoggerFactory.getLogger(ProviderGatewayServer.class);


    private static EventLoopGroup group;
    private static Bootstrap bootstrap;
    private static final Object groupLock = new Object();

    private static int serverRefCont = 0;
    private static int firstPort;
    private ServerProperties properties;


    private ChannelFuture channelFuture;
    private String serverName = "ProviderServer";
    private String readableServerName = "ProviderServer";
    private boolean setReadableServerName = false;
    private ProviderMessageExecutor messageExecutor;
    private int httpPort = 0;
    private boolean addLoggingHandler = true;
    private Channel channel;
    private GatewayManager gatewayManager;
    private MessageDecoderContext decoderContext;

    private volatile boolean closed;

    public final boolean start(String remoteHost, int remotePort) {

        Assert.notNull(gatewayManager);
        Assert.notNull(properties);

        Assert.notNull(messageExecutor);
        Assert.notNull(decoderContext);
        // Configure SSL.
        ServerProperties.ProviderProperties providerProperties = properties.getProvider();
        ServerProperties.ProviderProperties.GatewayProperties gateway = providerProperties.getGateway();
        if (group == null || group.isShuttingDown() || group.isShutdown()) {
            synchronized (groupLock) {
                if (group == null || group.isShuttingDown() || group.isShutdown()) {
                    group = new NioEventLoopGroup(gateway.getIoWorkThreadPoolSize());
                    SslContext sslCtx = null;
                    try {
                        if (gateway.isSsl()) {
                            SelfSignedCertificate ssc = new SelfSignedCertificate();
                            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                        }
                    } catch (Exception e) {
                        logger.error("使用ssl出错", e);
                    }
                    bootstrap = new Bootstrap();
                    SslContext finalSslCtx = sslCtx;
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) {
                                    ChannelPipeline p = ch.pipeline();
                                    if (finalSslCtx != null) {
                                        p.addLast(finalSslCtx.newHandler(ch.alloc(), remoteHost, remotePort));
                                    }
                                    p.addLast(new ProviderGatewayMessageDecoder(decoderContext));
                                    p.addLast(new ProviderGatewayMessageEncoder());
                                    if (addLoggingHandler) {

                                        p.addLast(new ProviderLoggingHandler(LogLevel.DEBUG, gateway.isInFormat(), gateway.isInFormat(), gateway.isSkipHeart()));
                                    }
                                    if (gateway.isEnableHeartCheck()) {
                                        p.addLast(new IdleStateHandler(0, gateway.getWriterIdleTime(), 0, TimeUnit.MILLISECONDS));
                                    }
                                    p.addLast(new ProviderGatewayServerHandler(messageExecutor, gatewayManager));
                                }
                            });

                }
            }
        }
        // Start the client.
        try {
            logger.debug("启动{}，网关地址 {}", providerProperties.getReadableName(), remoteHost + ":" + remotePort);
            readableServerName = providerProperties.getReadableName() + "->[" + remoteHost + ":" + remotePort + "]";
            channelFuture = bootstrap.connect(remoteHost, remotePort).sync();
            channel = channelFuture.channel();
            channel.closeFuture().addListener((ChannelFutureListener) channelFuture -> {

                logger.info("修改closed -> true");
                closed = true;
            });
            synchronized (groupLock) {
                serverRefCont++;
            }
            InetSocketAddress address = (InetSocketAddress) channel.localAddress();

            int localPort = address.getPort();
            markFirstPort(localPort);

            String gatewayKey = gatewayManager.getRemoteServerKey(remoteHost, remotePort);
//            String path;
//            if (AppEvn.classInJar(AppEvn.getStartClass())) {
//                path = AppEvn.getClassPath(AppEvn.getStartClass());
//            } else {
//                path = AppEvn.getClassRootPath();
//            }
            String serverKey = properties.getServerKey();
            if (!StringUtils.hasText(serverKey)) {
                serverKey = serverName + " " + address.getAddress().getHostAddress() + ":" + (httpPort > 0 ? httpPort : firstPort);

            }
            ChannelAttributeUtil.setRemoteServerKey(channel, gatewayKey);
            ChannelAttributeUtil.setLocalServerKey(channel, serverKey);
            logger.info("{}启动完成 localServerKey {} address {}", getReadableServerName(), serverKey, address);
        } catch (Exception e) {
            logger.error("启动" + getReadableServerName() + " 失败", e);
            destroy();
            return false;
        }
        return true;

    }

    public void setDecoderContext(MessageDecoderContext decoderContext) {
        this.decoderContext = decoderContext;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getReadableServerName() {
        return readableServerName;
    }


    public void setServerName(String serverName) {
        this.serverName = serverName;
        if (!setReadableServerName) {
            readableServerName = serverName;
        }
    }


    public void setReadableServerName(String readableServerName) {
        this.readableServerName = readableServerName;
        setReadableServerName = true;
    }

    public void setHttpPort(int httpPort) {

        this.httpPort = httpPort;
    }

    public boolean isAddLoggingHandler() {
        return addLoggingHandler;
    }

    public void setAddLoggingHandler(boolean addLoggingHandler) {
        this.addLoggingHandler = addLoggingHandler;
    }


    public void setGatewayManager(GatewayManager gatewayManager) {
        this.gatewayManager = gatewayManager;
    }

    public void setMessageExecutor(ProviderMessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    public void setProperties(ServerProperties properties) {
        this.properties = properties;
    }

    private static synchronized void markFirstPort(int port) {
        if (firstPort > 0) {
            return;
        }
        firstPort = port;
    }

    public void destroy() {
        if (channelFuture != null) {
            channelFuture.channel().close();
            synchronized (groupLock) {
                logger.debug("{}关闭 channel{} 并释放资源 ", channel, getReadableServerName());
                serverRefCont--;
            }
        }

        tryDestroyGroup(getReadableServerName());
    }

    public boolean isClosed() {
        return closed;
    }

    private synchronized static void tryDestroyGroup(String readableServerName) {
        synchronized (groupLock) {
            if (serverRefCont == 0) {
                if (group != null && (!group.isShutdown() | !group.isShuttingDown())) {
                    logger.debug("{} 关闭 group 并释放资源 ", readableServerName);
                    group.shutdownGracefully();
                }
            }
        }

    }
}
