package com.senpure.io.server.consumer;

import com.senpure.io.server.MessageHandlerContext;
import com.senpure.io.server.consumer.handler.ConsumerMessageHandler;


public interface ConsumerMessageHandlerContext  extends MessageHandlerContext {

    void registerHandler(ConsumerMessageHandler<?> handler);

    ConsumerMessageHandler<?> handler(int messageId);
}
