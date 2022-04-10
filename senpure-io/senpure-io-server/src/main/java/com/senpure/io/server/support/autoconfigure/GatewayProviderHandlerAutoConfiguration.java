package com.senpure.io.server.support.autoconfigure;

import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.provider.handler.*;
import com.senpure.io.server.provider.handler.CSAskHandleMessageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class GatewayProviderHandlerAutoConfiguration {


    @Bean("gatewayProviderCSHeartMessageHandler")
    @ConditionalOnMissingBean(CSHeartMessageHandler.class)
    public CSHeartMessageHandler csHeartMessageHandler() {
        return new DefaultCSHeartMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(CSFrameworkVerifyMessageHandler.class)
    public CSFrameworkVerifyProviderMessageHandler csFrameworkVerifyProviderMessageHandler() {
        return new DefaultCSFrameworkVerifyProviderMessageHandler();
    }

    @Bean("gatewayProviderCSFrameworkVerifyMessageHandler")
    @ConditionalOnMissingBean(CSFrameworkVerifyMessageHandler.class)
    public CSFrameworkVerifyMessageHandler csFrameworkVerifyMessageHandler(ServerProperties properties) {
        ServerProperties.GatewayProperties gatewayProperties = properties.getGateway();
        return new DefaultCSFrameworkVerifyMessageHandler(gatewayProperties.isSimpleVerify(), gatewayProperties.getSimpleToken(), gatewayProperties.getSimpleUserId());
    }

    @Bean
    @ConditionalOnMissingBean(CSRegisterProviderMessageHandler.class)
    public CSRegisterProviderMessageHandler csRegServerHandleMessageMessageHandler() {

        return new DefaultCSRegisterProviderMessageHandler();
    }

    @Bean("gatewayProviderCSMatchingConsumerMessageHandler")
    @ConditionalOnMissingBean(CSMatchingConsumerMessageHandler.class)
    public CSMatchingConsumerMessageHandler csMatchingConsumerMessageMessageHandler() {
        return new DefaultCSMatchingConsumerMessageHandler();
    }


    @Bean
    @ConditionalOnMissingBean(CSKickOffMessageHandler.class)
    public CSKickOffMessageHandler scKickOffMessageHandler() {
        return new DefaultCSKickOffMessageHandler();
    }

    @Bean("gatewayProviderCSBreakUserGatewayMessageHandler")
    @ConditionalOnMissingBean(CSBreakUserGatewayMessageHandler.class)
    public CSBreakUserGatewayMessageHandler csBreakUserGatewayMessageHandler() {
        return new DefaultCSBreakUserGatewayMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(CSMessageForwardMessageHandler.class)
    public CSMessageForwardMessageHandler scMessageForwardMessageHandler() {
        return new DefaultCSMessageForwardMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(CSStatisticMessageHandler.class)
    public CSStatisticMessageHandler scStatisticMessageHandler() {
        return new DefaultCSStatisticMessageHandler();
    }

    @Bean("gatewayProviderSCLoginMessageHandler")
    @ConditionalOnMissingBean(SCLoginMessageHandler.class)
    public SCLoginMessageHandler scLoginMessageHandler() {
        return new DefaultSCLoginMessageHandler();

    }

    @Bean
    @ConditionalOnMissingBean(SCRelationUserGatewayMessageHandler.class)
    public SCRelationUserGatewayMessageHandler scRelationUserGatewayMessageHandler() {
        return new DefaultSCRelationUserGatewayMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(CSAskHandleMessageHandler.class)
    public SCAskHandleMessageHandler scAskHandleMessageHandler() {
        return new DefaultSCAskHandleMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(SCIdNameMessageHandler.class)
    public SCIdNameMessageHandler scIdNameMessageHandler() {
        return new DefaultSCIdNameMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean(SCFrameworkVerifyMessageHandler.class)
    public SCFrameworkVerifyMessageHandler scFrameworkVerifyMessageHandler() {
        return new DefaultSCFrameworkVerifyMessageHandler();
    }

    @Bean("gatewayProviderSCBreakUserGatewayMessageHandler")
    @ConditionalOnMissingBean(SCBreakUserGatewayMessageHandler.class)
    public SCBreakUserGatewayMessageHandler scBreakUserGatewayMessageHandler() {
        return new DefaultSCBreakUserGatewayMessageHandler();
    }

    @Bean("gatewayProviderCSBindProviderMessageHandler")
    @ConditionalOnMissingBean(CSBindProviderMessageHandler.class)
    public CSBindProviderMessageHandler scBindProviderMessageHandler() {
        return new DefaultCSBindProviderMessageHandler();
    }

    @Bean("gatewayProviderSCFrameworkErrorMessageHandler")
    @ConditionalOnMissingBean(SCFrameworkErrorMessageHandler.class)
    public SCFrameworkErrorMessageHandler scFrameworkErrorMessageHandler() {
        return new DefaultSCFrameworkErrorMessageHandler();
    }

    @Bean("gatewayProviderSCSuccessMessageHandler")
    @ConditionalOnMissingBean(SCSuccessMessageHandler.class)
    public SCSuccessMessageHandler scSuccessMessageHandler() {
        return new DefaultSCSuccessMessageHandler();
    }
}
