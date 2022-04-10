package com.senpure.io.server.provider.handler;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageHandler;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;


public interface ProviderMessageHandler<T extends Message> extends MessageHandler<T> {
    /**
     * @param channel netty channel
     * @param token   用户此次连接的token
     * @param userId  用户id {@code userId ==0 } 表示没有经过身份认证
     * @param message message
     * @throws Exception Exception
     */
    void execute(Channel channel, long token, long userId, T message) throws Exception;

    /**
     * 是否直接转发，false 网关会进行一次询问
     */
    boolean direct();


    /**
     * 是否向网关注册该handler
     * 框架内部必须的不要注册到网关
     */
    boolean registerToGateway();

    /**
     * new 一个空对象
     */

    @Nonnull
    T newEmptyMessage();

}
