package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 注册provider返回
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class SCRegisterProviderMessage extends CompressMessage {

    public static final int MESSAGE_ID = 104;
    private String message;

    public void copy(SCRegisterProviderMessage source) {
        this.message = source.getMessage();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        if (message != null) {
            writeString(buf, 11, message);
        }
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
                case 11:// 1 << 3 | 3
                    message = readString(buf);
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
        if (message != null) {
             //tag size 11
             size += computeStringSize(1, message);
        }
        serializedSize = size ;
        return size ;
    }

    public String getMessage() {
        return message;
    }

    public SCRegisterProviderMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 104;
    }

    @Override
    public String toString() {
        return "SCRegisterProviderMessage[104]{"
                + "message=" + message
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 7
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCRegisterProviderMessage").append("[104]").append("{");
        sb.append("\n");
        sb.append(indent).append("message = ").append(message);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}