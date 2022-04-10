package com.senpure.io.server.consumer;


import com.senpure.base.util.Assert;
import com.senpure.io.server.consumer.handler.ConsumerMessageHandler;

import java.util.HashMap;
import java.util.Map;

public class DefaultConsumerMessageHandlerContext implements ConsumerMessageHandlerContext{


    private final static Map<Integer, ConsumerMessageHandler<?>> handlerMap = new HashMap<>();


    @Override
    public void registerHandler(ConsumerMessageHandler<?> handler) {
        ConsumerMessageHandler<?> last = handlerMap.get(handler.messageId());
        if (last != null) {
            Assert.error(handler.messageId() + " 处理程序已经存在 -> 新注册" + handler.newEmptyMessage()
                    .getClass().getName() + "[" + handler.getClass().getName() + "]" +
                    " -> 已注册" + last.newEmptyMessage().getClass().getName() + "[" + handler.getClass().getName() + "]");
        }
        handlerMap.put(handler.messageId(), handler);
    }

    @Override
    public ConsumerMessageHandler<?> handler(int messageId) {
        return handlerMap.get(messageId);
    }


    public static void main(String[] args) {
        new DefaultConsumerMessageHandlerContext();
    }
}
