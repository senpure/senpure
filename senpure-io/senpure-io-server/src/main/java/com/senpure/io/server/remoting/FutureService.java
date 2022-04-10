package com.senpure.io.server.remoting;

import com.senpure.io.protocol.Message;
import io.netty.channel.Channel;


public interface FutureService {

    ResponseFuture future( Channel channel, int requestId, Message message,int timeout);



}
