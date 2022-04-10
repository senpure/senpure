package com.senpure.io.server.gateway;

import com.senpure.base.util.Assert;
import com.senpure.base.util.IDGenerator;
import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.consumer.handler.GatewayConsumerMessageHandler;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.gateway.provider.handler.GatewayProviderMessageHandler;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.remoting.AbstractMessageExecutor;
import com.senpure.io.server.remoting.ChannelService;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultThreadFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class GatewayMessageExecutor extends AbstractMessageExecutor {

    private int csLoginMessageId = 0;

    private ServerProperties.GatewayProperties gatewayProperties;
    private int scLoginMessageId = 0;

    public final ConcurrentMap<Long, Channel> prepLoginChannels = new ConcurrentHashMap<>(2048);

    public final ConcurrentMap<Long, Channel> userClientChannel = new ConcurrentHashMap<>(32768);
    public final ConcurrentMap<Long, Channel> tokenChannel = new ConcurrentHashMap<>(32768);
    private final ConcurrentMap<String, ProviderManager> providerManagerMap = new ConcurrentHashMap<>(128);

    public ConcurrentMap<Integer, ProviderManager> messageHandleMap = new ConcurrentHashMap<>(2048);
    public ConcurrentMap<Integer, HandleMessageManager> handleMessageManagerMap = new ConcurrentHashMap<>(2048);


    protected IDGenerator idGenerator;
    public final ConcurrentHashMap<Long, WaitAskTask> waitAskMap = new ConcurrentHashMap<>(16);

    private final Map<Integer, GatewayProviderMessageHandler> provider2GatewayHandlerMap = new HashMap<>();
    private final Map<Integer, GatewayConsumerMessageHandler> consumer2GatewayHandlerMap = new HashMap<>();
    private boolean init = false;

    private ProviderManager verifyProviderManager;

    public GatewayMessageExecutor() {
        this(new DefaultTaskLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
                new DefaultThreadFactory("gateway-executor")), new IDGenerator(0, 0));
    }


    public GatewayMessageExecutor(TaskLoopGroup service, IDGenerator idGenerator) {

        super(service);
        this.idGenerator = idGenerator;
        // init();
        // startCheck();

    }


    public void registerProviderMessageHandler(GatewayProviderMessageHandler handler) {
        GatewayProviderMessageHandler old = provider2GatewayHandlerMap.get(handler.messageId());
        if (old != null) {
            Assert.error(handler.messageId() + " -> " + MessageIdReader.read(handler.messageId()) + "  处理程序已经存在"
                    + " 存在 " + old.getClass().getName() + " 注册 " + handler.getClass().getName());
        }
        provider2GatewayHandlerMap.put(handler.messageId(), handler);
    }

    public void registerConsumerMessageHandler(GatewayConsumerMessageHandler handler) {
        GatewayConsumerMessageHandler old = consumer2GatewayHandlerMap.get(handler.messageId());
        if (old != null) {
            Assert.error(handler.messageId() + " -> " + MessageIdReader.read(handler.messageId()) + "  处理程序已经存在"
                    + " 存在 " + old.getClass().getName() + " 注册 " + handler.getClass().getName());
        }
        consumer2GatewayHandlerMap.put(handler.messageId(), handler);
    }


    public void report() {
        logger.info("csLoginMessageId: {} scLoginMessageId:{} ", csLoginMessageId, scLoginMessageId);
    }

    public void channelActive(Channel channel) {
        Long token = idGenerator.nextId();
        ChannelAttributeUtil.setToken(channel, token);
        tokenChannel.putIfAbsent(token, channel);
        logger.debug("{} 绑定 token {}", channel, token);
    }

    public long nextToken() {
        return idGenerator.nextId();
    }

    //将客户端消息转发给具体的服务器
    public void execute(final Channel channel, final GatewayReceiveConsumerMessage message) {
        long token = message.token();
        service.get(token).execute(() -> {
            try {
                GatewayConsumerMessageHandler handler = consumer2GatewayHandlerMap.get(message.messageId());
                if (handler != null) {
                    handler.execute(channel, message);
                    if (handler.stopProvider()) {
                        return;
                    }
                }
                //转发到具体的子服务器
                HandleMessageManager handleMessageManager = handleMessageManagerMap.get(message.messageId());
                if (handleMessageManager == null) {
                    logger.warn("没有找到消息的接收服务器 {}", MessageIdReader.read(message.messageId()));
                    SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();

                    errorMessage.setCode(Constant.ERROR_NOT_FOUND_PROVIDER);
                    errorMessage.getArgs().add(String.valueOf(message.messageId()));
                    errorMessage.setMessage("没有服务器处理 " + MessageIdReader.read(message.messageId()));
                    responseMessage2Consumer(message.requestId(), message.token(), errorMessage);
                    return;
                }

                handleMessageManager.execute(message);
            } catch (Exception e) {
                logger.error("转发消息出错 " + message.messageId(), e);
                SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                errorMessage.setCode(Constant.ERROR_GATEWAY_ERROR);
                errorMessage.getArgs().add(String.valueOf(message.messageId()));
                errorMessage.setMessage(MessageIdReader.read(message.messageId()) + "," + e.getMessage());
                responseMessage2Consumer(message.requestId(), message.token(), errorMessage);
            }
        });
    }

    public void responseMessage2Consumer(int requestId, Long token, Message message) {
        Channel consumerChannel = tokenChannel.get(token);
        if (consumerChannel == null) {
            logger.warn("没有找到channel token {}", token);
        } else {
            GatewayLocalSendConsumerMessage frame = new GatewayLocalSendConsumerMessage(message);
            frame.setRequestId(requestId);
            if (consumerChannel.isWritable()) {
                consumerChannel.writeAndFlush(frame);
            }
        }
    }

    public void sendMessage2Consumer(GatewayReceiveProviderMessage message) {
        if (message.getUserIds().length == 0) {
            Channel consumerChannel = tokenChannel.get(message.getToken());
            if (consumerChannel == null) {
                logger.warn("没有找到channel token:{}", message.getToken());

            } else {
                if (consumerChannel.isWritable()) {
                    consumerChannel.writeAndFlush(message);
                }

            }
        } else {
            for (Long userId : message.getUserIds()) {
                //全消息
                if (userId == 0L) {
                    logger.debug("给所有客户端发送消息 {}", MessageIdReader.read(message.messageId()));
                    for (Map.Entry<Long, Channel> entry : userClientChannel.entrySet()) {
                        Channel consumerChannel = entry.getValue();
                        //排除掉内部consumer连接
                        if (ChannelAttributeUtil.isFramework(consumerChannel)) {
                            continue;
                        }
                        if (consumerChannel.isWritable()) {
                            consumerChannel.writeAndFlush(message);
                        }
                    }
                    break;
                } else {
                    Channel consumerChannel = userClientChannel.get(userId);
                    if (consumerChannel == null) {
                        logger.warn("没有找到用户 :{}", userId);
                    } else {
                        if (consumerChannel.isWritable()) {
                            consumerChannel.writeAndFlush(message);
                        }

                    }
                }
            }
        }
    }

    public void sendMessage2ConsumerWithoutFramework(Long token, int messageId, byte[] data) {
        Channel consumerChannel = tokenChannel.get(token);
        if (consumerChannel == null) {
            logger.warn("没有找到channel token {}", token);
        } else {
            if (ChannelAttributeUtil.isFramework(consumerChannel)) {
                return;
            }
            GatewayReceiveProviderMessage m = new GatewayReceiveProviderMessage(data.length, data);
            m.setRequestId(0);
            m.setToken(token);
            m.setMessageId(messageId);
            if (consumerChannel.isWritable()) {
                consumerChannel.writeAndFlush(m);
            }

        }
    }

    public void sendMessage2Consumer(Long token, int messageId, byte[] data) {
        Channel consumerChannel = tokenChannel.get(token);
        if (consumerChannel == null) {
            logger.warn("没有找到channel token {}", token);
        } else {
            GatewayReceiveProviderMessage m = new GatewayReceiveProviderMessage(data.length, data);
            m.setRequestId(0);
            m.setToken(token);
            m.setMessageId(messageId);
            if (consumerChannel.isWritable()) {
                consumerChannel.writeAndFlush(m);
            }

        }
    }


    public void responseMessage2Producer(int requestId, Channel channel, Message message) {
        GatewayLocalSendProviderMessage frame = createMessage(message);
        frame.setRequestId(requestId);
        if (channel.isWritable()) {
            channel.writeAndFlush(frame);
        }
    }

    public void sendMessage2Producer(Channel channel, Message message) {
        GatewayLocalSendProviderMessage toMessage = createMessage(message);
        if (channel.isWritable()) {
            channel.writeAndFlush(toMessage);
        }


    }

    public void init() {
        if (init) {
            logger.warn("messageExecutor 已经初始化");
            return;
        }
        Assert.notNull(gatewayProperties, "gateway 配置文件不能为空");
        init = true;
        Assert.isTrue(csLoginMessageId > 0 && scLoginMessageId > 0, "登录消息未设置");
        startCheck();
    }


    //处理服务器发过来的消息
    public void execute(Channel channel, final GatewayReceiveProviderMessage message) {
        long token = message.getToken();
        service.get(token).execute(() -> {
            try {
                GatewayProviderMessageHandler handler = provider2GatewayHandlerMap.get(message.getMessageId());
                if (handler != null) {
                    handler.execute(channel, message);
                    if (handler.stopConsumer()) {
                        return;
                    }
                    sendMessage2Consumer(message);
                }

            } catch (Exception e) {
                logger.error("处理服务器到网关的消息出错", e);
            }
        });
    }


    public void execute(Runnable runnable) {
        service.execute(runnable);
    }


    private void consumerOffline(Channel channel, Long token, Long userId) {
        for (Map.Entry<String, ProviderManager> entry : providerManagerMap.entrySet()) {
            ProviderManager providerManager = entry.getValue();
            providerManager.consumerOffline(channel, token, userId);

        }
    }

    /**
     * 登陆认证服务已经有了该账户的关联，不能取消token关联
     *
     * @param channel
     * @param token
     * @param userId
     */
    public void consumerUserChange(Channel channel, Long token, Long userId) {
        for (Map.Entry<String, ProviderManager> entry : providerManagerMap.entrySet()) {
            ProviderManager providerManager = entry.getValue();
            providerManager.consumerUserChange(channel, token, userId, csLoginMessageId);
        }
    }


    /**
     * 消费方离线
     *
     * @param channel
     */
    public void consumerOffline(Channel channel) {
        service.execute(() -> {
            Long token = ChannelAttributeUtil.getToken(channel);
            Long userId = ChannelAttributeUtil.getUserId(channel);
            userId = userId == null ? 0 : userId;
            tokenChannel.remove(token);
            if (userId == 0) {
                prepLoginChannels.remove(token);
            }
            consumerOffline(channel, token, userId);
        });
    }

    public void readMessage(Message message, GatewayReceiveProviderMessage gatewayReceiveProviderMessage) {
        ByteBuf buf = Unpooled.buffer(gatewayReceiveProviderMessage.getData().length);
        buf.writeBytes(gatewayReceiveProviderMessage.getData());
        message.read(buf, buf.writerIndex());
    }

    public void readMessage(Message message, byte[] data) {
        ByteBuf buf = Unpooled.buffer(data.length);
        buf.writeBytes(data);
        message.read(buf, buf.writerIndex());
    }

    public void consumerVerifySuccess(Channel channel) {

        ChannelAttributeUtil.setFramework(channel, true);
    }

    public void consumerVerifyFailure(Channel channel) {

        SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();

        // ChannelAttributeUtil.setFramework(channel, false);
    }

    public GatewayLocalSendProviderMessage createMessage(Message message) {

        return new GatewayLocalSendProviderMessage(message);
    }

    public GatewayLocalSendProviderMessage createMessage(long token, long userId, Message message) {
        GatewayLocalSendProviderMessage frame = new GatewayLocalSendProviderMessage(message);
        frame.setToken(token);
        frame.setUserId(userId);
        return frame;
    }


    public void sendMessage(Provider provider, Message message) {
        GatewaySendProviderMessage toMessage = createMessage(message);
        provider.sendMessage(toMessage);
    }


    private void checkWaitAskTask() {
        List<Long> tokens = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, WaitAskTask> entry : waitAskMap.entrySet()) {
            WaitAskTask waitAskTask = entry.getValue();
            if (waitAskTask.getProvider() != null) {
                tokens.add(entry.getKey());
                service.get(waitAskTask.getToken()).execute(waitAskTask::sendMessage);

            } else {
                //超时
                if (now - waitAskTask.getStartTime() > waitAskTask.getMaxDelay()) {
                    logger.debug("没有服务器处理 {} 值位{} 的请求 询问服务器 {} 应答服务器 {}",
                            MessageIdReader.read(waitAskTask.getFromMessageId()), waitAskTask.getValue(),
                            waitAskTask.getAskTimes(), waitAskTask.getAnswerTimes());
                    tokens.add(entry.getKey());
                    SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                    errorMessage.setCode(Constant.ERROR_NOT_HANDLE_VALUE_REQUEST);
                    errorMessage.getArgs().add(String.valueOf(waitAskTask.getFromMessageId()));
                    errorMessage.setMessage(MessageIdReader.read(waitAskTask.getFromMessageId()));
                    errorMessage.getArgs().add(Arrays.toString(waitAskTask.getValue()));
                    responseMessage2Consumer(waitAskTask.getRequestId(), waitAskTask.getMessage().token(), errorMessage);
                }
            }
        }
        for (Long token : tokens) {
            waitAskMap.remove(token);
        }
    }

    public Provider createProvider(String serverKey) {

        Provider provider = new Provider(service);
        provider.setChannelService(new ChannelService.SingleChannelService(serverKey));
        provider.setRemoteServerKey(serverKey);
        provider.setFutureService(this);
        provider.verifyWorkable();
        return provider;
    }

    @Nullable
    public ProviderManager getProviderManager(String serverName) {
        return providerManagerMap.get(serverName);
    }

    @Nonnull
    public ProviderManager addProviderManager(ProviderManager providerManager) {
        providerManagerMap.putIfAbsent(providerManager.getServerName(), providerManager);
        return providerManagerMap.get(providerManager.getServerName());
    }

    @Nonnull
    public synchronized ProviderManager addFrameworkVerifyProviderManager(ProviderManager providerManager) {
        providerManagerMap.putIfAbsent(providerManager.getServerName(), providerManager);
        verifyProviderManager = providerManagerMap.get(providerManager.getServerName());
        return verifyProviderManager;
    }

    @Nullable
    public ProviderManager getFrameworkVerifyProviderManager() {
        return verifyProviderManager;
    }

    private void startCheck() {
        service.scheduleWithFixedDelay(() -> {
            checkTimeoutFuture();
            checkWaitAskTask();
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void providerManagerForEach(Consumer<ProviderManager> consumer) {
        providerManagerMap.forEach((s, providerManager) -> consumer.accept(providerManager));
    }

    public IDGenerator getIdGenerator() {
        return idGenerator;
    }

    public TaskLoopGroup getService() {
        return service;
    }

    public int getCsLoginMessageId() {
        return csLoginMessageId;
    }

    public void setCsLoginMessageId(int csLoginMessageId) {
        this.csLoginMessageId = csLoginMessageId;
    }

    public int getScLoginMessageId() {
        return scLoginMessageId;
    }

    public void setScLoginMessageId(int scLoginMessageId) {
        this.scLoginMessageId = scLoginMessageId;
    }

    public ServerProperties.GatewayProperties getGatewayProperties() {
        return gatewayProperties;
    }

    public void setGatewayProperties(ServerProperties.GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }


}
