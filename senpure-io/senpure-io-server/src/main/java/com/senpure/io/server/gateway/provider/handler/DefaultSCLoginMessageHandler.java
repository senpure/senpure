package com.senpure.io.server.gateway.provider.handler;

import com.senpure.io.server.ChannelAttributeUtil;
import com.senpure.io.server.Constant;
import com.senpure.io.server.gateway.GatewayReceiveProviderMessage;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import io.netty.channel.Channel;

public class DefaultSCLoginMessageHandler extends DefaultSCFrameworkVerifyMessageHandler implements  SCLoginMessageHandler {

    @Override
    public void executeFramework(Channel channel, GatewayReceiveProviderMessage frame) {
        if (frame.getUserIds().length == 0) {
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("请将认证后的userId 放在 userIds第一位返回");
            messageExecutor.sendMessage2Producer(channel, errorMessage);
            return;
        }
        long userId = frame.getUserIds()[0];
        if (userId == 0) {
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("请将认证后的userId 放在 userIds第一位返回");
            messageExecutor.sendMessage2Producer(channel, errorMessage);
            return;
        }
        if (userId <= Constant.MAX_FRAMEWORK_USER_ID) {

            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("外部玩家认证id必须大于" + Constant.MAX_FRAMEWORK_USER_ID + "请修改相关程序");
            messageExecutor.sendMessage2Producer(channel, errorMessage);
            //todo tongzhi diaoyoufang
            //  messageExecutor.sendMessage2Consumer(frame.getToken(), errorMessage);
            return;
        }
        Channel consumerChannel = messageExecutor.prepLoginChannels.remove(frame.getToken());
        if (consumerChannel != null) {
            Long token = ChannelAttributeUtil.getToken(consumerChannel);
            Long oldUserId = ChannelAttributeUtil.getUserId(consumerChannel);
            if (oldUserId == null) {
                messageExecutor.providerManagerForEach(providerManager -> providerManager.afterUserAuthorize(token, userId));

            } else {
                if (oldUserId == userId) {
                    logger.info("{}重复登陆 {} 不做额外的处理", consumerChannel, userId);
                } else {
                    logger.info("{}切换账号{}  -》  {} ", consumerChannel, oldUserId, userId);
                    messageExecutor.consumerUserChange(consumerChannel, token, oldUserId);
                }
            }
            ChannelAttributeUtil.setUserId(consumerChannel, userId);
            messageExecutor.userClientChannel.put(userId, consumerChannel);

        } else {
            logger.warn("登录成功 userId:{} channel缺失 token{}", userId, frame.getToken());
        }
    }

    @Override
    public int messageId() {
        return messageExecutor.getScLoginMessageId();
    }

    @Override
    public boolean stopConsumer() {
        return false;
    }
}
