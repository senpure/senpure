package com.senpure.executor;

import io.netty.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;

/**
 * SingleThreadTaskLoop
 *
 * @author senpure
 * @time 2019-12-24 11:06:55
 */
public class DefaultTaskLoop extends SingleThreadEventExecutor implements TaskLoop {


    public DefaultTaskLoop() {
        this((EventExecutorGroup) null);
    }

    public DefaultTaskLoop(ThreadFactory threadFactory) {
        this(null, threadFactory);
    }

    public DefaultTaskLoop(Executor executor) {
        this(null, executor);
    }

    public DefaultTaskLoop(EventExecutorGroup parent) {
        this(parent, new DefaultThreadFactory(DefaultTaskLoop.class));
    }

    public DefaultTaskLoop(EventExecutorGroup parent, ThreadFactory threadFactory) {
        super(parent, threadFactory, true);
    }

    public DefaultTaskLoop(EventExecutorGroup parent, Executor executor) {
        super(parent, executor, true);
    }

    public DefaultTaskLoop(EventExecutorGroup parent, ThreadFactory threadFactory, int maxPendingTasks,
                           RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, threadFactory, true, maxPendingTasks, rejectedExecutionHandler);
    }

    public DefaultTaskLoop(EventExecutorGroup parent, Executor executor, int maxPendingTasks,
                           RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, true, maxPendingTasks, rejectedExecutionHandler);
    }


    @Override
    public TaskLoop next() {
        return (TaskLoop) super.next();
    }

    @Override
    public TaskLoopGroup parent() {
        return (TaskLoopGroup) super.parent();
    }

    @Override
    protected void run() {
        for (; ; ) {
            Runnable task = takeTask();
            if (task != null) {
                task.run();
                updateLastExecutionTime();
            }

            if (confirmShutdown()) {
                break;
            }
        }
    }


    /**
     * 同一个线程提交的任务立即执行
     *
     * @param task
     * @return
     */
    @Override
    public Future<?> submitMayNow(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        if (inEventLoop()) {
            RunnableFuture<Void> future = newTaskFor(task, null);
            future.run();
            return (Future<?>) future;
        } else {
            return submit(task);
        }

    }

    /**
     * 同一个线程提交的任务立即执行
     *
     * @param task
     * @param result
     * @return
     */
    @Override
    public <T> Future<T> submitMayNow(Runnable task, T result) {
        if (task == null) {
            throw new NullPointerException();
        }
        if (inEventLoop()) {
            RunnableFuture<T> future = newTaskFor(task, result);
            future.run();
            return (Future<T>) future;
        } else {
            return submit(task, result);
        }
    }

    /**
     * 同一个线程提交的任务立即执行
     *
     * @param task
     * @return
     */
    @Override
    public <T> Future<T> submitMayNow(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        if (inEventLoop()) {
            RunnableFuture<T> future = newTaskFor(task);
            future.run();
            return (Future<T>) future;

        } else {
            return submit(task);
        }

    }
}
