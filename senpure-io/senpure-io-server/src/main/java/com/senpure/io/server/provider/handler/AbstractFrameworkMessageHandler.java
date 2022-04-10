package com.senpure.io.server.provider.handler;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.Constant;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import io.netty.channel.Channel;

/**
 * 服务A调用服务B的处理器
 */
public abstract class AbstractFrameworkMessageHandler<T extends Message> extends AbstractProviderMessageHandler<T> {


    @Override
    public void execute(Channel channel, long token, long userId, T message) throws Exception {
        if (userId == 0 || userId > Constant.MAX_FRAMEWORK_USER_ID) {
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setCode(Constant.ERROR_VERIFY_FAILURE);
            errorMessage.setMessage("[" + message.getClass().getSimpleName() + "]该协议调用必须经过内部认证");
            messageSender.respondMessageByToken(token, errorMessage);
            return;
        }
        execute(token, message);
    }

    //很多服务用的是同一个userId 所以使用token通信
    public abstract void execute(long token, T message);
}
