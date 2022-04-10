package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.CSMatchingConsumerMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCMatchingConsumerMessage;
import io.netty.channel.Channel;

public class DefaultCSMatchingConsumerMessageHandler extends AbstractGatewayProviderMessageHandler implements CSMatchingConsumerMessageHandler {
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        CSMatchingConsumerMessage message = new CSMatchingConsumerMessage();
        readMessage(message, frame);
        ProviderManager providerManager = messageExecutor.getProviderManager(message.getServerName());
        if (providerManager != null) {
            Provider provider = providerManager.nextProvider();
            if (provider != null) {
                providerManager.sendMessage(provider, frame, response -> {
                    if (response.isSuccess()) {
                        SCMatchingConsumerMessage receiveMessage = response.getMessage();
                        SCMatchingConsumerMessage scMatchingConsumerMessage = new SCMatchingConsumerMessage();
                        scMatchingConsumerMessage.setServerName(message.getServerName());
                        scMatchingConsumerMessage.setServerKey(provider.getRemoteServerKey());
                        scMatchingConsumerMessage.setMatchableId(receiveMessage.getMatchableId());
                        messageExecutor.responseMessage2Producer(frame.requestId(), channel, scMatchingConsumerMessage);

                    } else {
                        messageExecutor.responseMessage2Producer(frame.requestId(), channel, response.getMessage());

                    }

                });


            }

        }

        SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
        errorMessage.setCode(Constant.ERROR_PROVIDER_ERROR);
        errorMessage.setMessage("请求匹配失败");
        messageExecutor.responseMessage2Producer(frame.requestId(), channel, errorMessage);


        //  SCMatchingMessage message = new SCMatchingMessage();
        //  messageExecutor.sendMessage2Producer(channel, message);

    }

    @Override
    public int messageId() {
        return CSMatchingConsumerMessage.MESSAGE_ID;
    }
}
