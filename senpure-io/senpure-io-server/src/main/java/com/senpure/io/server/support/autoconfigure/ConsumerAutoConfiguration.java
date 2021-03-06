package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.util.Assert;
import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.DefaultMessageDecoderContext;
import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.consumer.ConsumerMessageExecutor;
import com.senpure.io.server.consumer.ConsumerMessageHandlerContext;
import com.senpure.io.server.consumer.DefaultConsumerMessageHandlerContext;
import com.senpure.io.server.consumer.RemoteServerManager;
import com.senpure.io.server.consumer.handler.ConsumerMessageHandler;
import com.senpure.io.server.consumer.remoting.DefaultFuture;
import com.senpure.io.server.consumer.remoting.SuccessCallback;
import com.senpure.io.server.protocol.message.SCHeartMessage;
import com.senpure.io.server.protocol.message.SCInnerErrorMessage;
import com.senpure.io.server.support.ConsumerServerStarter;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * ConsumerAutoConfiguration
 *
 * @author senpure
 * @time 2019-03-01 11:46:50
 */


public class ConsumerAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Resource
    private ServerProperties serverProperties;

    private TaskLoopGroup taskLoopGroup;


    @PostConstruct
    public void init() {
        check();
    }


    private void check() {
        if (StringUtils.isEmpty(serverProperties.getName())) {
            serverProperties.setName("consumerServer");
        }
        ServerProperties.Consumer consumer = serverProperties.getConsumer();
        if (!consumer.isSetReadableName()) {
            consumer.setReadableName(serverProperties.getName());
        }
        //io *2 logic *1 ??????1.5
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
        TaskLoopGroup service = new DefaultTaskLoopGroup(serverProperties.getConsumer().getExecutorThreadPoolSize(),
                new DefaultThreadFactory(serverProperties.getName() + "-executor"));
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
    @ConditionalOnMissingBean(MessageDecoderContext.class)
    public MessageDecoderContext messageDecoderContext() {
        return new DefaultMessageDecoderContext();
    }

    @Bean
    public ConsumerMessageHandlerContext consumerMessageHandlerContext() {
        ConsumerMessageHandlerContext handlerContext = new DefaultConsumerMessageHandlerContext();
        SuccessCallback.setHandlerContext(handlerContext);

        return handlerContext;
    }

    @Bean
    public RemoteServerManager remoteServerManager() {
        return new RemoteServerManager(serverProperties.getConsumer());
    }

    @Bean
    public ConsumerMessageExecutor consumerMessageExecutor(ConsumerMessageHandlerContext messageDecoderContext) {

        ConsumerMessageExecutor messageExecutor= new ConsumerMessageExecutor(serverProperties.getConsumer(), messageDecoderContext);
        DefaultFuture.setMessageExecutor(messageExecutor);
        return messageExecutor;
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
            ConsumerMessageHandler<?> handler = handlerContext.handler(SCInnerErrorMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("??????[SCInnerErrorMessage]?????????");
            }
            handler = handlerContext.handler(SCHeartMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("??????[SCHeartMessage]?????????");
            }
        }
    }
}
