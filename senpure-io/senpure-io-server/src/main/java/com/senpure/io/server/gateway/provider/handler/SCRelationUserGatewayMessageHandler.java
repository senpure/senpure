package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.gateway.WaitRelationTask;
import com.senpure.io.server.protocol.message.CSBreakUserGatewayMessage;
import com.senpure.io.server.protocol.message.SCRelationUserGatewayMessage;
import io.netty.channel.Channel;

public class SCRelationUserGatewayMessageHandler extends AbstractProviderMessageHandler {
    @Override
    public void execute(Channel channel, GatewayReceiveProviderMessage gatewayReceiveProviderMessage) {
        SCRelationUserGatewayMessage message = new SCRelationUserGatewayMessage();
        messageExecutor.readMessage(message, gatewayReceiveProviderMessage);
        WaitRelationTask waitRelationTask =  messageExecutor.waitRelationMap.get(message.getRelationToken());
        if (waitRelationTask != null) {
            if (message.getRelationToken() == waitRelationTask.getRelationToken()) {
                waitRelationTask.setRelationTime(System.currentTimeMillis());
                waitRelationTask.setRelation(true);
            }
        } else {
            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
            breakUserGatewayMessage.setToken(message.getToken());
            breakUserGatewayMessage.setUserId(message.getUserId());
            breakUserGatewayMessage.setRelationToken(message.getRelationToken());
            messageExecutor.sendMessage2Producer(channel, breakUserGatewayMessage);
        }
    }

    @Override
    public int handleMessageId() {
        return SCRelationUserGatewayMessage.MESSAGE_ID;
    }
}
