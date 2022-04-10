package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveConsumerMessage;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;
import com.senpure.io.server.protocol.message.CSFrameworkVerifyMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCFrameworkVerifyMessage;
import io.netty.channel.Channel;

public class DefaultCSFrameworkVerifyMessageHandler extends AbstractGatewayProviderMessageHandler implements CSFrameworkVerifyMessageHandler {

    private final boolean simpleVerify;
    private final String token;
    private final long userId;

    public DefaultCSFrameworkVerifyMessageHandler(boolean simpleVerify, String token, long userId) {
        this.simpleVerify = simpleVerify;
        this.token = token;
        this.userId = userId;
        if (simpleVerify && (userId == 0 || userId > Constant.MAX_FRAMEWORK_USER_ID)) {
            throw new RuntimeException("userId 不合法");
        }
    }

    @Override
    public void execute(Channel channel, GatewayReceiveProviderMessage frame) {

        executeFramework(channel, frame);
    }

    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        if (simpleVerify) {
            CSFrameworkVerifyMessage message = new CSFrameworkVerifyMessage();
            messageExecutor.readMessage(message, frame.getData());
            if (message.getToken().equals(token)) {
                ChannelAttributeUtil.setFramework(channel, true);
                SCFrameworkVerifyMessage frameworkVerifyMessage = new SCFrameworkVerifyMessage();
                frameworkVerifyMessage.setUserId(userId);
                messageExecutor.responseMessage2Producer(frame.requestId(), channel, frameworkVerifyMessage);
                logger.debug("simpleVerify {} provider 框架简单认证成功", channel);
            } else {
                logger.debug("simpleVerify {} provider 框架简单认证失败", channel);
                SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
                errorMessage.setMessage("框架内部简单认证失败");
                messageExecutor.responseMessage2Producer(frame.requestId(), channel, errorMessage);
                channel.close();
            }
        }


        ProviderManager providerManager = messageExecutor.getFrameworkVerifyProviderManager();
        if (providerManager == null) {
            logger.debug("{} provider 框架认证失败,没有认证服务提供者", channel);
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("框架内部认证失败,没有认证服务提供者，如果是初次使用可更改[网关]的配置来使用内置的简单认证器,server.io.gateway.simple-verify=true");

            messageExecutor.responseMessage2Producer(frame.requestId(), channel, errorMessage);
            return;
        }

        Provider provider = providerManager.nextProvider();
        if (provider == null) {
            logger.warn("{} 没有服务实例可以使用", providerManager.getServerName());
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("框架内部认证失败,没有认证服务实例");
            messageExecutor.responseMessage2Producer(frame.requestId(), channel, errorMessage);
            return;
        }
        GatewayReceiveConsumerMessage requestMessage = new GatewayReceiveConsumerMessage(frame.getMessageLength(), frame.getData());
        requestMessage.setMessageId(frame.messageId());
        requestMessage.setMessageType(frame.messageType());
        int requestId = frame.requestId();
        providerManager.sendMessage(provider, requestMessage, response -> {
            if (response.isSuccess()) {
                ChannelAttributeUtil.setFramework(channel, true);
                SCFrameworkVerifyMessage message = new SCFrameworkVerifyMessage();
                readMessage(message, response.getMessage());
                if (message.getUserId() == 0 || message.getUserId() > Constant.MAX_FRAMEWORK_USER_ID) {
                    SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                    errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
                    errorMessage.setMessage("userId不合法[" + message.getUserId() + "]应该 >0&&<=" + Constant.MAX_FRAMEWORK_USER_ID);
                    messageExecutor.sendMessage2Producer(response.getChannel(), response.getMessage());
                    channel.close();
                } else {
                    ChannelAttributeUtil.setUserId(channel, message.getUserId());
                    messageExecutor.responseMessage2Producer(requestId, channel, response.getMessage());
                }
            } else {
                logger.warn("{} provider 框架认证失败 {}", channel, response.getMessage());
//                SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
//                errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
//                errorMessage.setMessage("框架内部认证失败");
//                Message message = response.getMessage();
//                GatewayReceiveConsumerMessage returnMessage = new GatewayReceiveConsumerMessage(frame.getMessageLength(), frame.getData());
//                returnMessage.setMessageId(message.messageId());
//                returnMessage.setMessageType(message.messageType());
                messageExecutor.responseMessage2Producer(requestId, channel, response.getMessage());
                channel.close();
            }
        });


    }

    @Override
    public int messageId() {
        return CSFrameworkVerifyMessage.MESSAGE_ID;
    }
}
