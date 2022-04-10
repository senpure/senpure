package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class SCAskHandleMessage extends CompressMessage {

    public static final int MESSAGE_ID = 114;
    //是否可以处理
    private boolean handle;
    //token
    private long askToken;
    private int fromMessageId;
    //值
    private String askValue;

    public void copy(SCAskHandleMessage source) {
        this.handle = source.isHandle();
        this.askToken = source.getAskToken();
        this.fromMessageId = source.getFromMessageId();
        this.askValue = source.getAskValue();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //是否可以处理
        writeBoolean(buf, 8, handle);
        //token
        writeVar64(buf, 16, askToken);
        writeVar32(buf, 24, fromMessageId);
        //值
        if (askValue != null) {
            writeString(buf, 35, askValue);
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
                //是否可以处理
                case 8:// 1 << 3 | 0
                    handle = readBoolean(buf);
                    break;
                //token
                case 16:// 2 << 3 | 0
                    askToken = readVar64(buf);
                    break;
                case 24:// 3 << 3 | 0
                    fromMessageId = readVar32(buf);
                    break;
                //值
                case 35:// 4 << 3 | 3
                    askValue = readString(buf);
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
        //是否可以处理
        //tag size 8
        size += computeBooleanSize(1, handle);
        //token
        //tag size 16
        size += computeVar64Size(1, askToken);
        //tag size 24
        size += computeVar32Size(1, fromMessageId);
        //值
        if (askValue != null) {
             //tag size 35
             size += computeStringSize(1, askValue);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * is 是否可以处理
     *
     * @return
     */
    public boolean isHandle() {
        return handle;
    }

    /**
     * set 是否可以处理
     */
    public SCAskHandleMessage setHandle(boolean handle) {
        this.handle = handle;
        return this;
    }

    /**
     * get token
     *
     * @return
     */
    public long getAskToken() {
        return askToken;
    }

    /**
     * set token
     */
    public SCAskHandleMessage setAskToken(long askToken) {
        this.askToken = askToken;
        return this;
    }

    public int getFromMessageId() {
        return fromMessageId;
    }

    public SCAskHandleMessage setFromMessageId(int fromMessageId) {
        this.fromMessageId = fromMessageId;
        return this;
    }

    public String getAskValue() {
        return askValue;
    }

    public SCAskHandleMessage setAskValue(String askValue) {
        this.askValue = askValue;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 114;
    }

    @Override
    public String toString() {
        return "SCAskHandleMessage[114]{"
                + "handle=" + handle
                + ",askToken=" + askToken
                + ",fromMessageId=" + fromMessageId
                + ",askValue=" + askValue
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 13
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCAskHandleMessage").append("[114]").append("{");
        //是否可以处理
        sb.append("\n");
        sb.append(indent).append("handle        = ").append(handle);
        //token
        sb.append("\n");
        sb.append(indent).append("askToken      = ").append(askToken);
        sb.append("\n");
        sb.append(indent).append("fromMessageId = ").append(fromMessageId);
        //值
        sb.append("\n");
        sb.append(indent).append("askValue      = ").append(askValue);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}