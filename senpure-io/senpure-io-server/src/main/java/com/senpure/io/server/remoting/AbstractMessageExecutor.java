package com.senpure.io.server.remoting;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.Constant;
import com.senpure.io.server.protocol.message.SCFrameworkErrorMessage;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractMessageExecutor implements FutureService {
    private final Map<Integer, DefaultFuture> futureMap = new ConcurrentHashMap<>();
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Set<Integer> errorMessageIds = new HashSet<>();

    protected final TaskLoopGroup service;

    public AbstractMessageExecutor(TaskLoopGroup service) {
        this.service = service;
        errorMessageIds.add(SCFrameworkErrorMessage.MESSAGE_ID);
    }

    @Override
    public ResponseFuture future(Channel channel, int requestId, Message message, int timeout) {
        DefaultFuture future = new DefaultFuture(channel, requestId, message, timeout);
        futureMap.put(requestId, future);

        ScheduledFuture<?> scheduledFuture = service.schedule(() -> {
            logger.debug("{} {} {}  同步请求超时 ", requestId, MessageIdReader.read(future.getMessage().messageId()), timeout);
            SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
            errorMessage.setMessage("同步请求超时[" + future.getRequestId() + "][" + MessageIdReader.read(future.getMessage().messageId()) + "]:" + future.getTimeout());
            errorMessage.getArgs().add(String.valueOf(future.getRequestId()));
            errorMessage.getArgs().add(String.valueOf(future.getMessage().messageId()));
            errorMessage.getArgs().add(String.valueOf(timeout));
            errorMessage.setCode(Constant.ERROR_TIMEOUT);
            receive(channel, requestId, errorMessage, false);
        }, timeout, TimeUnit.MILLISECONDS);
        future.setScheduledFuture(scheduledFuture);

        return future;
    }

    public void receive(Channel channel, int requestId, Message message) {
        receive(channel, requestId, message, true);
    }

    public void receive(Channel channel, int requestId, Message message, boolean cancel) {
        DefaultFuture future = futureMap.remove(requestId);
        if (future != null) {
            if (cancel) {
                if (future.getScheduledFuture().cancel(false)) {
                    boolean success = !isErrorMessage(message);
                    DefaultResponse response = new DefaultResponse(success, channel, message);
                    future.doReceived(response);
                } else {
                    logger.warn("远程服务器返回时间过长,服务器已经做了超时处理 {} {}", requestId, message);
                }

            } else {
                boolean success = !isErrorMessage(message);
                DefaultResponse response = new DefaultResponse(success, channel, message);
                future.doReceived(response);
            }


        } else {
            logger.warn("远程服务器返回时间过长,服务器已经做了超时处理 {} cancel{} {} ", requestId, cancel, message);
        }
    }


    public boolean isErrorMessage(Message message) {
        return errorMessageIds.contains(message.messageId());
    }

    public void checkTimeoutFuture() {
    }

    public void checkTimeoutFuture2() {

        long now = System.currentTimeMillis();
        for (DefaultFuture future : futureMap.values()) {
            if (future.isDone()) {
                continue;
            }
            if (now - future.getSendTime() > future.getTimeout()) {
                SCFrameworkErrorMessage errorMessage = new SCFrameworkErrorMessage();
                errorMessage.setCode(Constant.ERROR_TIMEOUT);
                errorMessage.setMessage("同步请求超时[" + future.getMessage().messageId() + "][" + future.getRequestId() + "]:" + future.getTimeout());
                errorMessage.getArgs().add(String.valueOf(future.getMessage().messageId()));
                errorMessage.getArgs().add(String.valueOf(future.getRequestId()));

                service.execute(() -> receive(future.getChannel(), future.getRequestId(), errorMessage, false));
            }
        }
    }


    public void addErrorMessage(int messageId) {

        errorMessageIds.add(messageId);
    }

    public void addErrorMessages(List<Integer> messageIds) {

        errorMessageIds.addAll(messageIds);
    }

    public TaskLoopGroup getService() {
        return service;
    }

    public void shutdownService() {
        service.shutdownGracefully();
    }

}
