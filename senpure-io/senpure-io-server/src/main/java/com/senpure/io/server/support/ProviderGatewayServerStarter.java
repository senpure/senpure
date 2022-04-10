package com.senpure.io.server.support;

import com.senpure.base.util.Spring;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.MessageFrame;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.protocol.bean.HandleMessage;
import com.senpure.io.server.protocol.bean.IdName;
import com.senpure.io.server.protocol.message.*;
import com.senpure.io.server.provider.ProviderMessageDecoderContext;
import com.senpure.io.server.provider.ProviderMessageExecutor;
import com.senpure.io.server.provider.ProviderMessageHandlerContext;
import com.senpure.io.server.provider.ProviderSendMessage;
import com.senpure.io.server.provider.gateway.Gateway;
import com.senpure.io.server.provider.gateway.GatewayManager;
import com.senpure.io.server.provider.gateway.ProviderGatewayServer;
import com.senpure.io.server.provider.handler.ProviderAskMessageHandler;
import com.senpure.io.server.provider.handler.ProviderMessageHandler;
import com.senpure.io.server.remoting.ChannelService;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProviderGatewayServerStarter implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServerProperties properties;
    private final List<ProviderGatewayServer> servers = new ArrayList<>();
    // private final Map<String, Long> failGatewayMap = new HashMap<>();
    private long lastLogTime = 0;

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private GatewayManager gatewayManager;
    @Resource
    private ProviderMessageHandlerContext handlerContext;
    @Resource
    private ProviderMessageDecoderContext decoderContext;
    @Resource
    private ProviderMessageExecutor messageExecutor;
    @Resource
    private TaskLoopGroup service;
    @Value("${server.port:0}")
    private int httpPort;

    public ProviderGatewayServerStarter(ServerProperties properties) {
        this.properties = properties;
    }

    @PreDestroy
    public void destroy() {
        for (ProviderGatewayServer server : servers) {
            server.destroy();
        }

    }

    private final List<HandleMessage> handleMessages = new ArrayList<>();
    private List<IdName> idNames = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) {
        List<Integer> ids = handlerContext.registerMessageIds();

        logger.debug("ids {}", ids.size());


        for (Integer id : ids) {
            HandleMessage handleMessage = new HandleMessage();
            handleMessage.setHandleMessageId(id);

            ProviderMessageHandler<?> handler = handlerContext.handler(id);
            if (handler != null) {
                if (handler instanceof ProviderAskMessageHandler) {
                    //避免编码出错
                    handleMessage.setDirect(false);
                    if (handler.direct()) {
                        logger.warn("{}实现了ProducerAskMessageHandler但是direct()返回true 修正为false", handler.getClass().getName());
                    }
                } else {
                    handleMessage.setDirect(true);
                }

                Message message = handler.newEmptyMessage();
                handleMessage.setMessageName(message.getClass().getName());
                handleMessages.add(handleMessage);
                MessageIdReader.relation(id, message.getClass().getSimpleName());
            }
        }
        ServerProperties.ProviderProperties providerProperties = properties.getProvider();
        ServerProperties.ProviderProperties.GatewayProperties providerGateway = properties.getProvider().getGateway();

        if (StringUtils.isNoneEmpty(providerProperties.getIdNamesPackage())) {
            idNames = MessageScanner.scan(providerProperties.getIdNamesPackage());

        }
        ServerProperties.GatewayProperties gatewayProperties = new ServerProperties.GatewayProperties();
        service.scheduleWithFixedDelay(() -> {
            try {
                boolean log = false;
                long now = System.currentTimeMillis();
                if (now - lastLogTime >= 60000) {
                    lastLogTime = now;
                    log = true;

                }
                if (providerGateway.getModel() == ServerProperties.ProviderProperties.GatewayProperties.MODEL.DIRECT) {
                    if (log) {
                        logger.debug("{} 直连模式 {}:{}", providerGateway.getName(), providerGateway.getHost(), providerGateway.getPort());
                    }

                    Gateway gateway = getGateway(providerGateway, providerGateway.getHost(), providerGateway.getPort());
                    if (gateway.isConnecting()) {
                        return;
                    }
                    if (gateway.getChannelSize() < providerGateway.getChannel() && now >= gateway.getNextConnectTime()) {
                        logger.debug("{} channel 数量 {}/{} {}", gateway.getRemoteServerKey(), gateway.getChannelSize(), providerGateway.getChannel(), gateway);

                        connect(gateway,providerGateway.getHost(),providerGateway.getPort(),providerProperties);
                    }
                } else {
                    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(providerGateway.getName());
                    if (log) {
                        logger.debug("{} 实例数量 {}", providerGateway.getName(), serviceInstances.size());
                        gatewayManager.report();
                    }
                    for (ServiceInstance instance : serviceInstances) {
                        boolean useDefault = false;
                        String portStr = instance.getMetadata().get(Constant.GATEWAY_METADATA_PROVIDER_PORT);
                        int port;
                        if (portStr == null) {
                            useDefault = true;
                            port = gatewayProperties.getProvider().getPort();
                        } else {
                            port = Integer.parseInt(portStr);
                        }
                        String gatewayKey = gatewayManager.getRemoteServerKey(instance.getHost(), port);
                        Gateway gateway = getGateway(providerGateway, instance.getHost(), port);

                        if (gateway.isConnecting()) {
                            continue;
                        }
                        if (gateway.getChannelSize() < providerGateway.getChannel() && now >= gateway.getNextConnectTime()) {
                            logger.debug("{} channel 数量 {}/{} {}", gatewayKey, gateway.getChannelSize(), providerGateway.getChannel(), gateway);
                            if (useDefault) {
                                logger.info("网关 [{}] {} {} 没有 没有配置provider socket端口,使用默认端口 {}", providerGateway.getName(), instance.getHost(), instance.getUri(), gatewayProperties.getProvider().getPort());
                            }
                            connect(gateway,instance.getHost(),port,providerProperties);

                        }
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }

        }, 2000, 50, TimeUnit.MILLISECONDS);
    }

    private Gateway getGateway(ServerProperties.ProviderProperties.GatewayProperties providerGateway,
                               String host, int port) {
        String gatewayKey = gatewayManager.getRemoteServerKey(host, port);
        Gateway gateway = gatewayManager.getGateway(gatewayKey);
        if (gateway == null) {
            gateway = new Gateway(service);
            if (providerGateway.getChannel() <= 1) {
                gateway.setChannelService(new ChannelService.SingleChannelService(gatewayKey));
            } else {
                gateway.setChannelService(new ChannelService.MultipleChannelService(gatewayKey));
            }
            gateway.setFutureService(messageExecutor);
            gateway.setRemoteServerKey(gatewayKey);
            gateway.verifyWorkable();
            gateway.setDefaultWaitSendTimeout(providerGateway.getMessageWaitSendTimeout());
            gateway = gatewayManager.addGateway(gateway);
        }
        return gateway;

    }

    private void connect(Gateway gateway, String host, int port, ServerProperties.ProviderProperties providerProperties) {
        gateway.setConnecting(true);
        ServerProperties.ProviderProperties.GatewayProperties providerGateway = properties.getProvider().getGateway();
        ProviderGatewayServer providerGatewayServer = new ProviderGatewayServer();
        providerGatewayServer.setGatewayManager(gatewayManager);
        providerGatewayServer.setProperties(properties);
        providerGatewayServer.setMessageExecutor(messageExecutor);
        providerGatewayServer.setDecoderContext(decoderContext);
        providerGatewayServer.setServerName(properties.getServerName());
        providerGatewayServer.setHttpPort(httpPort);
        providerGatewayServer.setReadableServerName(providerProperties.getReadableName());
        if (providerGatewayServer.start(host, port)) {
            Iterator<ProviderGatewayServer> iterator = servers.iterator();
            while (iterator.hasNext()) {
                ProviderGatewayServer server = iterator.next();
                if (server.isClosed()) {
                    iterator.remove();
                    server.destroy();
                }
            }
            servers.add(providerGatewayServer);
            Context context = new Context();
            context.gateway = gateway;
            context.server = providerGatewayServer;
            start(context);
            gateway.setStreakFailTimes(0);


        } else {
            logger.warn("{}  socket {}:{} 连接失败", providerGateway.getName(), host, port);
            gateway.streakFailTimesIncr();
            gateway.setNextConnectTime(System.currentTimeMillis() + providerGateway.getConnectFailInterval());
            gateway.setConnecting(false);
        }
    }

    private void start(Context context) {

        frameworkVerifyProvider(context);
    }

    private void frameworkVerifyProvider(Context context) {
        Gateway gateway = context.gateway;
        Channel channel = context.server.getChannel();

        if (properties.getProvider().isFrameworkVerifyProvider()) {
            //防止网关重启
            if (gateway.getChannelSize() > 0 && gateway.isFrameworkVerifyProviderPassed()) {
                frameworkVerify(context);
            } else {
                CSFrameworkVerifyProviderMessage message = new CSFrameworkVerifyProviderMessage();
                message.setServerName(properties.getServerName());
                message.setServerKey(ChannelAttributeUtil.getLocalServerKey(channel));
                message.setServerType(properties.getServerType());
                message.setServerOption(properties.getServerOption());
                MessageFrame frame = gatewayManager.createMessage(message, true);

                gateway.sendMessage(channel, frame, response -> {
                    if (response.isSuccess()) {
                        gateway.setFrameworkVerifyProviderPassed(true);
                        logger.info("向网关表明自己可以提供认证功能成功");
                        frameworkVerify(context);
                    } else {
                        logger.error("向网关表明自己可以提供认证功能失败");
                        logger.error(response.getMessage().toString());
                        channel.close();
                        error(context);
                    }
                });
            }
        } else {

            frameworkVerify(context);
        }


    }


    private void frameworkVerify(Context context) {
        Gateway gateway = context.gateway;
        Channel channel = context.server.getChannel();

        //每个channel都需要认证
        logger.info("{} 准备认证 {}", gateway.getRemoteServerKey(), channel);
        CSFrameworkVerifyMessage message = new CSFrameworkVerifyMessage();
        ServerProperties.Verify verify = properties.getVerify();
        message.setServerName(properties.getServerName());
        message.setServerKey(ChannelAttributeUtil.getLocalServerKey(channel));
        message.setServerType(properties.getServerType());
        message.setServerOption(properties.getServerOption());
        message.setUserName(verify.getUserName());
        message.setUserType(verify.getUserType());
        message.setPassword(verify.getPassword());
        message.setToken(verify.getToken());
        ProviderSendMessage frame = gatewayManager.createMessage(message, true);

        gateway.sendMessage(channel, frame, response -> {
            if (response.isSuccess()) {
                logger.info("框架内部认证成功");
                gateway.setFrameworkVerifyPassed(true);

                gateway.addChannel(channel);
                logger.debug("新增channel {} {}", context.gateway.getRemoteServerKey(), gateway.getChannelSize());
                registerProvider(context);
            } else {
                logger.error("框架认证失败");
                logger.error(response.getMessage().toString());
                channel.close();
                error(context);
            }
        });


    }

    public void registerProvider(Context context) {
        Gateway gateway = context.gateway;

        ProviderGatewayServer server = context.server;
        CSRegisterProviderMessage message = new CSRegisterProviderMessage();
        message.setServerName(properties.getServerName());
        message.setReadableServerName(server.getReadableServerName());
        message.setServerKey(ChannelAttributeUtil.getLocalServerKey(server.getChannel()));
        message.setServerType(properties.getServerType());
        message.setServerOption(properties.getServerOption());
        message.setMessages(handleMessages);
        ProviderSendMessage frame = gatewayManager.createMessage(message, true);
        logger.debug("向{}注册服务", ChannelAttributeUtil.getRemoteServerKey(server.getChannel()));
        for (HandleMessage handleMessage : handleMessages) {
            logger.debug(handleMessage.toString());
        }
        Channel channel = context.server.getChannel();
        gateway.sendMessage(frame, response -> {
            if (response.isSuccess()) {
                logger.info("{} 注册服务成功 {}", ChannelAttributeUtil.getLocalServerKey(channel), channel);
                SCRegisterProviderMessage scRegisterProviderMessage = response.getMessage();
                if (scRegisterProviderMessage.getMessage() != null) {
                    logger.info("{}", scRegisterProviderMessage.getMessage());

                }
            } else {
                logger.error("{} 注册服务失败 {}", ChannelAttributeUtil.getLocalServerKey(channel), channel);
                logger.error(response.getMessage().toString());
                Spring.exit();
            }
        });
        //
        if (gateway.getChannelSize() == 1) {
            registerIdNames(gateway, server.getChannel());
        }
        stop(context);

    }

    public void registerIdNames(Gateway gateway, Channel channel) {
        SCIdNameMessage message = new SCIdNameMessage();
        for (int i = 0; i < idNames.size(); i++) {
            if (i > 0 && i % 100 == 0) {
                registerIdNames(gateway, channel, message);
                message = new SCIdNameMessage();
            }
            message.getIdNames().add(idNames.get(i));
        }
        if (message.getIdNames().size() > 0) {
            registerIdNames(gateway, channel, message);
        }
    }

    private void registerIdNames(Gateway gateway, Channel channel, SCIdNameMessage message) {
        ProviderSendMessage frame = gatewayManager.createMessageByToken(0L, message);
        gateway.sendMessage(channel, frame);

    }

    private void stop(Context context) {

        context.gateway.setConnecting(false);
    }

    private void error(Context context) {
        Gateway gateway = context.gateway;
        gateway.streakFailTimesIncr();
        gateway.setNextConnectTime(System.currentTimeMillis() + properties.getProvider().getGateway().getConnectFailInterval());
        stop(context);
    }

    private static class Context {
        private ProviderGatewayServer server;
        private Gateway gateway;
    }
}
