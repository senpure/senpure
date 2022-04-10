package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;
import io.netty.util.concurrent.FastThreadLocal;

import javax.annotation.Nonnull;

public interface MessageSender {
    FastThreadLocal<Integer> REQUEST_ID = new FastThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    /**
     * 使用channel发送消息
     *
     * @param channel  channel
     * @param message 消息
     */
    void sendMessage(Channel channel, Message message);

    /**
     * 使用channel发送异步回调消息
     *
     * @param channel channel
     * @param message  消息
     * @param callback 回调
     */
    void sendMessage(Channel channel, Message message, ResponseCallback callback);

    /**
     * 使用channel发送异步回调消息
     *
     * @param channel channel
     * @param message  消息
     * @param callback 回调
     * @param timeout  超时毫秒
     */
    void sendMessage(Channel channel, Message message, ResponseCallback callback, int timeout);


    /**
     * 使用channel发发送同步步回调消息
     *
     * @param channel  channel
     * @param message 消息
     * @return {@link Response}
     */
    @Nonnull
    Response sendSyncMessage(Channel channel, Message message);

    /**
     * 使用channel发发送同步步回调消息
     *
     * @param channel  channel
     * @param message 消息
     * @param timeout 超时毫秒
     * @return {@link Response}
     */
    @Nonnull
    Response sendSyncMessage(Channel channel, Message message, int timeout);

    /**
     * 使用channel响应消息
     *
     * @param channel  channel
     * @param message 消息
     */
    void respondMessage(Channel channel, Message message);

    /**
     * 使用channel响应消息
     *
     * @param channel    channel
     * @param message   消息
     * @param requestId 请求唯一标识
     */
    void respondMessage(Channel channel, Message message, int requestId);
}
