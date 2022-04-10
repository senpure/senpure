package com.senpure.io.server.provider;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.remoting.ResponseCallback;

import java.util.List;


public interface MessageSender extends com.senpure.io.server.remoting.MessageSender {


    Long[] EMPTY_ARRAY = new Long[0];

    /**
     * 向用户发送消息
     *
     * @param userId  用户唯一标识
     * @param message 消息
     */
    void sendMessage(Long userId, Message message);

    /**
     * 向用户发送异步回调消息
     *
     * @param userId   用户唯一标识
     * @param message  消息
     * @param callback 回调函数
     */
    void sendMessage(Long userId, Message message, ResponseCallback callback);

    /**
     * 向用户发送异步回调消息
     *
     * @param userId   用户唯一标识
     * @param message  消息
     * @param callback 回调函数
     * @param timeout  超时毫秒
     */
    void sendMessage(Long userId, Message message, ResponseCallback callback, int timeout);

    /**
     * 向用户发送消息
     *
     * @param token   用户本次临时唯一标识
     * @param message 消息
     */
    void sendMessageByToken(Long token, Message message);


    /**
     * 响应用户消息
     *
     * @param userId  用户唯一标识
     * @param message 消息
     */
    void respondMessage(Long userId, Message message);

    /**
     * 响应用户消息
     *
     * @param userId    用户唯一标识
     * @param message   消息
     * @param requestId 用户请求唯一标识
     */
    void respondMessage(Long userId, Message message, int requestId);

    /**
     * 响应用户消息
     *
     * @param token   用户本次临时唯一标识
     * @param message 消息
     */
    void respondMessageByToken(Long token, Message message);

    /**
     * 响应用户消息
     *
     * @param token     用户本次临时唯一标识
     * @param message   消息
     * @param requestId 用户请求唯一标识
     */
    void respondMessageByToken(Long token, Message message, int requestId);


    void sendMessage(Long userId, List<Message> messages);

    void sendMessageByToken(Long token, List<Message> messages);

    void sendMessage(List<Long> userIds, Message message);

    void sendMessageByToken(List<Long> tokens, Message message);

//    /**
//     * 随机选择一个服务器发送消息
//     *
//     * @param message 消息
//     */
//    void sendMessage(Message message);
//
//    /**
//     * @param message  消息
//     * @param callback callback
//     */
//    void sendMessage(Message message, ResponseCallback callback);
//
//    /**
//     * @param message  消息
//     * @param callback callback
//     * @param timeout  timeout
//     */
//    void sendMessage(Message message, ResponseCallback callback, int timeout);

    /**
     * 将消息发送给所有的consumer
     *
     * @param message 消息
     */
    void dispatchMessage(Message message);

    /**
     * 将消息发送给所有的consumer
     *
     * @param message 消息
     */
    void dispatchMessage(ProviderSendMessage message);

    /**
     * 将消息发送给其他Provider
     *
     * @param serverName Provider服务名
     * @param serverKey  Provider服务唯一表示
     * @param message    消息
     */

    void dispatchMessage(String serverName, String serverKey, Message message);


    void sendKickOffMessage(Long userId);

    void sendKickOffMessageByToken(Long token);

    /**
     * 用户主动离开该服务器后，调用该方法，断线不要调用
     * 与byToken 调用一个就可以了
     *
     * @param userId userId
     */
    void sendUserLeaveMessage(Long userId);

    /**
     * 用户主动离开该服务器后，调用该方法，断线不要调用
     * 与userId 调用一个就可以了
     *
     * @param token token
     */
    void sendUserLeaveMessageByToken(Long token);

    void respondMessageByTokenAndRelationUser(Long token, Long userId, Message message);

    void respondMessageByTokenAndRelationUser(Long token, Long userId, Message message, int requestId);

    void sendMessageByTokenAndRelationUser(Long token, Long userId, Message message);

    /**
     * 将userId与网关关联起来
     *
     * @param gatewayKey    gatewayKey
     * @param userId        userId
     * @param relationToken relationToken
     */
    void relationUser(String gatewayKey, Long userId, long relationToken);

    /**
     * 将token与网关关联起来
     *
     * @param gatewayKey    gatewayKey
     * @param token         token
     * @param relationToken relationToken
     */

    void relationToken(String gatewayKey, Long token, long relationToken);

    boolean breakUser(Long userId, long relationToken);

    boolean breakToken(Long token, long relationToken);


    default ProviderSendMessage createMessage(Long userId, Message message) {
        ProviderSendMessage frame = new ProviderSendMessage(message);
        frame.setUserIds(new Long[]{userId});

        return frame;
    }

    default ProviderSendMessage createMessage(Long[] userIds, Message message) {
        ProviderSendMessage frame = new ProviderSendMessage(message);
        frame.setUserIds(userIds);
        return frame;
    }

    default ProviderSendMessage createMessageByToken(Long token, Message message) {
        ProviderSendMessage frame = new ProviderSendMessage(message);
        frame.setToken(token);
        frame.setUserIds(EMPTY_ARRAY);
        return frame;
    }
}
