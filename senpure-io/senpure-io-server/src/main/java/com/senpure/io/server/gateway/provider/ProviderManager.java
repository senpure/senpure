package com.senpure.io.server.gateway.provider;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayLocalSendProviderMessage;
import com.senpure.io.server.gateway.GatewayMessageExecutor;
import com.senpure.io.server.gateway.GatewaySendProviderMessage;
import com.senpure.io.server.protocol.message.CSBreakUserGatewayMessage;
import com.senpure.io.server.protocol.message.CSRelationUserGatewayMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCRelationUserGatewayMessage;
import com.senpure.io.server.remoting.AbstractSameServerMultipleInstanceMessageSender;
import com.senpure.io.server.remoting.ServerInstanceMessageFrameSender;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

public class ProviderManager extends AbstractSameServerMultipleInstanceMessageSender<GatewayLocalSendProviderMessage> {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Provider> useProviders = new ArrayList<>();
    private final ConcurrentMap<Long, ProviderRelation> tokenProviderMap = new ConcurrentHashMap<>();
    private ProviderNextStrategy nextStrategy = new ProviderDefaultNextStrategy();
    private final List<Provider> prepStopOldInstance = new ArrayList<>();
    private final Map<Integer, Boolean> handleIdsMap = new HashMap<>();
    private final GatewayMessageExecutor messageExecutor;
    private String serverName;
    private boolean registerMessageId;

    public ProviderManager(GatewayMessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    @Override
    public GatewayLocalSendProviderMessage createMessage(Message message) {
        return new GatewayLocalSendProviderMessage(message);
    }

    @Override
    public GatewayLocalSendProviderMessage createMessage(Message message, boolean requestId) {
        GatewayLocalSendProviderMessage frame = new GatewayLocalSendProviderMessage(message);
        if (requestId) {
            frame.setRequestId(nextRequestId());
        }
        return frame;
    }

    @Override
    public GatewayLocalSendProviderMessage createMessage(Message message, int requestId) {
        GatewayLocalSendProviderMessage frame = new GatewayLocalSendProviderMessage(message);
        frame.setRequestId(requestId);
        return frame;
    }


    @Nullable
    public Provider getProvider(String providerKey) {
        lock.readLock().lock();
        try {
            for (Provider provider : useProviders) {
                if (provider.getRemoteServerKey().equals(providerKey)) {
                    return provider;
                }
            }
        } finally {
            lock.readLock().unlock();
        }

        return null;
    }

    @Nonnull
    public Provider addProvider(Provider provider) {
        lock.writeLock().lock();
        try {
            for (Provider temp : useProviders) {
                if (temp.getRemoteServerKey().equals(provider.getRemoteServerKey())) {
                    return temp;
                }
            }
            useProviders.add(provider);
        } finally {
            lock.writeLock().unlock();
        }
        return provider;
    }

    public void bind(Long token, Long relationToken, Provider provider) {
        ProviderRelation providerRelation = new ProviderRelation();
        providerRelation.provider = provider;
        providerRelation.relationToken = relationToken;
        ProviderRelation old = tokenProviderMap.put(token, providerRelation);
        if (old == null) {
            provider.getStatistic().consumerIncr();
        }
    }


    @Nullable
    public Provider provider(long token) {
        ProviderRelation providerRelation = tokenProviderMap.get(token);
        if (providerRelation != null) {

            return providerRelation.provider;
        }
        return null;
    }

    public void sendMessage(GatewaySendProviderMessage frame) {
        ProviderRelation providerRelation = tokenProviderMap.get(frame.token());
        Provider provider;
        if (providerRelation == null) {
            provider = nextProvider();
            if (provider == null) {
                logger.warn("{} 没有服务实例可以使用", serverName);
                SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                errorMessage.setCode(Constant.ERROR_NOT_FOUND_PROVIDER);
                errorMessage.getArgs().add(String.valueOf(frame.messageId()));
                errorMessage.setMessage("没有服务器处理 " + MessageIdReader.read(frame.messageId()));
                messageExecutor.responseMessage2Consumer(frame.requestId(), frame.token(), errorMessage);
            } else {

                relationAndSendMessage(provider, frame);
            }

        } else {
            providerRelation.provider.sendMessage(frame);

        }
    }

    public void relation(Provider provider, long token, long userId, BiConsumer<CSRelationUserGatewayMessage, SCRelationUserGatewayMessage> biConsumer) {
        long relationToken = messageExecutor.getIdGenerator().nextId();
        CSRelationUserGatewayMessage relationUserGatewayMessage = new CSRelationUserGatewayMessage();
        relationUserGatewayMessage.setToken(token);
        relationUserGatewayMessage.setUserId(userId);
        relationUserGatewayMessage.setRelationToken(relationToken);
        sendMessage(provider, relationUserGatewayMessage, response -> {
            if (response.isSuccess()) {
                biConsumer.accept(relationUserGatewayMessage, response.getMessage());
            } else {
                logger.debug("error {} {} {}", token, relationToken, response.getMessage());
            }
        }, 5000);
    }

    public void relationAndSendMessage(Provider provider, GatewaySendProviderMessage frame) {
        relation(provider, frame.token(), frame.userId(), (cs, sc) -> {
            bind(frame.token(), cs.getRelationToken(), provider);
            provider.sendMessage(frame);
        });


    }

    //todo 该方法不对
    public void sendMessage2Consumer(@Nullable String serverKey, int messageId, byte[] data) {
        if (serverKey != null) {
            for (Map.Entry<Long, ProviderRelation> entry : tokenProviderMap.entrySet()) {
                Long userToken = entry.getKey();
                ProviderRelation providerRelation = entry.getValue();
                Provider provider = providerRelation.provider;
                if (provider.getRemoteServerKey().equals(serverKey)) {
                    messageExecutor.sendMessage2ConsumerWithoutFramework(userToken, messageId, data);
                    break;
                }
            }
        } else {
            for (Map.Entry<Long, ProviderRelation> entry : tokenProviderMap.entrySet()) {
                Long userToken = entry.getKey();
                messageExecutor.sendMessage2ConsumerWithoutFramework(userToken, messageId, data);


            }
        }

    }

    @Nullable
    public Provider nextProvider() {
        lock.readLock().lock();
        try {
            int size = useProviders.size();
            switch (size) {
                case 0:
                    return null;
                case 1:
                    return useProviders.get(0);
                default:
                    return nextStrategy.next(useProviders);
            }
        } finally {
            lock.readLock().unlock();
        }

    }

    public void markHandleId(int messageId) {
        handleIdsMap.put(messageId, true);
    }

    public List<Integer> getHandleIds() {
        return new ArrayList<>(handleIdsMap.keySet());
    }


    public boolean handleId(int messageId) {
        return handleIdsMap.get(messageId) != null;
    }

    public void prepStopOldInstance() {
        lock.writeLock().lock();
        try {
            prepStopOldInstance.addAll(useProviders);
            useProviders.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 服务器掉线
     *
     * @param channel channel
     */
    public void providerOffLine(Channel channel) {
        lock.writeLock().lock();
        try {
            providerOffLine(channel, prepStopOldInstance);
            providerOffLine(channel, useProviders);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void providerOffLine(Channel channel, List<Provider> providers) {
        Iterator<Provider> iterator = providers.iterator();
        while (iterator.hasNext()) {
            Provider provider = iterator.next();
            if (provider.offline(channel)) {
                if (provider.getChannelSize() == 0) {
                    iterator.remove();
                    clearRelation(provider);
                }
            }
        }


    }

    //用户认证之后需要对之前关联的服务器关联用户id
    public void afterUserAuthorize(long channelToken, long userId) {
        ProviderRelation providerRelation = tokenProviderMap.get(channelToken);
        if (providerRelation != null) {
            logger.info("对 {} --> {} 进行userId关联", serverName, providerRelation.provider.getRemoteServerKey());
            CSRelationUserGatewayMessage message = new CSRelationUserGatewayMessage();
            message.setUserId(userId);
            message.setRelationToken(providerRelation.relationToken);
            GatewaySendProviderMessage toMessage = messageExecutor.createMessage(message);
            if (!getHandleIds().contains(messageExecutor.getCsLoginMessageId())) {
                providerRelation.provider.sendMessage(toMessage);
            }

        }
    }

    //消费方离开了服务提供方
    public void consumerLeaveProducer(Channel consumerChannel, Long token, Long userId, long relationToken) {

        ProviderRelation providerRelation = tokenProviderMap.remove(token);
        if (providerRelation != null) {
            if (providerRelation.relationToken != relationToken) {
                tokenProviderMap.putIfAbsent(token, providerRelation);
            } else {
                logger.info("主动离开{} {} 取消 对{} :token{} userId:{}的 关联  ",
                        serverName, providerRelation.provider.getRemoteServerKey(), consumerChannel, token, userId);
                providerRelation.provider.getStatistic().consumerDecr();
            }

        } else {
            logger.info("{} 没有对{} 有关联 :token{} userId:{} ",
                    serverName, consumerChannel, token, userId);
        }

    }

    //消费方离线
    public void consumerOffline(Channel consumerChannel, Long token, Long userId) {
        breakUserGateway(consumerChannel, token, userId, Constant.BREAK_TYPE_USER_OFFlINE);
    }

    //消费方切换用户
    //除了登录服务器，其他的全部断开
    public void consumerUserChange(Channel consumerChannel, Long token, Long userId, int csLoginMessageId) {
        if (getHandleIds().contains(csLoginMessageId)) {
            breakUserGateway(consumerChannel, token, userId, Constant.BREAK_TYPE_USER_CHANGE, false);
        } else {
            breakUserGateway(consumerChannel, token, userId, Constant.BREAK_TYPE_USER_CHANGE);
        }
    }

    private void breakUserGateway(Channel consumerChannel, Long token, Long userId, String type) {
        breakUserGateway(consumerChannel, token, userId, type, true);
    }

    private void breakUserGateway(Channel consumerChannel, Long token, Long userId, String type, boolean localRemove) {
        ProviderRelation providerRelation = localRemove ? tokenProviderMap.remove(token) : tokenProviderMap.get(token);
        if (providerRelation != null) {
            if (localRemove) {
                providerRelation.provider.getStatistic().consumerDecr();
            }
            //本地已经处理，不用管provider的返回
            logger.info("{} {} 取消 对{} :token{} userId:{}的 关联  {}",
                    serverName, providerRelation.provider.getRemoteServerKey(), consumerChannel, token, userId, localRemove ? "移除" : "不移除");
            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
            breakUserGatewayMessage.setRelationToken(providerRelation.relationToken);
            breakUserGatewayMessage.setUserId(userId);
            breakUserGatewayMessage.setToken(localRemove ? token : 0);
            breakUserGatewayMessage.setType(type);
            GatewayLocalSendProviderMessage frame = messageExecutor.createMessage(breakUserGatewayMessage);
            frame.setUserId(breakUserGatewayMessage.getUserId());
            frame.setToken(breakUserGatewayMessage.getToken());
            providerRelation.provider.sendMessage(frame);


        } else {
            logger.info("{} 没有对{} 有关联 :token{} userId:{} ",
                    serverName, consumerChannel, token, userId);

        }
    }

    @Override
    public ServerInstanceMessageFrameSender getFrameSender(Channel channel) {

        String serverKey = ChannelAttributeUtil.getRemoteServerKey(channel);
        return getProvider(serverKey);
    }

    private void clearRelation(Provider serverChannelManager) {
        logger.warn("{} {} 全部channel已经下线 清空关联列表", serverName, serverChannelManager.getRemoteServerKey());
        List<Long> tokens = new ArrayList<>();
        for (Map.Entry<Long, ProviderRelation> entry : tokenProviderMap.entrySet()) {
            if (serverChannelManager == entry.getValue().provider) {
                tokens.add(entry.getKey());
            }
        }
        for (Long token : tokens) {
            logger.info("{} 取消关联 {} {} ", token, serverName, serverChannelManager.getRemoteServerKey());
            tokenProviderMap.remove(token);
        }
    }

    public void setNextStrategy(ProviderNextStrategy nextStrategy) {
        this.nextStrategy = nextStrategy;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public List<Provider> getUseProviders() {
        return useProviders;
    }

    public boolean isRegisterMessageId() {
        return registerMessageId;
    }

    public void setRegisterMessageId(boolean registerMessageId) {
        this.registerMessageId = registerMessageId;
    }

    private static class ProviderRelation {
        Provider provider;
        Long relationToken;
    }
}
