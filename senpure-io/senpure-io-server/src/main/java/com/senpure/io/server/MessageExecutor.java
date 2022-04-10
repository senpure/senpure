package com.senpure.io.server;

import io.netty.channel.Channel;

public interface MessageExecutor<T extends MessageFrame> {
    void execute(Channel channel, T frame);
}
