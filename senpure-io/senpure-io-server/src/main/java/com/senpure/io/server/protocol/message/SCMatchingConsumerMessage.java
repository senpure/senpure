package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 向网关请求将目标分配到一台实例
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class SCMatchingConsumerMessage extends CompressMessage {

    public static final int MESSAGE_ID = 122;
    private String serverName;
    private String serverKey;
    private long matchableId;

    public void copy(SCMatchingConsumerMessage source) {
        this.serverName = source.getServerName();
        this.serverKey = source.getServerKey();
        this.matchableId = source.getMatchableId();
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
        writeVar64(buf, 24, matchableId);
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
                    matchableId = readVar64(buf);
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
        size += computeVar64Size(1, matchableId);
        serializedSize = size ;
        return size ;
    }

    public String getServerName() {
        return serverName;
    }

    public SCMatchingConsumerMessage setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getServerKey() {
        return serverKey;
    }

    public SCMatchingConsumerMessage setServerKey(String serverKey) {
        this.serverKey = serverKey;
        return this;
    }

    public long getMatchableId() {
        return matchableId;
    }

    public SCMatchingConsumerMessage setMatchableId(long matchableId) {
        this.matchableId = matchableId;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 122;
    }

    @Override
    public String toString() {
        return "SCMatchingConsumerMessage[122]{"
                + "serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",matchableId=" + matchableId
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 11
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCMatchingConsumerMessage").append("[122]").append("{");
        sb.append("\n");
        sb.append(indent).append("serverName  = ").append(serverName);
        sb.append("\n");
        sb.append(indent).append("serverKey   = ").append(serverKey);
        sb.append("\n");
        sb.append(indent).append("matchableId = ").append(matchableId);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}