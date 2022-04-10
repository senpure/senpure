package com.senpure.io.server.provider;


import com.senpure.base.util.Assert;
import com.senpure.io.server.provider.handler.ProviderMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultProviderMessageHandlerContext implements ProviderMessageHandlerContext {

    private final static Map<Integer, ProviderMessageHandler<?>> handlerMap = new HashMap<>();
    private final static List<Integer> registerMessageIds = new ArrayList<>(128);

    @Override
    public void registerHandler(ProviderMessageHandler<?> handler) {
        ProviderMessageHandler<?> last = handlerMap.get(handler.messageId());
        if (last != null) {
            Assert.error(handler.messageId() + " 处理程序已经存在 -> 新注册" + handler.newEmptyMessage()
                    .getClass().getName() + "[" + handler.getClass().getName() + "]" +
                    " -> 已注册" + last.newEmptyMessage().getClass().getName() + "[" + handler.getClass().getName() + "]");
        }
        handlerMap.put(handler.messageId(), handler);
        if (handler.registerToGateway()) {
            if (handler.messageId() < 10000) {
                Assert.error("10000 以下为保留id不允许使用 " + handler.messageId() + " " + handler.getClass().getName());
            }
            registerMessageIds.add(handler.messageId());
        }
    }



    @Override
    public  ProviderMessageHandler<?> handler(int messageId) {

        return  handlerMap.get(messageId);
    }

    @Override
    public List<Integer> registerMessageIds() {
        return new ArrayList<>(registerMessageIds);
    }


}
