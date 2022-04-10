package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.AppEvn;
import com.senpure.io.server.Constant;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.support.annotation.EnableProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

public class ProviderMetadataCompletionRunListener extends AbstractMetadataCompletionRunListener {
    public ProviderMetadataCompletionRunListener(SpringApplication springApplication, String[] args) {
        super(springApplication, args);
    }

    @Override
    public void rootStarting() {

    }

    @Override
    protected Metadata metadata(ConfigurableEnvironment environment) {
        Class<?> bootClass = AppEvn.getStartClass();
        EnableProvider enableProvider = bootClass.getAnnotation(EnableProvider.class);
        if (enableProvider != null) {
           ServerProperties.ProviderProperties.MODEL model = environment.getProperty("server.io.provider.model",    ServerProperties.ProviderProperties.MODEL.class);
            if (model == ServerProperties.ProviderProperties.MODEL.GATEWAY) {
                Integer consumerPort = environment.getProperty("server.io.provider.consumer.port", Integer.class);
                if (consumerPort == null) {
                    consumerPort = new ServerProperties.ProviderProperties().getConsumer().getPort();
                }
                Metadata metadata = new Metadata("provider");
                metadata.addMetadata(Constant.GATEWAY_METADATA_CONSUMER_PORT, consumerPort);
                return metadata;
            }
        }
        return null;
    }
}
