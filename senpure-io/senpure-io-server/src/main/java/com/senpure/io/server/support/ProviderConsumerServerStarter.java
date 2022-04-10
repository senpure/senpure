package com.senpure.io.server.support;

import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.protocol.bean.IdName;
import com.senpure.io.server.provider.ProviderMessageDecoderContext;
import com.senpure.io.server.provider.ProviderMessageExecutor;
import com.senpure.io.server.provider.consumer.ConsumerManager;
import com.senpure.io.server.provider.consumer.ProviderConsumerServer;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

/**
 * DirectServerStarter
 *
 * @author senpure
 * @time 2019-09-18 10:01:46
 */
public class ProviderConsumerServerStarter {
    @Resource
    private ServerProperties properties;

    @Resource
    private ProviderMessageExecutor messageExecutor;
    @Resource
    private ConsumerManager consumerManager;
    @Resource
    private ProviderMessageDecoderContext decoderContext;

    private ProviderConsumerServer providerConsumerServer;


    @PostConstruct
    public void init() {

        ProviderConsumerServer providerConsumerServer = new ProviderConsumerServer();
        providerConsumerServer.setMessageExecutor(messageExecutor);
        providerConsumerServer.setProperties(properties.getProvider());

        providerConsumerServer.setClientManager(consumerManager);

        providerConsumerServer.setDecoderContext(decoderContext);

        providerConsumerServer.start();
        if (StringUtils.isNoneEmpty(properties.getProvider().getIdNamesPackage())) {
            List<IdName> idNames=  MessageScanner.scan(properties.getProvider().getIdNamesPackage());
            MessageIdReader.relation(idNames);

        }

        this.providerConsumerServer = providerConsumerServer;
    }


    @PreDestroy
    public void destroy() {
        if (providerConsumerServer != null) {
            providerConsumerServer.destroy();
        }


    }


}
