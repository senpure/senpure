package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.CSRelationUserGatewayMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.protocol.message.SCRelationUserGatewayMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;

public class DefaultSCRelationUserGatewayMessageHandler extends AbstractGatewayProviderMessageHandler  implements  SCRelationUserGatewayMessageHandler{
    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        SCRelationUserGatewayMessage message = new SCRelationUserGatewayMessage();
        messageExecutor.readMessage(message, frame);
        int requestId = frame.requestId();
        if (requestId != 0) {
            messageExecutor.receive(channel, requestId, frame);
        } else {
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_CONSUMER_ERROR);
            errorMessage.setMessage(MessageIdReader.read(CSRelationUserGatewayMessage.MESSAGE_ID) + " requestId == 0 不用响应");
            messageExecutor.sendMessage2Producer(channel, message);
        }


//        WaitRelationTask waitRelationTask =  messageExecutor.waitRelationMap.get(message.getRelationToken());
//        if (waitRelationTask != null) {
//            if (message.getRelationToken() == waitRelationTask.getRelationToken()) {
//                waitRelationTask.setRelationTime(System.currentTimeMillis());
//                waitRelationTask.setRelation(true);
//            }
//        } else {
//            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
//            breakUserGatewayMessage.setToken(message.getToken());
//            breakUserGatewayMessage.setUserId(message.getUserId());
//            breakUserGatewayMessage.setRelationToken(message.getRelationToken());
//            messageExecutor.sendMessage2Producer(channel, breakUserGatewayMessage);
//        }
    }

    @Override
    public int messageId() {
        return SCRelationUserGatewayMessage.MESSAGE_ID;
    }
}
