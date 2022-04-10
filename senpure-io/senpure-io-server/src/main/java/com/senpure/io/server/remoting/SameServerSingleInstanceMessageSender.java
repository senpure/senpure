package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;

import javax.annotation.Nonnull;

/**
 * 同一个服务只会连接一个实例
 */
public interface SameServerSingleInstanceMessageSender extends SameServerMultipleInstanceMessageSender {

    /**
     * 发送消息
     *
     * @param message 消息
     */
    void sendMessage(Message message);

    /**
     * 发送异步回调消息
     *
     * @param message  消息
     * @param callback 回调
     */
    void sendMessage(Message message, ResponseCallback callback);

    /**
     * 发送异步回调消息
     *
     * @param message  消息
     * @param callback 回调
     * @param timeout  超时毫秒
     */
    void sendMessage(Message message, ResponseCallback callback, int timeout);

    /**
     * 发送同步步回调消息
     *
     * @param message 消息
     * @return {@link Response}
     */
    @Nonnull
    Response sendSyncMessage(Message message);

    /**
     * 发送同步步回调消息
     *
     * @param message 消息
     * @param timeout 超时毫秒
     * @return {@link Response}
     */
    @Nonnull
    Response sendSyncMessage(Message message, int timeout);

    /**
     * 响应消息
     *
     * @param message 消息
     */
    void respondMessage(Message message);

    /**
     * 响应消息
     *
     * @param message   消息
     * @param requestId 请求唯一标识
     */
    void respondMessage(Message message, int requestId);
}
