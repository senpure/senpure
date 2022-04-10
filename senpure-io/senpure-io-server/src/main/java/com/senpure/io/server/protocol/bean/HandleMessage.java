package com.senpure.io.server.protocol.bean;

import com.senpure.io.protocol.CompressBean;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class HandleMessage extends CompressBean {
    //可以处理的消息ID
    private int handleMessageId;
    //消息名提高可读性
    private String messageName;
    //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
    private boolean direct;

    public void copy(HandleMessage source) {
        this.handleMessageId = source.getHandleMessageId();
        this.messageName = source.getMessageName();
        this.direct = source.isDirect();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //可以处理的消息ID
        writeVar32(buf, 8, handleMessageId);
        //消息名提高可读性
        if (messageName != null) {
            writeString(buf, 19, messageName);
        }
        //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
        writeBoolean(buf, 24, direct);
    }

    /**
     * 读取字节缓存
     */
    @Override
    public void read(ByteBuf buf, int endIndex) {
        while (true) {
            int tag = readTag(buf, endIndex);
            switch (tag) {
                case 0://end
                    return;
                //可以处理的消息ID
                case 8:// 1 << 3 | 0
                    handleMessageId = readVar32(buf);
                    break;
                //消息名提高可读性
                case 19:// 2 << 3 | 3
                    messageName = readString(buf);
                    break;
                //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
                case 24:// 3 << 3 | 0
                    direct = readBoolean(buf);
                    break;
                default://skip
                    skip(buf, tag);
                    break;
            }
        }
    }

    private int serializedSize = -1;

    @Override
    public int serializedSize() {
        int size = serializedSize;
        if (size != -1) {
            return size;
        }
        size = 0;
        //可以处理的消息ID
        //tag size 8
        size += computeVar32Size(1, handleMessageId);
        //消息名提高可读性
        if (messageName != null) {
             //tag size 19
             size += computeStringSize(1, messageName);
        }
        //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
        //tag size 24
        size += computeBooleanSize(1, direct);
        serializedSize = size ;
        return size ;
    }

    /**
     * get 可以处理的消息ID
     *
     * @return
     */
    public int getHandleMessageId() {
        return handleMessageId;
    }

    /**
     * set 可以处理的消息ID
     */
    public HandleMessage setHandleMessageId(int handleMessageId) {
        this.handleMessageId = handleMessageId;
        return this;
    }

    /**
     * get 消息名提高可读性
     *
     * @return
     */
    public String getMessageName() {
        return messageName;
    }

    /**
     * set 消息名提高可读性
     */
    public HandleMessage setMessageName(String messageName) {
        this.messageName = messageName;
        return this;
    }

    /**
     * is true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
     *
     * @return
     */
    public boolean isDirect() {
        return direct;
    }

    /**
     * set true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
     */
    public HandleMessage setDirect(boolean direct) {
        this.direct = direct;
        return this;
    }

    @Override
    public String toString() {
        return "HandleMessage{"
                + "handleMessageId=" + handleMessageId
                + ",messageName=" + messageName
                + ",direct=" + direct
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 15
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("HandleMessage").append("{");
        //可以处理的消息ID
        sb.append("\n");
        sb.append(indent).append("handleMessageId = ").append(handleMessageId);
        //消息名提高可读性
        sb.append("\n");
        sb.append(indent).append("messageName     = ").append(messageName);
        //true网关直接选择服务器转发，false 网关会对所有处理该消息的服务器进行一次询问
        sb.append("\n");
        sb.append(indent).append("direct          = ").append(direct);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}