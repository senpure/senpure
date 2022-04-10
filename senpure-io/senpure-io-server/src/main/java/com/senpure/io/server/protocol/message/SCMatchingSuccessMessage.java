package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-5-31 22:16:30
 */
public class SCMatchingSuccessMessage extends CompressMessage {

    public static final int MESSAGE_ID = 124;
    private String serverName;
    private String serverKey;
    private int dataId;
    private byte [] data;

    public void copy(SCMatchingSuccessMessage source) {
        this.serverName = source.getServerName();
        this.serverKey = source.getServerKey();
        this.dataId = source.getDataId();
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
        if (serverName != null) {
            writeString(buf, 11, serverName);
        }
        if (serverKey != null) {
            writeString(buf, 19, serverKey);
        }
        writeVar32(buf, 24, dataId);
        if (data != null) {
            writeBytes(buf, 35, data);
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
                    serverName = readString(buf);
                    break;
                case 19:// 2 << 3 | 3
                    serverKey = readString(buf);
                    break;
                case 24:// 3 << 3 | 0
                    dataId = readVar32(buf);
                    break;
                case 35:// 4 << 3 | 3
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
        if (serverName != null) {
             //tag size 11
             size += computeStringSize(1, serverName);
        }
        if (serverKey != null) {
             //tag size 19
             size += computeStringSize(1, serverKey);
        }
        //tag size 24
        size += computeVar32Size(1, dataId);
        if (data != null) {
             //tag size 35
             size += computeBytesSize(1, data);
        }
        serializedSize = size ;
        return size ;
    }

    public String getServerName() {
        return serverName;
    }

    public SCMatchingSuccessMessage setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getServerKey() {
        return serverKey;
    }

    public SCMatchingSuccessMessage setServerKey(String serverKey) {
        this.serverKey = serverKey;
        return this;
    }

    public int getDataId() {
        return dataId;
    }

    public SCMatchingSuccessMessage setDataId(int dataId) {
        this.dataId = dataId;
        return this;
    }

    public byte [] getData() {
        return data;
    }

    public SCMatchingSuccessMessage setData(byte [] data) {
        this.data = data;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 124;
    }

    @Override
    public String toString() {
        return "SCMatchingSuccessMessage[124]{"
                + "serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",dataId=" + dataId
                + ",data=" + bytesToString(data)
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 10
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCMatchingSuccessMessage").append("[124]").append("{");
        sb.append("\n");
        sb.append(indent).append("serverName = ").append(serverName);
        sb.append("\n");
        sb.append(indent).append("serverKey  = ").append(serverKey);
        sb.append("\n");
        sb.append(indent).append("dataId     = ").append(dataId);
        sb.append("\n");
        sb.append(indent).append("data       = ").append(bytesToString(data));
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}