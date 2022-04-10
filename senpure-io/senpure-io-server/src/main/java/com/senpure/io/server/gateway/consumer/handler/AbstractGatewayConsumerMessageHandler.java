package com.senpure.io.server.gateway.consumer.handler;


import com.senpure.io.server.gateway.GatewayMessageExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

public abstract class AbstractGatewayConsumerMessageHandler implements GatewayConsumerMessageHandler, InitializingBean {

    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Resource
    protected GatewayMessageExecutor messageExecutor;

    @Override
    public void afterPropertiesSet() {
        messageExecutor.registerConsumerMessageHandler(this);
    }
}
