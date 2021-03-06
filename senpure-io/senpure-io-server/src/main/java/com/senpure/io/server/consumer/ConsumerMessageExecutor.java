package com.senpure.io.server.consumer;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.consumer.handler.ConsumerMessageHandler;
import com.senpure.io.server.consumer.remoting.DefaultFuture;
import com.senpure.io.server.consumer.remoting.DefaultResponse;
import com.senpure.io.server.consumer.remoting.Response;
import com.senpure.io.server.protocol.message.SCInnerErrorMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/**
 * ConsumerMessageExecutor
 *
 * @author senpure
 * @time 2019-06-28 17:01:34
 */
public class ConsumerMessageExecutor {

    private final Logger logger = LoggerFactory.getLogger(ConsumerMessageExecutor.class);
    private TaskLoopGroup service;
    private int serviceRefCount = 0;
    private final Set<Integer> errorMessageIds = new HashSet<>();


    private final ConsumerMessageHandlerContext handlerContext;

    public ConsumerMessageExecutor(ServerProperties.Consumer properties, ConsumerMessageHandlerContext handlerContext) {
        errorMessageIds.add(SCInnerErrorMessage.MESSAGE_ID);
        errorMessageIds.add(properties.getScErrorMessageId());
        this.handlerContext = handlerContext;
    }


    public void setService(TaskLoopGroup service) {
        this.service = service;
    }

    public ScheduledExecutorService getService() {
        return service;
    }

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Channel channel, ConsumerMessage frame) {
        service.execute(() -> {
            int requestId = frame.getRequestId();
            Message message = frame.getMessage();
            if (requestId == 0) {

                ConsumerMessageHandler handler = handlerContext.handler(message.messageId());
                if (handler == null) {
                    logger.warn("??????????????????????????????{} ", message.messageId());
                } else {
                    try {
                        handler.execute(channel, message);
                    } catch (Exception e) {
                        logger.error("??????handler[" + handler.getClass().getName() + "]???????????? ", e);
                    }

                }
            } else {
                DefaultFuture future = DefaultFuture.received(requestId);
                if (future != null) {
                    if (isErrorMessage(message)) {
                        Response response = new DefaultResponse(channel, null, message);
                        future.doReceived(response);
                    } else {
                        Response response = new DefaultResponse(channel, message, null);
                        future.doReceived(response);
                    }
                } else {
                    logger.warn("?????????????????????????????????,????????????????????????????????? {}", frame);
                }

            }
        });


    }

    public boolean isErrorMessage(Message message) {
        return errorMessageIds.contains(message.messageId());
    }


}
