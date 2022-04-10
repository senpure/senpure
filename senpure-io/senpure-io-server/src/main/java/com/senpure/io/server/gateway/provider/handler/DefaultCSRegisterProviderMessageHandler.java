package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.HandleMessageManager;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.bean.HandleMessage;
import com.senpure.io.server.protocol.message.CSRegisterProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCRegisterProviderMessage;
import com.senpure.io.server.remoting.ChannelService;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class DefaultCSRegisterProviderMessageHandler extends AbstractGatewayProviderMessageHandler implements CSRegisterProviderMessageHandler {

    @Override
    public synchronized void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        //todo 一个服务只允许一个ask id
        StringBuilder sb = new StringBuilder();
        try {
            CSRegisterProviderMessage message = new CSRegisterProviderMessage();
            messageExecutor.readMessage(message, frame);
            List<HandleMessage> handleMessages = message.getMessages();
            String serverKey = message.getServerKey();
            String lastServerName = ChannelAttributeUtil.getRemoteServerName(channel);
            String lastServerKey = ChannelAttributeUtil.getRemoteServerName(channel);
            if (lastServerName != null) {
                if (!lastServerName.equals(message.getServerName()) || lastServerKey.equals(message.getServerKey())) {
                    return;
                }
            }
            ChannelAttributeUtil.setRemoteServerName(channel, message.getServerName());
            ChannelAttributeUtil.setRemoteServerKey(channel, serverKey);
            logger.info("服务注册:{}:{} [{}]", message.getServerName(), message.getServerKey(), message.getReadableServerName());
            for (HandleMessage handleMessage : handleMessages) {
                logger.info("{}", handleMessage);
            }
            ConcurrentMap<Integer, ProviderManager> messageHandleMap = messageExecutor.messageHandleMap;
            ProviderManager providerManager = messageExecutor.getProviderManager(message.getServerName());

            if (providerManager == null) {
                providerManager = new ProviderManager(messageExecutor);
                providerManager.setServerName(message.getServerName());
                providerManager = messageExecutor.addProviderManager(providerManager);
            }
            if (!providerManager.isRegisterMessageId()) {

                for (HandleMessage handleMessage : handleMessages) {
                    providerManager.markHandleId(handleMessage.getHandleMessageId());
                    messageHandleMap.putIfAbsent(handleMessage.getHandleMessageId(), providerManager);
                }
                providerManager.setRegisterMessageId(true);
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
            Provider provider = providerManager.getProvider(serverKey);
            if (provider == null) {
                provider = new Provider(messageExecutor.getService());
                provider.setChannelService(new ChannelService.MultipleChannelService(serverKey));
                provider.setRemoteServerKey(serverKey);
                provider.setFutureService(messageExecutor);
                provider.verifyWorkable();
                provider = providerManager.addProvider(provider);
            }


            provider.addChannel(channel);
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
            SCFrameworkErrorMessage returnMessage = new SCFrameworkErrorMessage();
            returnMessage.setCode(Constant.ERROR_GATEWAY_ERROR);
            returnMessage.setMessage(e.getMessage());
            messageExecutor.responseMessage2Producer(frame.requestId(), channel, returnMessage);
            return;
        }
        SCRegisterProviderMessage returnMessage = new SCRegisterProviderMessage();
        if (sb.length() > 0) {
            returnMessage.setMessage(sb.toString());
        }
        messageExecutor.responseMessage2Producer(frame.requestId(), channel, returnMessage);
    }

    @Override
    public int messageId() {
        return CSRegisterProviderMessage.MESSAGE_ID;
    }

}
