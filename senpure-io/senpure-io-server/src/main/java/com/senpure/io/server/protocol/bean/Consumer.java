package com.senpure.io.server.protocol.bean;

import com.senpure.io.protocol.CompressBean;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class Consumer extends CompressBean {
    private long token;
    private long userId;

    public void copy(Consumer source) {
        this.token = source.getToken();
        this.userId = source.getUserId();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        writeVar64(buf, 8, token);
        writeVar64(buf, 16, userId);
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
                case 8:// 1 << 3 | 0
                    token = readVar64(buf);
                    break;
                case 16:// 2 << 3 | 0
                    userId = readVar64(buf);
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
        //tag size 8
        size += computeVar64Size(1, token);
        //tag size 16
        size += computeVar64Size(1, userId);
        serializedSize = size ;
        return size ;
    }

    public long getToken() {
        return token;
    }

    public Consumer setToken(long token) {
        this.token = token;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public Consumer setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "Consumer{"
                + "token=" + token
                + ",userId=" + userId
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 6
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("Consumer").append("{");
        sb.append("\n");
        sb.append(indent).append("token  = ").append(token);
        sb.append("\n");
        sb.append(indent).append("userId = ").append(userId);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}