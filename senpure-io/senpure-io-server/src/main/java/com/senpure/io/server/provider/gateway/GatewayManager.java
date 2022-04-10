package com.senpure.io.server.provider.gateway;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageFrame;
import com.senpure.io.server.protocol.message.CSKickOffMessage;
import com.senpure.io.server.protocol.message.CSMessageForwardMessage;
import com.senpure.io.server.protocol.message.SCBreakUserGatewayMessage;
import com.senpure.io.server.provider.MessageSender;
import com.senpure.io.server.provider.ProviderSendMessage;
import com.senpure.io.server.remoting.AbstractSameServerMultipleInstanceMessageSender;
import com.senpure.io.server.remoting.MessageFrameSender;
import com.senpure.io.server.remoting.ResponseCallback;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class GatewayManager extends AbstractSameServerMultipleInstanceMessageSender<ProviderSendMessage> implements MessageSender {

    private final ConcurrentMap<String, Gateway> gatewayMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, GatewayRelation> userGatewayMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<Long, GatewayRelation> tokenGatewayMap = new ConcurrentHashMap<>();


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
    public int requestId() {
        return REQUEST_ID.get();
    }


    @Nonnull
    public Gateway getGateway(String gatewayKey, Function<String, Gateway> mappingFunction) {
        return gatewayMap.computeIfAbsent(gatewayKey, mappingFunction);
    }

    @Nullable
    public Gateway getGateway(String gatewayKey) {
        return gatewayMap.get(gatewayKey);
    }


    @Nonnull
    public List<Gateway> gateways() {
        return new ArrayList<>(gatewayMap.values());
    }


    @Nonnull
    public Gateway addGateway(@Nonnull Gateway gateway) {

        gatewayMap.putIfAbsent(gateway.getRemoteServerKey(), gateway);
        return gatewayMap.get(gateway.getRemoteServerKey());
    }

    public void report() {
        for (Map.Entry<String, Gateway> entry : gatewayMap.entrySet()) {
            logger.debug("{} {}", entry.getKey(), entry.getValue().toString());
        }
    }


    public void sendMessage(Long userId, Message message, boolean tryAllGateway) {

        ProviderSendMessage frame = createMessage(userId, message);
        GatewayRelation gatewayRelation = userGatewayMap.get(userId);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame);
        } else {
            if (tryAllGateway) {
                dispatchMessage(frame);
            } else {
                logger.warn("userId {} 不存在 GatewayRelation", userId);
            }

        }
    }

    public void sendMessageByToken(Long token, Message message, boolean tryAllGateway) {
        ProviderSendMessage frame = createMessageByToken(token, message);
        GatewayRelation gatewayRelation = tokenGatewayMap.get(token);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame);
        } else {
            if (tryAllGateway) {
                dispatchMessage(frame);
            } else {
                logger.warn("token {} 不存在 GatewayRelation", token);
            }

        }
    }

    @Override
    public void sendMessage(Long userId, Message message) {
        sendMessage(userId, message, false);
    }

    @Override
    public void sendMessage(Long userId, Message message, ResponseCallback callback) {
        ProviderSendMessage frame = createMessage(userId, message);
        GatewayRelation gatewayRelation = userGatewayMap.get(userId);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame, callback);
        } else {
            logger.warn("userId {} 不存在 GatewayRelation", userId);
        }
    }

    @Override
    public void sendMessage(Long userId, Message message, ResponseCallback callback, int timeout) {
        ProviderSendMessage frame = createMessage(userId, message);
        GatewayRelation gatewayRelation = userGatewayMap.get(userId);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame, callback, timeout);
        } else {
            logger.warn("userId {} 不存在 GatewayRelation", userId);
        }
    }

    @Override
    public void sendMessageByToken(Long token, Message message) {
        sendMessageByToken(token, message, false);
    }

    @Override
    public void respondMessage(Long userId, Message message) {
        respondMessage(userId, message, requestId());
    }

    @Override
    public void respondMessage(Long userId, Message message, int requestId) {
        ProviderSendMessage frame = createMessage(userId, message);
        frame.setRequestId(requestId);
        GatewayRelation gatewayRelation = userGatewayMap.get(userId);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame);
        } else {
            logger.warn("userId {} 不存在 GatewayRelation", userId);
        }
    }

    @Override
    public void respondMessageByToken(Long token, Message message) {
        respondMessageByToken(token, message, requestId());
    }

    @Override
    public void respondMessageByToken(Long token, Message message, int requestId) {
        ProviderSendMessage frame = createMessageByToken(token, message);
        frame.setRequestId(requestId);
        GatewayRelation gatewayRelation = tokenGatewayMap.get(token);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame);
        } else {
            logger.warn("token {} 不存在 GatewayRelation", token);
        }
    }

    @Override
    public void sendMessage(Long userId, List<Message> messages) {
        GatewayRelation gatewayRelation = userGatewayMap.get(userId);
        if (gatewayRelation != null) {
            List<MessageFrame> frames = new ArrayList<>(messages.size());
            Long[] userIds = new Long[]{userId};
            for (Message message : messages) {
                ProviderSendMessage frame = createMessage(userIds, message);
                frames.add(frame);
            }
            gatewayRelation.gateway.sendMessage(frames);
        } else {
            logger.warn("userId {} 不存在 GatewayRelation", userId);
        }
    }

    @Override
    public void sendMessageByToken(Long token, List<Message> messages) {
        GatewayRelation gatewayRelation = tokenGatewayMap.get(token);
        if (gatewayRelation != null) {
            List<MessageFrame> frames = new ArrayList<>(messages.size());
            for (Message message : messages) {
                ProviderSendMessage frame = createMessageByToken(token, message);
                frames.add(frame);
            }
            gatewayRelation.gateway.sendMessage(frames);
        } else {
            logger.warn("token {} 不存在 GatewayRelation", token);
        }
    }

    @Override
    public void sendMessage(List<Long> userIds, Message message) {
        Map<String, GatewayUsers> map = new HashMap<>();
        for (Long userId : userIds) {
            GatewayRelation gatewayRelation = userGatewayMap.get(userId);
            if (gatewayRelation != null) {
                String key = gatewayRelation.gateway.getRemoteServerKey();
                GatewayUsers gatewayUsers = map.get(key);
                if (gatewayUsers == null) {
                    gatewayUsers = new GatewayUsers();
                    gatewayUsers.gateway = gatewayRelation.gateway;
                    map.put(key, gatewayUsers);
                }
                gatewayUsers.userIds.add(userId);
            } else {
                logger.warn("userIds -> userId {} 不存在 GatewayRelation", userId);
            }
        }
        map.values().forEach(gatewayUsers -> {
            Long[] users = new Long[gatewayUsers.userIds.size()];
            gatewayUsers.userIds.toArray(users);
            ProviderSendMessage frame = createMessage(users, message);
            gatewayUsers.gateway.sendMessage(frame);
        });
    }

    @Override
    public void sendMessageByToken(List<Long> tokens, Message message) {
        for (Long token : tokens) {
            sendMessageByToken(token, message);
        }
    }


    @Override
    public void dispatchMessage(Message message) {
        ProviderSendMessage frame = createMessage(new Long[]{0L}, message);
        dispatchMessage(frame);
    }

    @Override
    public void dispatchMessage(ProviderSendMessage message) {
        for (Gateway gateway : gatewayMap.values()) {
            gateway.sendMessage(message);
        }
    }

    @Override
    public void dispatchMessage(String serverName, String serverKey, Message message) {
        CSMessageForwardMessage forwardMessage = new CSMessageForwardMessage();
        forwardMessage.setServerName(serverName);
        forwardMessage.setServerKey(serverKey);

        ByteBuf buf = Unpooled.buffer(forwardMessage.serializedSize());
        message.write(buf);
        forwardMessage.setData(buf.array());

        dispatchMessage(forwardMessage);
    }

    @Override
    public void sendKickOffMessage(Long userId) {
        CSKickOffMessage message = new CSKickOffMessage();
        message.setUserId(userId);
        sendMessage(userId, message, true);
    }

    @Override
    public void sendKickOffMessageByToken(Long token) {
        CSKickOffMessage message = new CSKickOffMessage();
        message.setToken(token);
        sendMessageByToken(token, message, true);
    }

    @Override
    public void sendUserLeaveMessage(Long userId) {
        sendMessage(userId, new SCBreakUserGatewayMessage());
    }

    @Override
    public void sendUserLeaveMessageByToken(Long token) {
        sendMessageByToken(token, new SCBreakUserGatewayMessage());
    }

    @Override
    public void respondMessageByTokenAndRelationUser(Long token, Long userId, Message message) {
        respondMessageByTokenAndRelationUser(token, userId, message, requestId());
    }

    @Override
    public void respondMessageByTokenAndRelationUser(Long token, Long userId, Message message, int requestId) {
        if (userId == 0) {
            logger.warn("userId 不能为0");
            return;
        }
        ProviderSendMessage frame = new ProviderSendMessage(message);
        frame.setToken(token);
        frame.setUserIds(new Long[]{userId});
        frame.setRequestId(requestId);

        GatewayRelation gatewayRelation = tokenGatewayMap.get(token);
        if (gatewayRelation != null) {
            gatewayRelation.gateway.sendMessage(frame);
            //关联userId
            userGatewayMap.put(userId, gatewayRelation);
        } else {
            logger.warn("token {} 不存在 GatewayRelation", token);
        }
    }

    @Override
    public void sendMessageByTokenAndRelationUser(Long token, Long userId, Message message) {
        respondMessageByTokenAndRelationUser(token, userId, message, requestId());
    }

    @Override
    public void relationUser(String gatewayKey, Long userId, long relationToken) {
        Gateway gateway = gatewayMap.get(gatewayKey);
        if (gateway != null) {
            GatewayRelation relation = new GatewayRelation();
            relation.relationToken = relationToken;
            relation.gateway = gateway;
            userGatewayMap.put(userId, relation);
        }
    }

    @Override
    public void relationToken(String gatewayKey, Long token, long relationToken) {
        Gateway gateway = gatewayMap.get(gatewayKey);
        if (gateway != null) {
            GatewayRelation relation = new GatewayRelation();
            relation.relationToken = relationToken;
            relation.gateway = gateway;
            tokenGatewayMap.put(token, relation);
        }
    }

    @Override
    public boolean breakUser(Long userId, long relationToken) {
        GatewayRelation relation = userGatewayMap.get(userId);
        if (relation != null) {
            if (relation.relationToken == relationToken) {
                logger.debug("{} 取消关联user {}", relation.gateway.getRemoteServerKey(), userId);
                userGatewayMap.remove(userId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean breakToken(Long token, long relationToken) {
        GatewayRelation relation = tokenGatewayMap.get(token);
        if (relation != null) {
            if (relation.relationToken == relationToken) {
                logger.debug("{} 取消关联token {}", relation.gateway.getRemoteServerKey(), token);
                tokenGatewayMap.remove(token);
                return true;
            }
        }
        return false;
    }

    @Override
    public void sendMessage(Channel channel, Message message) {
        MessageFrame frame = createMessage(message);
        channel.writeAndFlush(frame);
    }

    @Override
    public void respondMessage(Channel channel, Message message) {

        MessageFrame frame = createMessage(message, requestId());
        channel.writeAndFlush(frame);
    }

    @Override
    public void respondMessage(Channel channel, Message message, int requestId) {

        MessageFrame frame = createMessage(message, requestId);
        channel.writeAndFlush(frame);
    }

    @Override
    public MessageFrameSender getFrameSender(Channel channel) {
//        String serverKey = ChannelAttributeUtil.getRemoteServerKey(channel);
//        return getGateway(serverKey);
        throw new RuntimeException("gatewayManager不允许调用该方法,请使用具体的gateway实例发送消息");
    }

    private static class GatewayUsers {
        List<Long> userIds = new ArrayList<>(16);
        Gateway gateway;
    }

    private static class GatewayRelation {
        Gateway gateway;
        Long relationToken;
    }
}
