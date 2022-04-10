package com.senpure.io.server.provider;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.Constant;
import com.senpure.io.server.MessageFrame;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.provider.handler.ProviderMessageHandler;
import com.senpure.io.server.remoting.AbstractMessageExecutor;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

public class ProviderMessageExecutor extends AbstractMessageExecutor {

    private final MessageSender messageSender;
    private final ProviderMessageHandlerContext handlerContext;

    public ProviderMessageExecutor(TaskLoopGroup service,MessageSender messageSender, ProviderMessageHandlerContext handlerContext) {
        super(service);
        this.messageSender = messageSender;
        this.handlerContext = handlerContext;
        service.scheduleWithFixedDelay(this::checkTimeoutFuture, 0, 10, TimeUnit.MILLISECONDS);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void executeCSMessage(Channel channel, ProviderReceivedMessage frame) {
        long userId = frame.getUserId();
        long id = userId > 0 ? userId : frame.getToken();
        service.get(id).execute(() -> {
            ProviderMessageHandler handler = handlerContext.handler(frame.getMessageId());
            if (handler == null) {
                logger.warn("没有找到消息处理程序 {} token {} userId:{}", frame.getMessageId(), frame.getToken(), userId);

                long token = frame.getToken();
                if (token == 0) {
                    logger.error("内部处理handler{} 没有注册到 handlerContext", frame.getMessageId());
                    //  channel.writeAndFlush( scInnerErrorMessage);
                } else {
                    SCFrameworkErrorMessage scFrameworkErrorMessage = new SCFrameworkErrorMessage();
                    scFrameworkErrorMessage.setCode(Constant.ERROR_NOT_HANDLE_REQUEST);
                    scFrameworkErrorMessage.setMessage("服务器没有处理程序:" + frame.getMessageId());
                    scFrameworkErrorMessage.getArgs().add(String.valueOf(frame.getMessageId()));
                    messageSender.sendMessageByToken(token, scFrameworkErrorMessage);
                }

            } else {
                try {

                    MessageSender.REQUEST_ID.set(frame.getRequestId());
                    handler.execute(channel, frame.getToken(), userId, frame.getMessage());
                } catch (Exception e) {
                    logger.error("执行handler[" + handler.getClass().getName() + "]逻辑出错 ", e);
                    SCFrameworkErrorMessage scFrameworkErrorMessage = new SCFrameworkErrorMessage();
                    scFrameworkErrorMessage.setMessage("服务器执行错误:" + frame.getMessage().getClass().getSimpleName()
                            + "[" + frame.getMessageId() + "]:" +
                            e.getMessage());
                    scFrameworkErrorMessage.setCode(Constant.ERROR_PROVIDER_ERROR);
                    scFrameworkErrorMessage.getArgs().add(String.valueOf(frame.getMessageId()));
                    messageSender.sendMessageByToken(frame.getToken(), scFrameworkErrorMessage);
                } finally {
                    MessageSender.REQUEST_ID.remove();
                }

            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void executeSCMessage(Channel channel, ProviderReceivedMessage frame) {
        long userId = frame.getUserId();
        long id = userId > 0 ? userId : frame.getToken();
        service.get(id).execute(() -> {
            int requestId = frame.requestId();
            if (requestId == 0) {
                ProviderMessageHandler handler = handlerContext.handler(frame.getMessageId());
                if (handler == null) {
                    logger.warn("没有找到消息处理程序{} ", frame.messageId());
                } else {
                    try {
                        handler.execute(channel, frame.getToken(), frame.getUserId(), frame.getMessage());
                    } catch (Exception e) {
                        logger.error("执行handler[" + handler.getClass().getName() + "]逻辑出错 ", e);
                    }

                }

            } else {
                receive(channel, requestId, frame.message());
            }
        });

    }

    public void execute(Channel channel, ProviderReceivedMessage frame) {
        int messageType = frame.messageType();

        if (messageType == MessageFrame.MESSAGE_TYPE_CS) {
            executeCSMessage(channel, frame);
        } else if (messageType == MessageFrame.MESSAGE_TYPE_SC) {
            executeSCMessage(channel, frame);
        } else {
            executeCSMessage(channel, frame);
        }


    }


}
