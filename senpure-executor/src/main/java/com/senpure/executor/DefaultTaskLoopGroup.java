package com.senpure.executor;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.RejectedExecutionHandlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DefaultTaskLoopGroup extends MultithreadEventExecutorGroup implements TaskLoopGroup {


    private Map<Long, TaskLoop> taskLoopMap = new ConcurrentHashMap<>(1024);

    public DefaultTaskLoopGroup(int nThreads) {
        this(nThreads, null);
    }

    /**
     * Create a new instance.
     *
     * @param nThreads      the number of threads that will be used by this instance.
     * @param threadFactory the ThreadFactory to use, or {@code null} if the default should be used.
     */
    public DefaultTaskLoopGroup(int nThreads, ThreadFactory threadFactory) {
        this(nThreads, threadFactory, 16,
                RejectedExecutionHandlers.reject());
    }

    /**
     * Create a new instance.
     *
     * @param nThreads        the number of threads that will be used by this instance.
     * @param threadFactory   the ThreadFactory to use, or {@code null} if the default should be used.
     * @param maxPendingTasks the maximum number of pending tasks before new tasks will be rejected.
     * @param rejectedHandler the {@link RejectedExecutionHandler} to use.
     */
    public DefaultTaskLoopGroup(int nThreads, ThreadFactory threadFactory, int maxPendingTasks,
                                RejectedExecutionHandler rejectedHandler) {
        super(nThreads, threadFactory, maxPendingTasks, rejectedHandler);
    }

    @Override
    public TaskLoop next() {
        return (TaskLoop) super.next();
    }


    @Override
    public TaskLoop get(long id) {
        TaskLoop taskLoop = taskLoopMap.get(id);
        if (taskLoop == null) {
            taskLoop = taskLoopMap.computeIfAbsent(id, key -> next());
        }
        return taskLoop;
    }

    @Override
    protected EventExecutor newChild(Executor executor, Object... args) throws Exception {
        return new DefaultTaskLoop(this, executor, (Integer) args[0], (RejectedExecutionHandler) args[1]);
    }


}
