package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.CSFrameworkVerifyProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCFrameworkVerifyProviderMessage;
import io.netty.channel.Channel;

public class DefaultCSFrameworkVerifyProviderMessageHandler extends AbstractGatewayProviderMessageHandler implements CSFrameworkVerifyProviderMessageHandler {


    @Override
    public void execute(Channel channel, GatewayReceiveProviderMessage frame) {
        verify(channel, frame);
    }

    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {


    }

    private synchronized void verify(Channel channel, GatewayReceiveProviderMessage frame) {
        CSFrameworkVerifyProviderMessage message = new CSFrameworkVerifyProviderMessage();
        messageExecutor.readMessage(message, frame);

        if (message.getServerName() == null || message.getServerKey() == null) {

            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("参数错误");
            messageExecutor.responseMessage2Producer(frame.requestId(), channel, errorMessage);
            return;
        }
        ProviderManager providerManager = messageExecutor.getFrameworkVerifyProviderManager();

        if (providerManager == null) {
            providerManager = new ProviderManager(messageExecutor);
            providerManager.setServerName(message.getServerName());
            providerManager = messageExecutor.addFrameworkVerifyProviderManager(providerManager);

        }
        String verifyName = providerManager.getServerName();
        if (!verifyName.equals(message.getServerName())) {
            logger.warn("已经存在认证服务，且服务名不相同 {} -> {}", verifyName, message.getServerName());
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_GATEWAY_ERROR);
            errorMessage.setMessage("已经存在认证服务，且服务名不相同 " + verifyName + " -> " + message.getServerName());
            messageExecutor.responseMessage2Producer(frame.requestId(), channel, errorMessage);
            return;
        }
        Provider provider = providerManager.getProvider(message.getServerKey());
        if (provider == null) {
            String serverKey = message.getServerKey();
            provider = messageExecutor.createProvider(serverKey);
            provider = providerManager.addProvider(provider);
        }


        ChannelAttributeUtil.setRemoteServerName(channel, message.getServerName());
        ChannelAttributeUtil.setRemoteServerKey(channel, message.getServerKey());
        provider.addChannel(channel);

        SCFrameworkVerifyProviderMessage providerMessage = new SCFrameworkVerifyProviderMessage();
        messageExecutor.responseMessage2Producer(frame.requestId(), channel, providerMessage);

    }

    @Override
    public int messageId() {
        return CSFrameworkVerifyProviderMessage.MESSAGE_ID;
    }
}
