package com.senpure.io.server.provider;

import com.senpure.io.server.remoting.MessageSender;
import io.netty.util.concurrent.FastThreadLocalThread;

/**
 * ProducerHelper
 *
 * @author senpure
 * @time 2019-12-30 17:40:10
 */
public class ProviderHelper {

    /**
     * 包装一个可传递requestId的 Runnable 释放该 requestId 依赖 FastThreadLocalThread 的包装
     *
     * @param runnable
     * @return
     * @see FastThreadLocalThread
     */
    public static Runnable getRequestIdRunnable(Runnable runnable) {

        return new RequestIdRunnable(runnable);
    }

    private static class RequestIdRunnable implements Runnable {

        private final Runnable runnable;

        private final Integer requestId;

        public RequestIdRunnable(Runnable runnable) {
            this.runnable = runnable;
            requestId = MessageSender.REQUEST_ID.get();
        }

        @Override
        public void run() {
            MessageSender.REQUEST_ID.set(requestId);
            try {
                runnable.run();
            } finally {
                MessageSender.REQUEST_ID.remove();
            }

        }
    }
}
