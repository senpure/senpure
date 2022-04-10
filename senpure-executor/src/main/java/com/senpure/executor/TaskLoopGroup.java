package com.senpure.executor;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 * ExecutorGroup
 *
 * @author senpure
 * @time 2019-12-23 17:48:12
 */
public interface TaskLoopGroup extends EventExecutorGroup {


    @Override
    TaskLoop next();

    TaskLoop get(long id);

}
