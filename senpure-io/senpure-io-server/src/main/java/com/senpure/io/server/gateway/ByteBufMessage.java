package com.senpure.io.server.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;

public class ByteBufMessage implements Message {
    private final int messageId;
    private final int messageType;
    private final int messageLength;
    private final byte[] data;

    public ByteBufMessage(int messageId, int messageType, int messageLength, byte[] data) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.messageLength = messageLength;
        this.data = data;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBytes(data);
    }

    @Override
    public void read(ByteBuf buf, int maxIndex) {

        Assert.error("not allow");
    }

    @Override
    public String toString(String indent) {
        return "ByteBufMessage[" + MessageIdReader.read(messageId) + "]";
    }

    @Override
    public int serializedSize() {
        return messageLength;
    }

    @Override
    public int messageId() {
        return messageId;
    }

    @Override
    public int messageType() {
        return messageType;
    }
}
