package com.senpure.io.server.support.autoconfigure;

import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.consumer.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


public class GatewayConsumerHandlerAutoConfiguration {

    @Bean("gatewayConsumerCSFrameworkVerifyMessageHandler")
    @ConditionalOnMissingBean(CSFrameworkVerifyMessageHandler.class)
    public CSFrameworkVerifyMessageHandler csConsumerVerifyMessageHandler(ServerProperties properties) {
        ServerProperties.GatewayProperties gatewayProperties = properties.getGateway();
        return new DefaultCSFrameworkVerifyMessageHandler(gatewayProperties.isSimpleVerify(), gatewayProperties.getSimpleToken(), gatewayProperties.getSimpleUserId());
    }

    @Bean("gatewayConsumerCSHeartMessageHandler")
    @ConditionalOnMissingBean(CSHeartMessageHandler.class)
    public CSHeartMessageHandler csHeartMessageHandler() {
        return new DefaultCSHeartMessageHandler();
    }

    @Bean("gatewayConsumerCSLoginMessageHandler")
    @ConditionalOnMissingBean(CSLoginMessageHandler.class)
    public CSLoginMessageHandler csLoginMessageHandler() {
        return new DefaultCSLoginMessageHandler();
    }
}
