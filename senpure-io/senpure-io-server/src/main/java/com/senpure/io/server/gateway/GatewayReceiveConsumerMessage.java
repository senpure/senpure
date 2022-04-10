package com.senpure.io.server.gateway;

import com.senpure.base.util.Assert;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;

public class GatewayReceiveConsumerMessage implements GatewaySendProviderMessage, Message {

    private int messageType;
    private int requestId;
    private int messageId;
    private long token;
    private long userId;
    private final int messageLength;
    private final byte[] data;

    public GatewayReceiveConsumerMessage(int messageLength, byte[] data) {
        this.messageLength = messageLength;
        this.data = data;
    }


    public byte[] getData() {
        return data;
    }

    @Override
    public int messageType() {
        return messageType;
    }

    @Override
    public int requestId() {
        return requestId;
    }

    @Override
    public int messageId() {
        return messageId;
    }

    @Override
    public Message message() {
        return this;
    }

    @Override
    public long token() {
        return token;
    }

    @Override
    public long userId() {
        return userId;
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
        return toString();
    }

    @Override
    public int serializedSize() {
        return messageLength;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMessageLength() {
        return messageLength;
    }

    @Override
    public String toString() {
        return "GatewayReceiveConsumerMessage{" +
                "messageId=" + MessageIdReader.read(messageId) +
                ", requestId=" + requestId +
                ",token=" + token +
                ", userId=" + userId +
                ", dataLen=" + (data == null ? 0 : data.length) +
                '}';
    }
}
