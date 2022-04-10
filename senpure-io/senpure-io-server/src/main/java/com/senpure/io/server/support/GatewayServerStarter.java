package com.senpure.io.server.support;

import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.consumer.GatewayConsumerServer;
import com.senpure.io.server.gateway.provider.GatewayProviderServer;
import com.senpure.io.server.gateway.GatewayMessageExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

/**
 * GatewayServerStarter
 *
 * @author senpure
 * @time 2019-03-01 15:17:33
 */
public class GatewayServerStarter{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ServerProperties properties;
    private GatewayMessageExecutor messageExecutor;
    private GatewayConsumerServer gatewayConsumerServer;
    private GatewayProviderServer gatewayProviderServer;


    public void start(GatewayMessageExecutor  messageExecutor) {
        this.messageExecutor = messageExecutor;
        servers();
    }






    private void servers() {
        GatewayConsumerServer gatewayConsumerServer = new GatewayConsumerServer();
        gatewayConsumerServer.setMessageExecutor(messageExecutor);
        gatewayConsumerServer.setProperties(properties.getGateway());
        if (gatewayConsumerServer.start()) {
            this.gatewayConsumerServer = gatewayConsumerServer;
        }
        GatewayProviderServer gatewayProviderServer = new GatewayProviderServer();
        gatewayProviderServer.setMessageExecutor(messageExecutor);
        gatewayProviderServer.setProperties(properties.getGateway());
        if (gatewayProviderServer.start()) {
            this.gatewayProviderServer = gatewayProviderServer;
        }
    }

    @PreDestroy
    public void destroy() {
        if (gatewayConsumerServer != null) {
            gatewayConsumerServer.destroy();
        }
        if (gatewayProviderServer != null) {
            gatewayProviderServer.destroy();
        }
        if (messageExecutor != null) {
            messageExecutor.shutdownService();
        }
    }

    public void setProperties(ServerProperties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {

    }


}
