package com.senpure.io.server.support;

import com.senpure.io.server.MessageDecoderContext;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.direct.ClientManager;
import com.senpure.io.server.direct.DirectServer;
import com.senpure.io.server.protocol.bean.IdName;
import com.senpure.io.server.provider.ProviderMessageExecutor;
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
public class DirectServerStarter {
    @Resource
    private ServerProperties properties;

    @Resource
    private ProviderMessageExecutor messageExecutor;
    @Resource
    private ClientManager clientManager;
    @Resource
    private MessageDecoderContext decoderContext;

    private DirectServer directServer;


    @PostConstruct
    public void init() {

        DirectServer directServer = new DirectServer();
        directServer.setMessageExecutor(messageExecutor);
        directServer.setProperties(properties.getProvider());

        directServer.setClientManager(clientManager);

        directServer.setDecoderContext(decoderContext);

        directServer.start();
        if (StringUtils.isNoneEmpty(properties.getProvider().getIdNamesPackage())) {
            List<IdName> idNames=  MessageScanner.scan(properties.getProvider().getIdNamesPackage());
            MessageIdReader.relation(idNames);

        }

        this.directServer = directServer;
    }


    @PreDestroy
    public void destroy() {
        if (directServer != null) {
            directServer.destroy();
        }


    }


}
