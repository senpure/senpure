package com.senpure.io.server.support.autoconfigure;


import com.senpure.io.server.gateway.provider.handler.DefaultSCBreakUserGatewayMessageHandler;
import com.senpure.io.server.gateway.provider.handler.SCBreakUserGatewayMessageHandler;
import com.senpure.io.server.provider.handler.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * ProviderMessageHandlerAutoConfiguration
 *
 * @author senpure
 * @time 2020-06-11 15:08:01
 */

public class ProviderMessageHandlerAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(CSBreakUserGatewayMessageHandler.class)
    public CSBreakUserGatewayMessageHandler csBreakUserGatewayMessageHandler() {
        return new DefaultCSBreakUserGatewayMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(CSRelationUserGatewayMessageHandler.class)
    public CSRelationUserGatewayMessageHandler csRelationUserGatewayMessageHandler() {
        return new DefaultCSRelationUserGatewayMessageHandler();
    }

    @ConditionalOnMissingBean(CSAskHandleMessageHandler.class)
    @Bean
    public DefaultCSAskHandleMessageHandler csAskHandleMessageHandler() {
        return new DefaultCSAskHandleMessageHandler();
    }

    @Bean("providerConsumerCSHeartMessageHandler")
    @ConditionalOnMissingBean(CSHeartMessageHandler.class)
    public CSHeartMessageHandler csHeartMessageHandler() {
        return new DefaultCSHeartMessageHandler();

    }

    @Bean("providerSCFrameworkErrorMessageHandler")
    @ConditionalOnMissingBean(SCFrameworkErrorMessageHandler.class)
    public SCFrameworkErrorMessageHandler scFrameworkErrorMessageHandler() {
        return new DefaultSCFrameworkErrorMessageHandler();
    }

    @Bean("providerSCFrameworkVerifyMessageHandler")
    @ConditionalOnMissingBean(SCFrameworkVerifyMessageHandler.class)
    public SCFrameworkVerifyMessageHandler scFrameworkVerifyMessageHandler() {
        return new DefaultSCFrameworkVerifyMessageHandler();
    }

    @Bean("providerSCSuccessMessageHandler")
    @ConditionalOnMissingBean(SCSuccessMessageHandler.class)
    public SCSuccessMessageHandler scSuccessMessageHandler() {
        return new DefaultSCSuccessMessageHandler();
    }


    @Bean("providerSCFrameworkVerifyProviderMessageHandler")
    @ConditionalOnMissingBean(SCFrameworkVerifyProviderMessageHandler.class)
    @ConditionalOnProperty(prefix = "server.io.provider", value = "framework-verify-provider", havingValue = "true")
    public SCFrameworkVerifyProviderMessageHandler scFrameworkVerifyProviderMessageHandler() {
        return new DefaultSCFrameworkVerifyProviderMessageHandler();
    }

    @Bean("providerSCRegisterProviderMessageHandler")
    @ConditionalOnMissingBean(SCRegisterProviderMessageHandler.class)
    public SCRegisterProviderMessageHandler scRegisterProviderMessageHandler() {
        return new DefaultSCRegisterProviderMessageHandler();
    }

    @Bean("providerSCHeartMessageHandler ")
    @ConditionalOnMissingBean(SCHeartMessageHandler.class)
    public SCHeartMessageHandler scHeartMessageHandler() {
        return new DefaultSCHeartMessageHandler();
    }



}
