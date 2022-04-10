package com.senpure.io.server.remoting;

import com.senpure.io.server.MessageFrame;
import io.netty.channel.Channel;


public interface MessageExecutor<T extends MessageFrame> {

    void execute(Channel channel, T  frame);


}
