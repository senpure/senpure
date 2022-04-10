package com.senpure.io.server.remoting;

import com.senpure.io.server.MessageFrame;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.List;

public interface MessageFrameSender {

    /**
     * 使用指定的channel 向服务器发送消息
     *
     * @param channel channel
     * @param frame   消息
     */
    void sendMessage(Channel channel, MessageFrame frame);


    /**
     * 使用指定的channel 向服务器发送消息
     *
     * @param channel channel
     * @param frames  消息列表
     */
    void sendMessage(Channel channel, List<MessageFrame> frames);

    /**
     * 使用指定的channel 向服务器发送消息
     *
     * @param channel  channel
     * @param frame    消息
     * @param callback 回调
     */
    void sendMessage(Channel channel, MessageFrame frame, ResponseCallback callback);


    /**
     * 使用指定的channel 向服务器发送消息
     *
     * @param channel  channel
     * @param frame    消息
     * @param callback 回调
     * @param timeout  超时毫秒
     */
    void sendMessage(Channel channel, MessageFrame frame, ResponseCallback callback, int timeout);

    /**
     * 使用指定的channel 向服务器发送同步消息
     *
     * @param channel channel
     * @param frame   消息
     * @return Response
     */
    @Nonnull
    Response sendSyncMessage(Channel channel, MessageFrame frame);

    /**
     * 使用指定的channel 向服务器发送同步消息
     *
     * @param channel channel
     * @param frame   消息
     * @param timeout 超时毫秒
     * @return {@code Response}
     */
    @Nonnull
    Response sendSyncMessage(Channel channel, MessageFrame frame, int timeout);
}
