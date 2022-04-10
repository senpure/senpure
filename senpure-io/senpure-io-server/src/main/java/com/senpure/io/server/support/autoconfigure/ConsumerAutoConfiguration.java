package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.util.Assert;
import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.consumer.*;
import com.senpure.io.server.consumer.handler.ConsumerMessageHandler;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCHeartMessage;
import com.senpure.io.server.protocol.message.SCSuccessMessage;
import com.senpure.io.server.support.ConsumerServerStarter;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * ConsumerAutoConfiguration
 *
 * @author senpure
 * @time 2019-03-01 11:46:50
 */


public class ConsumerAutoConfiguration {
    private static final ServerProperties.ConsumerProperties DEFAULT_CONSUMER_PROPERTIES = new ServerProperties.ConsumerProperties();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServerProperties properties;

    private TaskLoopGroup taskLoopGroup;


    public ConsumerAutoConfiguration(Environment env, ServerProperties properties) {
        check(env, properties);
        this.properties = properties;
    }

    private void check(Environment env, ServerProperties properties) {
        if (!StringUtils.hasText(properties.getServerName())) {
            String name = env.getProperty("spring.application.name");
            if (StringUtils.hasText(name)) {
                logger.debug("使用 name {}", name);
                properties.setServerName(name);
            } else {
                logger.warn("spring.application.name 值为空");
            }
        }
        if (!StringUtils.hasText(properties.getServerName())) {
            properties.setServerName("consumer");
        }
        if (!StringUtils.hasText(properties.getServerType())) {
            properties.setServerType(properties.getServerName());
        }
        ServerProperties.ConsumerProperties consumer = properties.getConsumer();

        if (!StringUtils.hasText(consumer.getReadableName()) || consumer.getReadableName().equals(DEFAULT_CONSUMER_PROPERTIES.getReadableName())) {
            consumer.setReadableName(properties.getServerName());
        }

        //io *2 logic *1 综合1.5
        double size = Runtime.getRuntime().availableProcessors() * 1.5;
        int ioSize = (int) (size * 0.6);
        ioSize = Math.max(ioSize, 1);
        int logicSize = (int) (size * 0.4);
        logicSize = Math.max(logicSize, 1);
        if (consumer.getIoWorkThreadPoolSize() < 1) {
            consumer.setIoWorkThreadPoolSize(ioSize);
        }
        if (consumer.getExecutorThreadPoolSize() < 1) {
            consumer.setExecutorThreadPoolSize(logicSize);
        }
    }

    @Bean
    @ConditionalOnMissingBean(TaskLoopGroup.class)
    public TaskLoopGroup taskLoopGroup() {
        TaskLoopGroup service = new DefaultTaskLoopGroup(properties.getConsumer().getExecutorThreadPoolSize(),
                new DefaultThreadFactory(properties.getServerName() + "-executor"));
        this.taskLoopGroup = service;
        return service;

    }

    @PreDestroy
    public void destroy() {
        if (taskLoopGroup != null) {
            taskLoopGroup.shutdownGracefully();
        }
    }

    @Bean
    @ConditionalOnMissingBean(ConsumerMessageDecoderContext.class)
    public ConsumerMessageDecoderContext consumerMessageDecoderContext () {
        return new DefaultConsumerMessageDecoderContext();
    }


    @Bean
    public ConsumerMessageHandlerContext consumerMessageHandlerContext() {

        return new DefaultConsumerMessageHandlerContext();
    }

    // @Bean
//    public RemoteServerManager remoteServerManager() {
//        return new RemoteServerManager(properties.getConsumer());
//    }

    @Bean
    public ProviderManager providerManager() {

        return new ProviderManager();
    }

    @Bean
    public ConsumerMessageExecutor consumerMessageExecutor(TaskLoopGroup taskLoopGroup,
                                                           ConsumerMessageHandlerContext messageDecoderContext,
                                                           ProviderManager providerManager) {
        return new ConsumerMessageExecutor(taskLoopGroup,
                properties.getConsumer(),
                messageDecoderContext, providerManager);
    }

    @Bean
    public ConsumerServerStarter consumerServerStarter() {
        return new ConsumerServerStarter();
    }

    @Bean
    public ConsumerHandlerChecker consumerHandlerChecker() {
        return new ConsumerHandlerChecker();
    }


    public static class ConsumerHandlerChecker implements ApplicationRunner {
        @Resource
        private ConsumerMessageHandlerContext handlerContext;

        @Override
        public void run(ApplicationArguments args) {
            ConsumerMessageHandler<?> handler = handlerContext.handler(SCFrameworkErrorMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[SCFrameworkErrorMessage]处理器");
            }
            handler = handlerContext.handler(SCHeartMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[SCHeartMessage]处理器");
            }
            handler = handlerContext.handler(SCSuccessMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[SCSuccessMessage]处理器,无法提供消息解码器");
            }
        }
    }
}
