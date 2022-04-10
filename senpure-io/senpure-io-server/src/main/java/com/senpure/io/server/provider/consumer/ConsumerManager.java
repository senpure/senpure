package com.senpure.io.server.provider.consumer;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.provider.MessageSender;
import com.senpure.io.server.provider.ProviderSendMessage;
import com.senpure.io.server.remoting.AbstractSameServerMultipleInstanceMessageSender;
import com.senpure.io.server.remoting.FutureService;
import com.senpure.io.server.remoting.MessageFrameSender;
import com.senpure.io.server.remoting.ResponseCallback;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerManager extends AbstractSameServerMultipleInstanceMessageSender<ProviderSendMessage> implements MessageSender {

    private final Map<Long, ConsumerRelation> userChannelMap = new ConcurrentHashMap<>();
    private final Map<Long, ConsumerRelation> tokenChannelMap = new ConcurrentHashMap<>();
    private final Map<String, Channel> consumerChannelMap = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Consumer consumer;

    public ConsumerManager(FutureService futureService) {
        consumer = new Consumer();
        consumer.setFutureService(futureService);
        consumer.verifyWorkable();
    }


    public void addChannel(String key, Channel channel) {

        consumerChannelMap.put(key, channel);
    }

    /**
     * 向用户发送消息
     *
     * @param userId  用户唯一标识
     * @param message 消息
     */
    @Override
    public void sendMessage(Long userId, Message message) {

        ConsumerRelation consumerRelation = userChannelMap.get(userId);
        if (consumerRelation != null) {
            consumerRelation.channel.writeAndFlush(createMessage(userId, message));
        } else {

            logger.warn("userId {} channel is null ", userId);

        }
    }

    @Override
    public void sendMessage(Long userId, Message message, ResponseCallback callback) {
        ConsumerRelation consumerRelation = userChannelMap.get(userId);
        if (consumerRelation != null) {
            consumer.sendMessage(consumerRelation.channel, createMessage(userId, message), callback);
        } else {

            logger.warn("userId {} channel is null ", userId);

        }
    }

    @Override
    public void sendMessage(Long userId, Message message, ResponseCallback callback, int timeout) {
        ConsumerRelation consumerRelation = userChannelMap.get(userId);
        if (consumerRelation != null) {
            consumer.sendMessage(consumerRelation.channel, createMessage(userId, message), callback, timeout);
        } else {

            logger.warn("userId {} channel is null ", userId);

        }
    }

    /**
     * 向用户发送消息
     *
     * @param token   用户本次临时唯一标识
     * @param message 消息
     */
    @Override
    public void sendMessageByToken(Long token, Message message) {
        ConsumerRelation consumerRelation = tokenChannelMap.get(token);
        if (consumerRelation != null) {
            consumerRelation.channel.writeAndFlush(createMessageByToken(token, message));
        } else {
            logger.warn("token  {} channel is null ", token);

        }
    }

    /**
     * 响应用户消息
     *
     * @param userId  用户唯一标识
     * @param message 消息
     */
    @Override
    public void respondMessage(Long userId, Message message) {

        respondMessage(userId, message, requestId());
    }

    /**
     * 响应用户消息
     *
     * @param userId    用户唯一标识
     * @param message   消息
     * @param requestId 用户请求唯一标识
     */
    @Override
    public void respondMessage(Long userId, Message message, int requestId) {

        ConsumerRelation relation = userChannelMap.get(userId);
        if (relation != null) {
            ProviderSendMessage frame = createMessage(userId, message);
            frame.setRequestId(requestId);
            relation.channel.writeAndFlush(frame);
        } else {

            logger.warn("userId {} channel is null ", userId);

        }
    }

    /**
     * 响应用户消息
     *
     * @param token   用户本次临时唯一标识
     * @param message 消息
     */
    @Override
    public void respondMessageByToken(Long token, Message message) {
        respondMessageByToken(token, message, requestId());
    }

    /**
     * 响应用户消息
     *
     * @param token     用户本次临时唯一标识
     * @param message   消息
     * @param requestId 用户请求唯一标识
     */
    @Override
    public void respondMessageByToken(Long token, Message message, int requestId) {
        ConsumerRelation relation = tokenChannelMap.get(token);
        if (relation != null) {
            ProviderSendMessage frame = createMessageByToken(token, message);
            frame.setRequestId(requestId);
            relation.channel.writeAndFlush(frame);
        } else {

            logger.warn("token {} channel is null ", token);

        }
    }

    @Override
    public void sendMessage(Long userId, List<Message> messages) {
        ConsumerRelation relation = userChannelMap.get(userId);
        if (relation != null) {
            for (Message message : messages) {
                ProviderSendMessage frame = createMessage(userId, message);
                relation.channel.writeAndFlush(frame);
            }

        } else {

            logger.warn("userId {} channel is null ", userId);

        }
    }

    @Override
    public void sendMessageByToken(Long token, List<Message> messages) {
        ConsumerRelation relation = tokenChannelMap.get(token);
        if (relation != null) {
            for (Message message : messages) {
                ProviderSendMessage frame = createMessageByToken(token, message);
                relation.channel.writeAndFlush(frame);
            }

        } else {

            logger.warn("token {} channel is null ", token);

        }
    }

    @Override
    public void sendMessage(List<Long> userIds, Message message) {

        for (Long userId : userIds) {
            sendMessage(userId, message);
        }

    }

    @Override
    public void sendMessageByToken(List<Long> tokens, Message message) {

        for (Long token : tokens) {
            sendMessageByToken(token, message);
        }
    }

    /**
     * 将消息发送给所有的consumer
     *
     * @param message 消息
     */
    @Override
    public void dispatchMessage(Message message) {

        logger.warn("direct dispatchMessage ignore");

    }

    /**
     * 将消息发送给所有的consumer
     *
     * @param message 消息
     */
    @Override
    public void dispatchMessage(ProviderSendMessage message) {
        logger.warn("direct dispatchMessage ignore");
    }

    /**
     * 将消息发送给其他Provider
     *
     * @param serverName Provider服务名
     * @param serverKey  Provider服务唯一表示
     * @param message    消息
     */
    @Override
    public void dispatchMessage(String serverName, String serverKey, Message message) {
        logger.warn("direct dispatchMessage ignore");
    }

    @Override
    public void sendKickOffMessage(Long userId) {
        ConsumerRelation relation = userChannelMap.get(userId);
        if (relation != null) {

            relation.channel.close();

        } else {

            logger.warn("userId {} channel is null ", userId);

        }
    }

    @Override
    public void sendKickOffMessageByToken(Long token) {
        // logger.warn("direct sendKickOffMessageByToken ignore");

        ConsumerRelation relation = tokenChannelMap.get(token);
        if (relation != null) {

            relation.channel.close();

        } else {

            logger.warn("token {} channel is null ", token);

        }
    }

    /**
     * 用户主动离开该服务器后，调用该方法，断线不要调用
     * 与byToken 调用一个就可以了
     *
     * @param userId userId
     */
    @Override
    public void sendUserLeaveMessage(Long userId) {
        logger.warn("direct sendUserLeaveMessage ignore");
    }

    /**
     * 用户主动离开该服务器后，调用该方法，断线不要调用
     * 与userId 调用一个就可以了
     *
     * @param token token
     */
    @Override
    public void sendUserLeaveMessageByToken(Long token) {
        logger.warn("direct endUserLeaveMessageByToken ignore");
    }

    @Override
    public void respondMessageByTokenAndRelationUser(Long token, Long userId, Message message) {

        respondMessageByTokenAndRelationUser(token, userId, message, requestId());

    }

    @Override
    public void respondMessageByTokenAndRelationUser(Long token, Long userId, Message message, int requestId) {
        ConsumerRelation relation = tokenChannelMap.get(token);
        if (relation != null) {
            userChannelMap.put(userId, relation);
            ChannelAttributeUtil.setUserId(relation.channel, userId);
            ProviderSendMessage frame = createMessage(userId, message);
            frame.setRequestId(requestId);
            relation.channel.writeAndFlush(frame);
        } else {
            logger.warn("token  {} channel is null ", token);

        }
    }

    @Override
    public void sendMessageByTokenAndRelationUser(Long token, Long userId, Message message) {

        respondMessageByTokenAndRelationUser(token, userId, message, 0);
    }

    /**
     * 将userId与网关关联起来
     *
     * @param gatewayKey    gatewayKey
     * @param userId        userId
     * @param relationToken relationToken
     */
    @Override
    public void relationUser(String gatewayKey, Long userId, long relationToken) {

        Channel channel = consumerChannelMap.get(gatewayKey);
        if (channel != null) {
            ConsumerRelation consumerRelation = new ConsumerRelation();
            consumerRelation.relationToken = relationToken;
            consumerRelation.channel = channel;
            consumerRelation.key = gatewayKey;
            userChannelMap.put(userId, consumerRelation);
        } else {

            logger.warn("gatewayKey {} userId  没有找到channel", gatewayKey);
        }
    }

    /**
     * 将token与网关关联起来
     *
     * @param gatewayKey    gatewayKey
     * @param token         token
     * @param relationToken relationToken
     */
    @Override
    public void relationToken(String gatewayKey, Long token, long relationToken) {

        Channel channel = consumerChannelMap.get(gatewayKey);
        if (channel != null) {
            ConsumerRelation consumerRelation = new ConsumerRelation();
            consumerRelation.relationToken = relationToken;
            consumerRelation.channel = channel;
            consumerRelation.key = gatewayKey;
            tokenChannelMap.put(token, consumerRelation);
        } else {

            logger.warn("gatewayKey {}  token没有找到channel", gatewayKey);
        }
    }

    @Override
    public boolean breakUser(Long userId, long relationToken) {

        ConsumerRelation relation = userChannelMap.get(userId);
        if (relation != null) {
            if (relation.relationToken == relationToken) {
                logger.debug("{} 取消关联user {}", relation.key, userId);
                userChannelMap.remove(userId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean breakToken(Long token, long relationToken) {
        ConsumerRelation relation = tokenChannelMap.get(token);
        if (relation != null) {
            if (relation.relationToken == relationToken) {
                logger.debug("{} 取消关联token {}", relation.key, token);
                tokenChannelMap.remove(token);
                return true;
            }
        }
        return false;
    }

    @Override
    public ProviderSendMessage createMessage(Message message) {
        return createMessage(0L, message);
    }

    @Override
    public ProviderSendMessage createMessage(Message message, boolean requestId) {
        ProviderSendMessage frame = createMessage(0L, message);
        if (requestId) {
            frame.setRequestId(nextRequestId());
        }
        return frame;
    }



    @Override
    public ProviderSendMessage createMessage(Message message, int requestId) {
        ProviderSendMessage frame = createMessage(0L, message);
        frame.setRequestId(requestId);

        return frame;
    }

    @Override
    public MessageFrameSender getFrameSender(Channel channel) {
        return consumer;
    }


    private static class ConsumerRelation {
        String key;
        Channel channel;
        Long relationToken;
    }

}
