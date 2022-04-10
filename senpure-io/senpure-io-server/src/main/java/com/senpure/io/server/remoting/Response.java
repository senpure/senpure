package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public interface Response {

    boolean isSuccess();

    @Nonnull
    <T extends Message> T getMessage();

    @Nonnull
    Channel getChannel();
}
