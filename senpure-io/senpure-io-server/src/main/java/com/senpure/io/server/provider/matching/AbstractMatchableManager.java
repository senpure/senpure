package com.senpure.io.server.provider.matching;

import com.senpure.io.server.protocol.bean.Consumer;

public abstract class AbstractMatchableManager<T extends Matchable> {


    protected abstract T createMatchable();

    protected abstract void markMatchable(Consumer consumer, Matchable matchable);
}
