package com.senpure.io.server.support.autoconfigure;

import com.senpure.io.server.gateway.consumer.handler.CSConsumerVerifyMessageHandler;
import com.senpure.io.server.gateway.consumer.handler.CSHeartMessageHandler;
import com.senpure.io.server.gateway.consumer.handler.CSLoginMessageHandler;
import org.springframework.context.annotation.Bean;


public class GatewayConsumerHandlerAutoConfiguration {

    @Bean
    public CSConsumerVerifyMessageHandler csConsumerVerifyMessageHandler() {
        return new CSConsumerVerifyMessageHandler();
    }

    @Bean
    public CSHeartMessageHandler csHeartMessageHandler() {
        return new CSHeartMessageHandler();
    }

    @Bean
    public CSLoginMessageHandler csLoginMessageHandler() {
        return new CSLoginMessageHandler();
    }
}
