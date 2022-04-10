package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.util.Assert;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.protocol.message.*;
import com.senpure.io.server.provider.DefaultProviderMessageDecoderContext;
import com.senpure.io.server.provider.DefaultProviderMessageHandlerContext;
import com.senpure.io.server.provider.ProviderMessageDecoderContext;
import com.senpure.io.server.provider.ProviderMessageHandlerContext;
import com.senpure.io.server.provider.handler.ProviderMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * ProducerAutoConfiguration
 *
 * @author senpure
 * @time 2019-03-01 11:46:50
 */


public class ProviderAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public ProviderAutoConfiguration(Environment env, ServerProperties serverProperties) {
        check(env, serverProperties);

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
            properties.setServerName("provider");
        }

        if (!StringUtils.hasText(properties.getServerType())) {
            properties.setServerType(properties.getServerName());
        }
        ServerProperties.ProviderProperties provider = properties.getProvider();

        if (!StringUtils.hasText(provider.getReadableName()) || provider.getReadableName().equals(new ServerProperties.ProviderProperties().getReadableName())) {
            provider.setReadableName(properties.getServerName());
        }

    }

    @Bean
    @ConditionalOnMissingBean(ProviderMessageDecoderContext.class)
    public ProviderMessageDecoderContext providerMessageDecoderContext () {

        return new DefaultProviderMessageDecoderContext();
    }

    @Bean
    public ProviderMessageHandlerContext providerMessageHandlerContext() {
        return new DefaultProviderMessageHandlerContext();
    }


    @Bean
    public ProviderHandlerChecker providerHandlerChecker() {
        return new ProviderHandlerChecker();
    }


    public static class ProviderHandlerChecker implements ApplicationRunner {

        @Resource
        private ProviderMessageHandlerContext handlerContext;
        @Resource
        private ServerProperties serverProperties;

        @Override
        public void run(ApplicationArguments args) {
            ProviderMessageHandler<?> handler;
            handler = handlerContext.handler(CSBreakUserGatewayMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[CSBreakUserGatewayMessage]处理器");
            }
            handler = handlerContext.handler(CSRelationUserGatewayMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[CSRelationUserGatewayMessage]处理器");
            }
            handler = handlerContext.handler(SCSuccessMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[SCSuccessMessage]处理器,无法提供消息解码器");
            }

            handler = handlerContext.handler(SCRegisterProviderMessage.MESSAGE_ID);
            if (handler == null) {
                Assert.error("缺少[SCRegisterProviderMessage]处理器,无法提供消息解码器");
            }

            if (serverProperties.getProvider().isFrameworkVerifyProvider()) {
                handler = handlerContext.handler(CSFrameworkVerifyMessage.MESSAGE_ID);
                if (handler == null) {
                    Assert.error("表明自己可以提供框架认证功能，但是缺少[CSFrameworkVerifyMessage]处理器");
                }
                handler = handlerContext.handler(SCFrameworkVerifyProviderMessage.MESSAGE_ID);
                if (handler == null) {
                    Assert.error("表明自己可以提供框架认证功能，但是缺少[SCFrameworkVerifyProviderMessage]处理器,无法提供消息解码器");
                }
            }

        }
    }
}
