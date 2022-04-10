package com.senpure.io.server.consumer;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.Constant;
import com.senpure.io.server.MessageFrame;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.consumer.handler.ConsumerMessageHandler;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.remoting.AbstractMessageExecutor;
import com.senpure.io.server.remoting.MessageSender;
import com.senpure.io.server.remoting.Response;
import com.senpure.io.server.remoting.ResponseCallback;
import io.netty.channel.Channel;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

public class ConsumerMessageExecutor extends AbstractMessageExecutor {
    private final ConsumerMessageHandlerContext handlerContext;

    private final ProviderManager providerManager;

    public ConsumerMessageExecutor(TaskLoopGroup service,
                                   ServerProperties.ConsumerProperties properties,
                                   ConsumerMessageHandlerContext handlerContext, ProviderManager providerManager) {
        super(service);
        String[] ids = StringUtils.commaDelimitedListToStringArray(properties.getScErrorMessageId());
        for (String id : ids) {
            addErrorMessage(Integer.parseInt(id));
        }
        this.handlerContext = handlerContext;
        this.providerManager = providerManager;
    }

    public void execute(Channel channel, ConsumerMessage frame) {
        int messageType = frame.messageType();
        if (messageType == MessageFrame.MESSAGE_TYPE_SC) {
            executeSCMessage(channel, frame);
        } else if (messageType == MessageFrame.MESSAGE_TYPE_CS) {
            executeCSMessage(channel, frame);
        } else {
            executeSCMessage(channel, frame);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void executeCSMessage(Channel channel, ConsumerMessage frame) {
        service.execute(() -> {
            ConsumerMessageHandler handler = handlerContext.handler(frame.messageId());
            if (handler == null) {
                logger.warn("没有找到消息处理程序 {} ", frame.messageId());

                SCFrameworkErrorMessage scFrameworkErrorMessage = new SCFrameworkErrorMessage();
                scFrameworkErrorMessage.setCode(Constant.ERROR_NOT_HANDLE_REQUEST);
                scFrameworkErrorMessage.setMessage("服务器没有处理程序:" + frame.messageId());
                scFrameworkErrorMessage.getArgs().add(String.valueOf(frame.messageId()));
                providerManager.sendMessage(scFrameworkErrorMessage);

            } else {
                try {
                    MessageSender.REQUEST_ID.set(frame.requestId());
                    handler.execute(channel, frame.message());
                } catch (Exception e) {
                    logger.error("执行handler[" + handler.getClass().getName() + "]逻辑出错 ", e);
                    SCFrameworkErrorMessage scFrameworkErrorMessage = new SCFrameworkErrorMessage();
                    scFrameworkErrorMessage.setMessage("服务器执行错误:" + frame.message().getClass().getSimpleName()
                            + "[" + frame.messageId() + "]:" +
                            e.getMessage());
                    scFrameworkErrorMessage.setCode(Constant.ERROR_PROVIDER_ERROR);
                    scFrameworkErrorMessage.getArgs().add(String.valueOf(frame.messageId()));
                    providerManager.sendMessage(scFrameworkErrorMessage);
                } finally {
                    MessageSender.REQUEST_ID.remove();
                }

            }
        });

    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void executeSCMessage(Channel channel, ConsumerMessage frame) {
        service.execute(() -> {
            int requestId = frame.requestId();
            if (requestId != 0) {
                receive(channel, requestId, frame.message());
            } else {
                ConsumerMessageHandler handler = handlerContext.handler(frame.messageId());
                if (handler == null) {
                    logger.warn("没有找到消息处理程序{} ", frame.messageId());
                } else {
                    try {
                        handler.execute(channel, frame.message());
                    } catch (Exception e) {
                        logger.error("执行handler[" + handler.getClass().getName() + "]逻辑出错 ", e);
                    }

                }
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void executeSCHandler(Channel channel, Message message) {
        ConsumerMessageHandler handler = handlerContext.handler(message.messageId());
        if (handler == null) {
            logger.warn("没有找到消息处理程序{} ", message.messageId());
        } else {
            try {
                handler.execute(channel, message);
            } catch (Exception e) {
                logger.error("执行handler[" + handler.getClass().getName() + "]逻辑出错 ", e);
            }

        }
    }


    public <T extends Message> ResponseCallback handlerCallback() {

        return response -> executeSCHandler(response.getChannel(), response.getMessage());
    }

    public <T extends Message> ResponseCallback successCallback(Consumer<T> consumer) {

        return new SuccessCallback<T>() {
            @Override
            public void success(T message) {
                consumer.accept(message);
            }
        };
    }

    private abstract class SuccessCallback<T extends Message> implements ResponseCallback {

        @Override
        public void execute(Response result) {
            if (result.isSuccess()) {
                success(result.getMessage());
            } else {
                error(result.getChannel(), result.getMessage());
            }
        }

        public abstract void success(T message);

        public void error(Channel channel, Message message) {
            executeSCHandler(channel, message);
        }
    }


    public static void main(String[] args) {

    }
}
