package com.senpure.io.server;

import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;

public interface MessageDecoder<T extends Message> extends MessageReactor {

    T decode(ByteBuf buf, int maxIndex);
}
