package com.senpure.io.server.remoting;

import javax.annotation.Nonnull;

public interface ResponseFuture {


    @Nonnull
    Response get();

    @Nonnull
    Response get(int timeout);

    void setCallback(@Nonnull ResponseCallback callback);

    boolean isDone();
}
