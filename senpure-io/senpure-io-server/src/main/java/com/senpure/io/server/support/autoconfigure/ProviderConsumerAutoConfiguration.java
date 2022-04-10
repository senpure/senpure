package com.senpure.io.server.support.autoconfigure;


import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.provider.MessageSender;
import com.senpure.io.server.provider.ProviderMessageHandlerContext;
import com.senpure.io.server.provider.consumer.ConsumerManager;
import com.senpure.io.server.provider.ProviderMessageExecutor;
import com.senpure.io.server.support.ProviderConsumerServerStarter;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;

@ConditionalOnProperty(prefix = "server.io", value = "provider.model", havingValue = "CONSUMER")
@AutoConfigureAfter(ProviderAutoConfiguration.class)
public class ProviderConsumerAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServerProperties properties;
    private TaskLoopGroup taskLoopGroup;

    public ProviderConsumerAutoConfiguration(ServerProperties properties) {
        ServerProperties.ProviderProperties.ConsumerProperties consumer = properties.getProvider().getConsumer();
        //io *2 logic *1 综合1.5
        double size = Runtime.getRuntime().availableProcessors() * 1.5;
        int ioSize = (int) (size * 0.6);
        ioSize = Math.max(ioSize, 1);
        int logicSize = (int) (size * 0.4);
        logicSize = Math.max(logicSize, 1);
        if (consumer.getIoWorkThreadPoolSize() < 1) {
            consumer.setIoWorkThreadPoolSize(ioSize);
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
    public ProviderMessageExecutor providerMessageExecutor(TaskLoopGroup service, MessageSender messageSender, ProviderMessageHandlerContext handlerContext) {

        return new ProviderMessageExecutor(service, messageSender, handlerContext);
    }

    @Bean
    public ConsumerManager consumerManager(ProviderMessageExecutor messageExecutor) {
        return new ConsumerManager(messageExecutor);
    }

    @Bean
    public ProviderConsumerServerStarter providerConsumerServerStarter() {
        return new ProviderConsumerServerStarter();
    }

    @PreDestroy
    public void destroy() {
        if (taskLoopGroup != null) {
            taskLoopGroup.shutdownGracefully();
        }
    }
}
