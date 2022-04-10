package com.senpure.io.server.provider;

import com.senpure.io.server.MessageHandlerContext;
import com.senpure.io.server.provider.handler.ProviderMessageHandler;

import java.util.List;

public interface ProviderMessageHandlerContext extends MessageHandlerContext {

    void registerHandler(ProviderMessageHandler<?> handler);

    ProviderMessageHandler<?> handler(int messageId);

    List<Integer> registerMessageIds();
}
