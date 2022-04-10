package com.senpure.io.server.support.autoconfigure;

import com.senpure.io.server.consumer.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * ConsumerMessageHandlerAutoConfiguration
 *
 * @author senpure
 * @time 2020-06-11 16:53:58
 */
public class ConsumerMessageHandlerAutoConfiguration {

    @Bean("consumerSCFrameworkMessageHandler")
    @ConditionalOnMissingBean(SCFrameworkErrorMessageHandler.class)
    public SCFrameworkErrorMessageHandler scFrameworkErrorMessageHandler() {
        return new DefaultSCFrameworkErrorMessageHandler();
    }

    @Bean("consumerSCHeartMessageHandler")
    @ConditionalOnMissingBean(SCHeartMessageHandler.class)
    public SCHeartMessageHandler scHeartMessageHandler() {
        return new DefaultSCHeartMessageHandler();
    }

    @Bean("consumerSCSuccessMessageHandler")
    @ConditionalOnMissingBean(SCSuccessMessageHandler.class)
    public SCSuccessMessageHandler scSuccessMessageHandler() {
        return new DefaultSCSuccessMessageHandler();
    }

    @Bean("consumerSCFrameworkVerifyMessageHandler")
    @ConditionalOnMissingBean(SCFrameworkVerifyMessageHandler.class)
    public SCFrameworkVerifyMessageHandler scFrameworkVerifyMessageHandler() {
        return new DefaultSCFrameworkVerifyMessageHandler();
    }
}
