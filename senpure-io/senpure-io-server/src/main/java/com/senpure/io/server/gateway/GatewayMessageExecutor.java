package com.senpure.io.server.gateway;

import com.senpure.base.util.Assert;
import com.senpure.base.util.IDGenerator;
import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.consumer.handler.ConsumerMessageHandler;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.handler.ProviderMessageHandler;
import com.senpure.io.server.protocol.message.SCInnerErrorMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


public class GatewayMessageExecutor {
    protected static Logger logger = LoggerFactory.getLogger(GatewayMessageExecutor.class);


    private final TaskLoopGroup service;
    private int serviceRefCount = 0;
    private int csLoginMessageId = 0;

    private ServerProperties.Gateway gateway;
    private int scLoginMessageId = 0;

    public final ConcurrentMap<Long, Channel> prepLoginChannels = new ConcurrentHashMap<>(2048);

    public final ConcurrentMap<Long, Channel> userClientChannel = new ConcurrentHashMap<>(32768);
    public final ConcurrentMap<Long, Channel> tokenChannel = new ConcurrentHashMap<>(32768);
    public final ConcurrentMap<String, ProviderManager> providerManagerMap = new ConcurrentHashMap<>(128);

    public ConcurrentMap<Integer, ProviderManager> messageHandleMap = new ConcurrentHashMap<>(2048);
    public ConcurrentMap<Integer, HandleMessageManager> handleMessageManagerMap = new ConcurrentHashMap<>(2048);


    protected IDGenerator idGenerator;
    public final ConcurrentHashMap<Long, WaitRelationTask> waitRelationMap = new ConcurrentHashMap<>(16);
    public final ConcurrentHashMap<Long, WaitAskTask> waitAskMap = new ConcurrentHashMap<>(16);

    private final Map<Integer, ProviderMessageHandler> p2gHandlerMap = new HashMap<>();
    private final Map<Integer, ConsumerMessageHandler> c2gHandlerMap = new HashMap<>();
    private boolean init = false;

    public GatewayMessageExecutor() {
        this(new DefaultTaskLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
                new DefaultThreadFactory("gateway-executor")), new IDGenerator(0, 0));
    }

    public GatewayMessageExecutor(TaskLoopGroup service, IDGenerator idGenerator) {
        this.service = service;
        this.idGenerator = idGenerator;
        // init();
        // startCheck();

    }


    public void registerProviderMessageHandler(ProviderMessageHandler handler) {
        ProviderMessageHandler old = p2gHandlerMap.get(handler.handleMessageId());
        if (old != null) {
            Assert.error(handler.handleMessageId() + " -> " + MessageIdReader.read(handler.handleMessageId()) + "  ????????????????????????"
                    + " ?????? " + old.getClass().getName() + " ?????? " + handler.getClass().getName());
        }
        p2gHandlerMap.put(handler.handleMessageId(), handler);
    }

    public void registerConsumerMessageHandler(ConsumerMessageHandler handler) {
        ConsumerMessageHandler old = c2gHandlerMap.get(handler.messageId());
        if (old != null) {
            Assert.error(handler.messageId() + " -> " + MessageIdReader.read(handler.messageId()) + "  ????????????????????????"
                    + " ?????? " + old.getClass().getName() + " ?????? " + handler.getClass().getName());
        }
        c2gHandlerMap.put(handler.messageId(), handler);
    }

    /**
     * ????????????+1
     */
    public void retainService() {
        serviceRefCount++;
    }

    public void releaseService() {
        serviceRefCount--;

    }

    public void releaseAndTryShutdownService() {
        serviceRefCount--;
        if (serviceRefCount <= 0) {
            service.shutdownGracefully();
        }
    }

    public void shutdownService() {
        if (serviceRefCount <= 0) {
            service.shutdownGracefully();
        } else {
            logger.warn("service ????????????{}????????????????????????", serviceRefCount);
        }
    }


    public void report() {
        logger.info("csLoginMessageId: {} scLoginMessageId:{} ", csLoginMessageId, scLoginMessageId);
    }

    public void channelActive(Channel channel) {
        Long token = idGenerator.nextId();
        ChannelAttributeUtil.setToken(channel, token);
        tokenChannel.putIfAbsent(token, channel);
        logger.debug("{} ?????? token {}", channel, token);
    }

    //?????????????????????????????????????????????
    public void execute(final Channel channel, final GatewayReceiveConsumerMessage message) {
        long token = ChannelAttributeUtil.getToken(channel);
        message.setToken(token);
        Long userId = ChannelAttributeUtil.getUserId(channel);
        if (userId != null) {
            message.setUserId(userId);
        }
        service.get(token).execute(() -> {
            try {
                ConsumerMessageHandler handler = c2gHandlerMap.get(message.getMessageId());
                if (handler != null) {
                    handler.execute(channel, message);
                    if (handler.stopForward()) {
                        return;
                    }
                }
                //??????????????????????????????
                HandleMessageManager handleMessageManager = handleMessageManagerMap.get(message.getMessageId());
                if (handleMessageManager == null) {
                    logger.warn("????????????????????????????????????{}", message.getMessageId());
                    SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();

                    errorMessage.setCode(Constant.ERROR_NOT_FOUND_SERVER);
                    errorMessage.getArgs().add(String.valueOf(message.getMessageId()));
                    errorMessage.setMessage("?????????????????????" + MessageIdReader.read(message.getMessageId()));
                    sendMessage2Consumer(message.getRequestId(), message.getToken(), errorMessage);
                    return;
                }

                handleMessageManager.execute(message);
            } catch (Exception e) {
                logger.error("?????????????????? " + message.getMessageId(), e);
                SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();

                errorMessage.setCode(Constant.ERROR_SERVER_ERROR);
                errorMessage.getArgs().add(String.valueOf(message.getMessageId()));
                errorMessage.setMessage(MessageIdReader.read(message.getMessageId()) + "," + e.getMessage());
                sendMessage2Consumer(message.getRequestId(), message.getToken(), errorMessage);
            }
        });
    }

    public void sendMessage2Consumer(int requestId, Long token, Message message) {
        Channel consumerChannel = tokenChannel.get(token);
        if (consumerChannel == null) {
            logger.warn("????????????channel token {}", token);
        } else {
            GatewayReceiveProviderMessage m = new GatewayReceiveProviderMessage();
            m.setRequestId(requestId);
            ByteBuf buf = Unpooled.buffer(message.serializedSize());
            message.write(buf);
            byte[] data = new byte[message.serializedSize()];
            buf.readBytes(data);
            m.setToken(token);
            m.setData(data);
            m.setMessageId(message.messageId());
            if (consumerChannel.isWritable()) {
                consumerChannel.writeAndFlush(m);
            }

        }
    }

    public void sendMessage2Consumer(Long token, int messageId, byte[] data) {
        Channel consumerChannel  = tokenChannel.get(token);
        if (consumerChannel  == null) {
            logger.warn("????????????channel token {}", token);
        } else {
            GatewayReceiveProviderMessage m = new GatewayReceiveProviderMessage();
            m.setRequestId(0);
            m.setToken(token);
            m.setData(data);
            m.setMessageId(messageId);
            if (consumerChannel .isWritable()) {
                consumerChannel .writeAndFlush(m);
            }

        }
    }


    public void sendMessage2Producer(Channel channel, Message message) {
        GatewayReceiveConsumerMessage toMessage = createMessage(message);
        if (channel.isWritable()) {
            channel.writeAndFlush(toMessage);
        }


    }
    public void init() {
        if (init) {
            logger.warn("messageExecutor ???????????????");
            return;
        }
        Assert.notNull(gateway, "gateway ????????????????????????");
        init = true;
        Assert.isTrue(csLoginMessageId > 0 && scLoginMessageId > 0, "?????????????????????");
        startCheck();
    }

    //?????????????????????????????????
    public void execute(Channel channel, final GatewayReceiveProviderMessage message) {
        long token = message.getToken();
        service.get(token).execute(() -> {
            try {
                ProviderMessageHandler handler = p2gHandlerMap.get(message.getMessageId());
                if (handler != null) {
                    handler.execute(channel, message);
                    if (handler.stopResponse()) {
                        return;
                    }
                }
                if (message.getUserIds().length == 0) {
                    Channel consumerChannel = tokenChannel.get(token);
                    if (consumerChannel == null) {
                        logger.warn("????????????channel token:{}", token);
                    } else {
                        if (consumerChannel.isWritable()) {
                            consumerChannel.writeAndFlush(message);
                        }

                    }
                } else {
                    for (Long userId : message.getUserIds()) {
                        //?????????
                        if (userId == 0L) {
                            for (Map.Entry<Long, Channel> entry : userClientChannel.entrySet()) {
                                Channel consumerChannel = entry.getValue();
                                if (consumerChannel.isWritable()) {
                                    consumerChannel.writeAndFlush(message);
                                }
                            }
                            break;
                        } else {
                            Channel consumerChannel = userClientChannel.get(userId);
                            if (consumerChannel == null) {
                                logger.warn("?????????????????? :{}", userId);
                            } else {
                                if (consumerChannel.isWritable()) {
                                    consumerChannel.writeAndFlush(message);
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("???????????????????????????????????????", e);
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
     * ???????????????????????????????????????????????????????????????token??????
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
     * ???????????????
     *
     * @param channel
     */
    public void consumerOffline(Channel channel) {
        service.execute(() -> {
            Long token = ChannelAttributeUtil.getToken(channel);
            Long userId = ChannelAttributeUtil.getUserId(channel);
            userId = userId == null ? 0 : userId;
            tokenChannel.remove(token);
            consumerOffline(channel, token, userId);
        });
    }

    public void readMessage(Message message, GatewayReceiveProviderMessage gatewayReceiveProviderMessage) {
        ByteBuf buf = Unpooled.buffer(gatewayReceiveProviderMessage.getData().length);
        buf.writeBytes(gatewayReceiveProviderMessage.getData());
        message.read(buf, buf.writerIndex());
    }


    public GatewayReceiveConsumerMessage createMessage(Message message) {
        GatewayReceiveConsumerMessage toMessage = new GatewayReceiveConsumerMessage();
        toMessage.setMessageId(message.messageId());
        ByteBuf buf = Unpooled.buffer(message.serializedSize());
        message.write(buf);
        byte[] data = new byte[message.serializedSize()];
        buf.readBytes(data);
        toMessage.setData(data);
        return toMessage;
    }


    public void sendMessage(Provider provider, Message message) {
        GatewayReceiveConsumerMessage toMessage = createMessage(message);
        provider.sendMessage(toMessage);
    }





    private void checkWaitRelationTask() {
        List<Long> tokens = new ArrayList<>();
        for (Map.Entry<Long, WaitRelationTask> entry : waitRelationMap.entrySet()) {
            WaitRelationTask task = entry.getValue();
            if (task.check()) {
                tokens.add(entry.getKey());
                service.get(task.getToken()).execute(task::sendMessage);
            } else {
                if (task.cancel()) {
                    tokens.add(entry.getKey());
                    service.get(task.getToken()).execute(() -> task.sendCancelMessage(this));
                }
            }
        }
        for (Long token : tokens) {
            waitRelationMap.remove(token);
        }
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
                //??????
                if (now - waitAskTask.getStartTime() > waitAskTask.getMaxDelay()) {
                    logger.debug("????????????????????? {} ??????{} ????????? ??????????????? {} ??????????????? {}",
                            MessageIdReader.read(waitAskTask.getFromMessageId()), waitAskTask.getValue(),
                            waitAskTask.getAskTimes(), waitAskTask.getAnswerTimes());
                    tokens.add(entry.getKey());
                    SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();
                    errorMessage.setCode(Constant.ERROR_NOT_HANDLE_VALUE_REQUEST);
                    errorMessage.getArgs().add(String.valueOf(waitAskTask.getFromMessageId()));
                    errorMessage.setMessage(MessageIdReader.read(waitAskTask.getFromMessageId()));
                    errorMessage.getArgs().add(Arrays.toString(waitAskTask.getValue()));
                    sendMessage2Consumer(waitAskTask.getRequestId(), waitAskTask.getMessage().getToken(), errorMessage);
                }
            }
        }
        for (Long token : tokens) {
            waitAskMap.remove(token);
        }
    }

    private void startCheck() {
        service.scheduleWithFixedDelay(() -> {
            checkWaitRelationTask();
            checkWaitAskTask();
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void destroy() {
        service.shutdownGracefully();
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

    public ServerProperties.Gateway getGateway() {
        return gateway;
    }

    public void setGateway(ServerProperties.Gateway gateway) {
        this.gateway = gateway;
    }


    public static void main(String[] args) {

    }
}
