package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.MessageFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractMessageSender<T extends MessageFrame> implements MessageSender {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected final AtomicInteger atomicRequestId = new AtomicInteger(1);


    public int nextRequestId() {
        int requestId = atomicRequestId.getAndIncrement();
        //为了编码效率将负数提高为正数
        if (requestId < 0) {
            logger.info("requestId {} 小于0 重新获取", requestId);
            synchronized (atomicRequestId) {
                requestId = atomicRequestId.getAndIncrement();
                if (requestId < 0) {
                    atomicRequestId.set(1);
                    requestId = atomicRequestId.getAndIncrement();
                } else {
                    logger.info("requestId {} 已经被其他线程修改", requestId);
                }
                return requestId;
            }
        }
        return requestId;
    }

    public abstract T createMessage(Message message);

    public abstract T createMessage(Message message, boolean requestId);

    public abstract T createMessage(Message message, int requestId);


    public int requestId() {
        return REQUEST_ID.get();
    }


}
