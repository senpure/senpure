package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.HandleMessageManager;
import com.senpure.io.server.gateway.ProviderManager;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.protocol.bean.HandleMessage;
import com.senpure.io.server.protocol.message.CSRegServerHandleMessageMessage;
import com.senpure.io.server.protocol.message.SCRegServerHandleMessageMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class SCRegServerHandleMessageMessageHandler extends AbstractProviderMessageHandler {

    @Override
    public void execute(Channel channel, GatewayReceiveProviderMessage gatewayReceiveProviderMessage) {
        //todo 没有加锁 线程安全问题
        //todo 一个服务只允许一个ask id
        StringBuilder sb = new StringBuilder();
        try {
            SCRegServerHandleMessageMessage message = new SCRegServerHandleMessageMessage();
            messageExecutor.readMessage(message, gatewayReceiveProviderMessage);
            List<HandleMessage> handleMessages = message.getMessages();
            String serverKey = message.getServerKey();
            ChannelAttributeUtil.setRemoteServerName(channel, message.getServerName());
            ChannelAttributeUtil.setRemoteServerKey(channel, serverKey);
            logger.info("服务注册:{}:{} [{}]", message.getServerName(), message.getServerKey(), message.getReadableServerName());
            for (HandleMessage handleMessage : handleMessages) {
                logger.info("{}", handleMessage);
            }
            ConcurrentMap<String, ProviderManager> producerManagerMap = messageExecutor.providerManagerMap;
            ConcurrentMap<Integer, ProviderManager> messageHandleMap = messageExecutor.messageHandleMap;
            ProviderManager providerManager = producerManagerMap.get(message.getServerName());
            if (providerManager == null) {
                providerManager = new ProviderManager(messageExecutor);
                producerManagerMap.put(message.getServerName(), providerManager);
                for (HandleMessage handleMessage : handleMessages) {
                    providerManager.markHandleId(handleMessage.getHandleMessageId());
                    messageHandleMap.putIfAbsent(handleMessage.getHandleMessageId(), providerManager);
                }
                providerManager.setServerName(message.getServerName());
            }
            //如果同一个服务处理消息id不一致，旧得实例停止接收新的连接
            for (HandleMessage handleMessage : handleMessages) {
                if (!providerManager.handleId(handleMessage.getHandleMessageId())) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(message.getServerName()).append(" 处理了新的消息").append(handleMessage.getHandleMessageId()).append("[")
                            .append(handleMessage.getMessageName()).append("] ,旧的服务器停止接收新的请求分发");
                    logger.info("{} 处理了新的消息{}[{}] ，旧的服务器停止接收新的请求分发", message.getServerName(),
                            handleMessage.getHandleMessageId(), handleMessage.getMessageName());
                    providerManager.prepStopOldInstance();
                    for (HandleMessage hm : handleMessages) {
                        providerManager.markHandleId(hm.getHandleMessageId());
                    }
                    break;
                }
            }
            for (Integer id : providerManager.getHandleIds()) {
                boolean discard = true;
                for (HandleMessage handleMessage : handleMessages) {
                    if (handleMessage.getHandleMessageId() == id) {
                        discard = false;
                        break;
                    }
                }
                if (discard) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(message.getServerName()).append(" 丢弃了消息 ").append(MessageIdReader.read(id))
                            .append(" ，旧的服务器停止接收新的请求分发");
                    logger.info("{} 丢弃了消息 {} ，旧的服务器停止接收新的请求分发", message.getServerName(), MessageIdReader.read(id));
                    providerManager.prepStopOldInstance();
                    for (HandleMessage hm : handleMessages) {
                        providerManager.markHandleId(hm.getHandleMessageId());
                    }
                    break;
                }
            }
            Provider provider = providerManager.getProducer(serverKey);
            provider.addChannel(channel);
            providerManager.checkChannelServer(serverKey, provider);
            ConcurrentMap<Integer, HandleMessageManager> handleMessageManagerMap = messageExecutor.handleMessageManagerMap;
            for (HandleMessage handleMessage : handleMessages) {
                HandleMessageManager handleMessageManager = handleMessageManagerMap.get(handleMessage.getHandleMessageId());
                if (handleMessageManager == null) {
                    handleMessageManager = new HandleMessageManager(handleMessage.getHandleMessageId(), handleMessage.isDirect(), messageExecutor);
                    handleMessageManagerMap.put(handleMessage.getHandleMessageId(), handleMessageManager);
                }
                handleMessageManager.addProviderManager(handleMessage.getHandleMessageId(), providerManager);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            CSRegServerHandleMessageMessage returnMessage = new CSRegServerHandleMessageMessage();
            returnMessage.setSuccess(false);
            returnMessage.setMessage(e.getMessage());
            messageExecutor.sendMessage2Producer(channel, returnMessage);
            return;
        }
        CSRegServerHandleMessageMessage returnMessage = new CSRegServerHandleMessageMessage();
        returnMessage.setSuccess(true);
        if (sb.length() > 0) {
            returnMessage.setMessage(sb.toString());
        }
        messageExecutor.sendMessage2Producer(channel, returnMessage);
    }

    @Override
    public int handleMessageId() {
        return SCRegServerHandleMessageMessage.MESSAGE_ID;
    }

}
