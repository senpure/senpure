package com.senpure.io.server.gateway;


import com.senpure.base.util.Assert;
import com.senpure.io.protocol.Message;
import com.senpure.io.server.support.MessageIdReader;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;


public class GatewayReceiveProviderMessage implements GatewaySendConsumerMessage, Message {

    private Long[] userIds;

    private int requestId;
    private long token;
    private int messageId;

    private byte[] data;

    private final int messageLength;
    private int messageType;

    public GatewayReceiveProviderMessage(int messageLength, byte[] data) {
        this.messageLength = messageLength;
        this.data = data;

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


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public String toString() {

        return "GatewayReceiveProviderMessage{" +
                "messageId=" + MessageIdReader.read(messageId) +
                ",messageType =" + messageType +
                ",requestId =" + requestId +
                ",token=" + token +
                ", userIds=" + Arrays.toString(userIds) +
                ", dataLen=" + (data == null ? 0 : data.length) +
                '}';
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
        return null;
    }

    @Override
    public int serializedSize() {
        return messageLength;
    }


}
