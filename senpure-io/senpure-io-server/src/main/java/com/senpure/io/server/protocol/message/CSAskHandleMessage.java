package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSAskHandleMessage extends CompressMessage {

    public static final int MESSAGE_ID = 113;
    //askToken
    private long askToken;
    private int fromMessageId;
    private byte [] data;

    public void copy(CSAskHandleMessage source) {
        this.askToken = source.getAskToken();
        this.fromMessageId = source.getFromMessageId();
        if (source.getData() != null) {
            this.data = copyBytes(source.getData());
        } else {
            this.data = null;
        }
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //askToken
        writeVar64(buf, 8, askToken);
        writeVar32(buf, 16, fromMessageId);
        if (data != null) {
            writeBytes(buf, 27, data);
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
                //askToken
                case 8:// 1 << 3 | 0
                    askToken = readVar64(buf);
                    break;
                case 16:// 2 << 3 | 0
                    fromMessageId = readVar32(buf);
                    break;
                case 27:// 3 << 3 | 3
                    data = readBytes(buf);
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
        //askToken
        //tag size 8
        size += computeVar64Size(1, askToken);
        //tag size 16
        size += computeVar32Size(1, fromMessageId);
        if (data != null) {
             //tag size 27
             size += computeBytesSize(1, data);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get askToken
     *
     * @return
     */
    public long getAskToken() {
        return askToken;
    }

    /**
     * set askToken
     */
    public CSAskHandleMessage setAskToken(long askToken) {
        this.askToken = askToken;
        return this;
    }

    public int getFromMessageId() {
        return fromMessageId;
    }

    public CSAskHandleMessage setFromMessageId(int fromMessageId) {
        this.fromMessageId = fromMessageId;
        return this;
    }

    public byte [] getData() {
        return data;
    }

    public CSAskHandleMessage setData(byte [] data) {
        this.data = data;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 113;
    }

    @Override
    public String toString() {
        return "CSAskHandleMessage[113]{"
                + "askToken=" + askToken
                + ",fromMessageId=" + fromMessageId
                + ",data=" + bytesToString(data)
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 13
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSAskHandleMessage").append("[113]").append("{");
        //askToken
        sb.append("\n");
        sb.append(indent).append("askToken      = ").append(askToken);
        sb.append("\n");
        sb.append(indent).append("fromMessageId = ").append(fromMessageId);
        sb.append("\n");
        sb.append(indent).append("data          = ").append(bytesToString(data));
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}