package com.senpure.io.server.gateway.provider.handler;


import com.senpure.base.util.Assert;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayMessageExecutor;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

public abstract class AbstractGatewayProviderMessageHandler implements GatewayProviderMessageHandler, InitializingBean {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    protected GatewayMessageExecutor messageExecutor;

    protected void readMessage(Message message, GatewayReceiveProviderMessage frame) {
        if (message.messageId() != frame.messageId()) {
            Assert.error("消息id不匹配" + MessageIdReader.read(message.messageId()) + "  " + MessageIdReader.read(frame.messageId()));
        }
        messageExecutor.readMessage(message, frame);
    }

    @Override
    public void execute(Channel channel, GatewayReceiveProviderMessage frame) {
        if (!ChannelAttributeUtil.isFramework(channel)) {
            SCFrameworkErrorMessage returnMessage = new SCFrameworkErrorMessage();
            returnMessage.setCode(Constant.ERROR_GATEWAY_ERROR);
            returnMessage.setMessage(MessageIdReader.read(frame.messageId()) + "请先经过认证");
            messageExecutor.responseMessage2Producer(frame.requestId(), channel, returnMessage);
            return;
        }

        executeFramework(channel, frame);
    }

    public abstract void executeFramework(Channel channel, GatewayReceiveProviderMessage frame);


    @Override
    public void afterPropertiesSet() {
        messageExecutor.registerProviderMessageHandler(this);

    }
}
