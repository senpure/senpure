package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 框架内部验证结果
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class SCFrameworkVerifyMessage extends CompressMessage {

    public static final int MESSAGE_ID = 102;
    //认证分配的id
    private long userId;

    public void copy(SCFrameworkVerifyMessage source) {
        this.userId = source.getUserId();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //认证分配的id
        writeVar64(buf, 8, userId);
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
                //认证分配的id
                case 8:// 1 << 3 | 0
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
        //认证分配的id
        //tag size 8
        size += computeVar64Size(1, userId);
        serializedSize = size ;
        return size ;
    }

    /**
     * get 认证分配的id
     *
     * @return
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set 认证分配的id
     */
    public SCFrameworkVerifyMessage setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 102;
    }

    @Override
    public String toString() {
        return "SCFrameworkVerifyMessage[102]{"
                + "userId=" + userId
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 6
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCFrameworkVerifyMessage").append("[102]").append("{");
        //认证分配的id
        sb.append("\n");
        sb.append(indent).append("userId = ").append(userId);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}