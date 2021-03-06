package com.senpure.io.server.support;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.protocol.bean.HandleMessage;
import com.senpure.io.server.protocol.bean.IdName;
import com.senpure.io.server.protocol.message.SCIdNameMessage;
import com.senpure.io.server.protocol.message.SCRegServerHandleMessageMessage;
import com.senpure.io.server.provider.*;
import com.senpure.io.server.provider.handler.ProviderAskMessageHandler;
import com.senpure.io.server.provider.handler.ProviderMessageHandler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProviderServerStarter implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServerProperties properties;
    private final List<ProviderServer> servers = new ArrayList<>();
    private final Map<String, Long> failGatewayMap = new HashMap<>();
    private long lastLogTime = 0;

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private GatewayManager gatewayManager;
    @Resource
    private ProviderMessageHandlerContext handlerContext;
    @Resource
    private MessageDecoderContext decoderContext;
    @Resource
    private ProviderMessageExecutor messageExecutor;
    @Resource
    private TaskLoopGroup service;
    @Value("${server.port:8080}")
    private int httpPort;

    public ProviderServerStarter(ServerProperties properties) {
        this.properties = properties;
    }

    @PreDestroy
    public void destroy() {
        for (ProviderServer server : servers) {
            server.destroy();
        }

    }

    @Override
    public void run(ApplicationArguments args) {
        List<Integer> ids = handlerContext.registerMessageIds();

        logger.debug("ids {}",ids.size());
        List<HandleMessage> handleMessages = new ArrayList<>();

        for (Integer id : ids) {
            HandleMessage handleMessage = new HandleMessage();
            handleMessage.setHandleMessageId(id);

            ProviderMessageHandler<?> handler = handlerContext.handler(id);
            if (handler != null) {
                if (handler instanceof ProviderAskMessageHandler) {
                    //??????????????????
                    handleMessage.setDirect(false);
                    if (handler.direct()) {
                        logger.warn("{}?????????ProducerAskMessageHandler??????direct()??????true ?????????false", handler.getClass().getName());
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
        ServerProperties.Provider provider = properties.getProvider();
        List<IdName> idNames = null;
        if (StringUtils.isNoneEmpty(provider.getIdNamesPackage())) {
            idNames = MessageScanner.scan(provider.getIdNamesPackage());

        }
        ServerProperties.Gateway gateway = new ServerProperties.Gateway();
        List<IdName> finalIdNames = idNames;
        service.scheduleWithFixedDelay(() -> {
            try {
                boolean log=false;
                long now = System.currentTimeMillis();
                if (now - lastLogTime >= 60000) {
                    lastLogTime = now;
                    log=true;

                }
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(provider.getGatewayName());
                if (log) {
                    logger.debug("{} ???????????? {}",provider.getGatewayName(),serviceInstances.size());
                    gatewayManager.report();
                }
                for (ServiceInstance instance : serviceInstances) {
                    boolean useDefault = false;
                    String portStr = instance.getMetadata().get(Constant.GATEWAY_METADATA_SC_PORT);

                    int port;
                    if (portStr == null) {
                        useDefault = true;
                        port = gateway.getScPort();
                    } else {
                        port = Integer.parseInt(portStr);
                    }
                    String gatewayKey = gatewayManager.getGatewayKey(instance.getHost(), port);
                    GatewayChannelManager gatewayChannelManager = gatewayManager.getGatewayChannelManager(gatewayKey);
                    if (gatewayChannelManager == null) {
                        gatewayChannelManager = new GatewayChannelManager(gatewayKey, provider.getGatewayChannel(),service);
                        gatewayChannelManager = gatewayManager.addGatewayChannelManager(gatewayChannelManager);
                    }
                    if (gatewayChannelManager.isConnecting()) {
                        continue;
                    }
                    if (gatewayChannelManager.getChannelSize() < provider.getGatewayChannel()) {
                        Long lastFailTime = failGatewayMap.get(gatewayKey);
                        boolean start = false;
                        if (lastFailTime == null) {
                            start = true;
                        } else {
                            if (now - lastFailTime >= provider.getConnectFailInterval()) {
                                start = true;
                            }
                        }
                        if (start) {
                            if (useDefault) {
                                logger.info("?????? [{}] {} {} ?????? ????????????sc socket??????,?????????????????? {}", provider.getGatewayName(), instance.getHost(), instance.getUri(), gateway.getScPort());
                            }
                            gatewayChannelManager.setConnecting(true);
                            ProviderServer providerServer = new ProviderServer();
                            providerServer.setGatewayManager(gatewayManager);
                            providerServer.setProperties(provider);
                            providerServer.setMessageExecutor(messageExecutor);
                            providerServer.setDecoderContext(decoderContext);

                            providerServer.setServerName(properties.getName());
                            providerServer.setHttpPort(httpPort);
                            providerServer.setReadableServerName(provider.getReadableName());
                            if (providerServer.start(instance.getHost(), port)) {
                                servers.add(providerServer);
                                registerProvider(providerServer, handleMessages);
                                if (gatewayChannelManager.getChannelSize() == 0) {
                                    gatewayChannelManager.setDefaultMessageRetryTimeLimit(provider.getMessageRetryTimeLimit());
                                    if (finalIdNames != null && finalIdNames.size() > 0) {
                                        registerIdNames(providerServer, finalIdNames);
                                    }
                                }
                                //??????
                                gatewayChannelManager.addChannel(providerServer.getChannel());

                            } else {
                                logger.warn("{}  socket {}:{} ????????????",provider.getGatewayName(),instance.getHost(),port);
                                failGatewayMap.put(gatewayKey, now);
                            }
                            gatewayChannelManager.setConnecting(false);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("",e);
            }

        }, 2000, 50, TimeUnit.MILLISECONDS);
    }

    public void registerProvider(ProviderServer server, List<HandleMessage> handleMessages) {
        SCRegServerHandleMessageMessage message = new SCRegServerHandleMessageMessage();
        message.setServerName(properties.getName());
        message.setReadableServerName(server.getReadableServerName());
        message.setServerKey(ChannelAttributeUtil.getLocalServerKey(server.getChannel()));
        message.setMessages(handleMessages);
        ProviderSendMessage frame = gatewayManager.createMessageByToken(0L,message);


        logger.debug("???{}????????????", ChannelAttributeUtil.getRemoteServerKey(server.getChannel()));
        for (HandleMessage handleMessage : handleMessages) {
            logger.debug(handleMessage.toString());
        }
        server.getChannel().writeAndFlush(frame);
    }

    public void registerIdNames(ProviderServer server, List<IdName> idNames) {
        SCIdNameMessage message = new SCIdNameMessage();
        for (int i = 0; i < idNames.size(); i++) {
            if (i > 0 && i % 100 == 0) {
                registerIdNames(server, message);
                message = new SCIdNameMessage();
            }
            message.getIdNames().add(idNames.get(i));
        }
        if (message.getIdNames().size() > 0) {
            registerIdNames(server, message);
        }
    }

    private void registerIdNames(ProviderServer server, SCIdNameMessage message) {
        ProviderSendMessage frame = gatewayManager.createMessageByToken(0L, message);

        server.getChannel().writeAndFlush(frame);
    }
}
