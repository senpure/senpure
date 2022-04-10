package com.senpure.io.server.provider.handler;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageDecoder;
import com.senpure.io.server.provider.MessageSender;
import com.senpure.io.server.provider.ProviderMessageDecoderContext;
import com.senpure.io.server.provider.ProviderMessageHandlerContext;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

public abstract class AbstractProviderMessageHandler<T extends Message> implements ProviderMessageHandler<T>, MessageDecoder<T>, InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    protected MessageSender messageSender;
    @Resource
    protected ProviderMessageHandlerContext handlerContext;
    @Resource
    protected ProviderMessageDecoderContext decoderContext;

    @Override
    public void afterPropertiesSet() {
        handlerContext.registerHandler(this);
        decoderContext.registerDecoder(this);
    }

    /**
     * 是否直接转发，false 网关会进行一次询问
     */
    @Override
    public boolean direct() {
        return true;
    }

    /**
     * 是否向网关注册该handler
     * 框架内部必须的不要注册到网关
     */
    @Override
    public boolean registerToGateway() {
        return true;
    }

    @Override
    public T decode(ByteBuf buf, int maxIndex) {
        T message = newEmptyMessage();
        message.read(buf, maxIndex);
        return message;
    }
}
