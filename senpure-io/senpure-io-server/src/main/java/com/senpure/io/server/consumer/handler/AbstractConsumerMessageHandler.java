package com.senpure.io.server.consumer.handler;


import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageDecoder;
import com.senpure.io.server.consumer.ConsumerMessageDecoderContext;
import com.senpure.io.server.consumer.ConsumerMessageHandlerContext;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;


public abstract class AbstractConsumerMessageHandler<T extends Message> implements ConsumerMessageHandler<T>, MessageDecoder<T>, InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ConsumerMessageHandlerContext handlerContext;
    @Resource
    private ConsumerMessageDecoderContext decoderContext;

    public AbstractConsumerMessageHandler() {

        //  ResolvableType resolvableType = ResolvableType.forClass(getClass());
        // messageClass = (Class<T>) resolvableType.getSuperType().getGeneric(0).resolve();
    }


    @Override
    public T decode(ByteBuf buf, int maxIndex) {
        T message = newEmptyMessage();
        message.read(buf, maxIndex);
        return message;
    }

    @Override
    public void afterPropertiesSet() {
        handlerContext.registerHandler(this);
        decoderContext.registerDecoder(this);

    }
}
