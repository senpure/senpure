package com.senpure.io.server.support.autoconfigure;


import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.provider.MessageSender;
import com.senpure.io.server.provider.ProviderMessageExecutor;
import com.senpure.io.server.provider.ProviderMessageHandlerContext;
import com.senpure.io.server.provider.gateway.GatewayManager;
import com.senpure.io.server.support.ProviderGatewayServerStarter;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;

@ConditionalOnProperty(prefix = "server.io", value = "provider.model", havingValue = "GATEWAY", matchIfMissing = true)
@AutoConfigureAfter(ProviderAutoConfiguration.class)
public class ProviderGatewayAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServerProperties properties;
    private TaskLoopGroup taskLoopGroup;

    public ProviderGatewayAutoConfiguration(ServerProperties properties) {
        ServerProperties.ProviderProperties.GatewayProperties gateway = properties.getProvider().getGateway();
        //io *2 logic *1 综合1.5
        double size = Runtime.getRuntime().availableProcessors() * 1.5;
        int ioSize = (int) (size * 0.6);
        ioSize = Math.max(ioSize, 1);
        int logicSize = (int) (size * 0.4);
        logicSize = Math.max(logicSize, 1);
        if (gateway.getIoWorkThreadPoolSize() < 1) {
            gateway.setIoWorkThreadPoolSize(ioSize);
        }
        if (properties.getProvider().getExecutorThreadPoolSize() < 1) {
            properties.getProvider().setExecutorThreadPoolSize(logicSize);
        }
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(TaskLoopGroup.class)
    public TaskLoopGroup taskLoopGroup() {

        TaskLoopGroup service = new DefaultTaskLoopGroup(properties.getProvider().getExecutorThreadPoolSize(),
                new DefaultThreadFactory(properties.getServerName() + "-executor"));
        this.taskLoopGroup = service;
        return service;

    }


    @Bean
    public GatewayManager gatewayManager() {
        logger.info("bean gatewayManager");
        return new GatewayManager();
    }

    @Bean
    public ProviderMessageExecutor providerMessageExecutor(TaskLoopGroup service, MessageSender messageSender, ProviderMessageHandlerContext handlerContext) {

        return new ProviderMessageExecutor(service, messageSender, handlerContext);
    }

    @Bean
    public ProviderGatewayServerStarter providerGatewayServerStarter(ServerProperties properties) {
        return new ProviderGatewayServerStarter(properties);
    }

    @PreDestroy
    public void destroy() {
        if (taskLoopGroup != null) {
            taskLoopGroup.shutdownGracefully();
        }
    }
}
