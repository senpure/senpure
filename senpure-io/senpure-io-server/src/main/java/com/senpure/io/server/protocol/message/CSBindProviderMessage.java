package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSBindProviderMessage extends CompressMessage {

    public static final int MESSAGE_ID = 123;
    private String serverName;
    private String serverKey;
    private long token;
    private long userId;

    public void copy(CSBindProviderMessage source) {
        this.serverName = source.getServerName();
        this.serverKey = source.getServerKey();
        this.token = source.getToken();
        this.userId = source.getUserId();
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
        writeVar64(buf, 24, token);
        writeVar64(buf, 32, userId);
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
                    token = readVar64(buf);
                    break;
                case 32:// 4 << 3 | 0
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
        if (serverName != null) {
             //tag size 11
             size += computeStringSize(1, serverName);
        }
        if (serverKey != null) {
             //tag size 19
             size += computeStringSize(1, serverKey);
        }
        //tag size 24
        size += computeVar64Size(1, token);
        //tag size 32
        size += computeVar64Size(1, userId);
        serializedSize = size ;
        return size ;
    }

    public String getServerName() {
        return serverName;
    }

    public CSBindProviderMessage setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getServerKey() {
        return serverKey;
    }

    public CSBindProviderMessage setServerKey(String serverKey) {
        this.serverKey = serverKey;
        return this;
    }

    public long getToken() {
        return token;
    }

    public CSBindProviderMessage setToken(long token) {
        this.token = token;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public CSBindProviderMessage setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 123;
    }

    @Override
    public String toString() {
        return "CSBindProviderMessage[123]{"
                + "serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",token=" + token
                + ",userId=" + userId
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 10
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSBindProviderMessage").append("[123]").append("{");
        sb.append("\n");
        sb.append(indent).append("serverName = ").append(serverName);
        sb.append("\n");
        sb.append(indent).append("serverKey  = ").append(serverKey);
        sb.append("\n");
        sb.append(indent).append("token      = ").append(token);
        sb.append("\n");
        sb.append(indent).append("userId     = ").append(userId);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}