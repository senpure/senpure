package com.senpure.executor;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;

/**
 * TaskLoop
 *
 * @author senpure
 * @time 2019-12-24 10:39:09
 */
public interface TaskLoop extends EventExecutor, EventExecutorGroup {


    @Override
    TaskLoop next();

    @Override
    TaskLoopGroup parent();

    /**
     * 同一个线程提交的任务立即执行
     *
     * @param task
     * @return
     */
    Future<?> submitMayNow(Runnable task);

    /**
     * 同一个线程提交的任务立即执行
     *
     * @param task
     * @return
     */
    <T> Future<T> submitMayNow(Runnable task, T result);

    /**
     * 同一个线程提交的任务立即执行
     *
     * @param task
     * @return
     */
    <T> Future<T> submitMayNow(Callable<T> task);
}
