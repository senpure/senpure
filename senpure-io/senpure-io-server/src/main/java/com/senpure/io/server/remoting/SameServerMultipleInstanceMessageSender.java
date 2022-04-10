package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;

import javax.annotation.Nonnull;

/**
 * 同一个服务会连接多个实例
 */
public interface SameServerMultipleInstanceMessageSender extends MessageSender {
    /**
     * 向服务实例发送消息
     *
     * @param messageSender frameSender
     * @param message       消息
     */
    void sendMessage(ServerInstanceMessageFrameSender messageSender, Message message);

    /**
     * 向服务实例发送异步回调消息
     *
     * @param frameSender frameSender
     * @param message     消息
     * @param callback    回调
     */
    void sendMessage(ServerInstanceMessageFrameSender frameSender, Message message, ResponseCallback callback);

    /**
     * 向服务实例发送异步回调消息
     *
     * @param frameSender frameSender
     * @param message     消息
     * @param callback    回调
     * @param timeout     超时毫秒
     */
    void sendMessage(ServerInstanceMessageFrameSender frameSender, Message message, ResponseCallback callback, int timeout);


    /**
     * 向服务实例发发送同步步回调消息
     *
     * @param frameSender frameSender
     * @param message     消息
     * @return {@link Response}
     */
    @Nonnull
    Response sendSyncMessage(ServerInstanceMessageFrameSender frameSender, Message message);

    /**
     * 向服务实例发发送同步步回调消息
     *
     * @param frameSender frameSender
     * @param message     消息
     * @param timeout     超时毫秒
     * @return {@link Response}
     */
    @Nonnull
    Response sendSyncMessage(ServerInstanceMessageFrameSender frameSender, Message message, int timeout);

    /**
     * 向服务实例响应消息
     *
     * @param frameSender frameSender
     * @param message     消息
     */
    void respondMessage(ServerInstanceMessageFrameSender frameSender, Message message);

    /**
     * 向服务实例响应消息
     *
     * @param frameSender frameSender
     * @param message     消息
     * @param requestId   请求唯一标识
     */
    void respondMessage(ServerInstanceMessageFrameSender frameSender, Message message, int requestId);
}
