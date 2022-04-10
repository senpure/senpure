package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSBreakUserGatewayMessage extends CompressMessage {

    public static final int MESSAGE_ID = 111;
    //channel token
    private long token;
    //用户Id
    private long userId;
    //relation token
    private long relationToken;
    //error,userChange,userOffline
    private String type;

    public void copy(CSBreakUserGatewayMessage source) {
        this.token = source.getToken();
        this.userId = source.getUserId();
        this.relationToken = source.getRelationToken();
        this.type = source.getType();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //channel token
        writeVar64(buf, 8, token);
        //用户Id
        writeVar64(buf, 16, userId);
        //relation token
        writeVar64(buf, 24, relationToken);
        //error,userChange,userOffline
        if (type != null) {
            writeString(buf, 35, type);
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
                //channel token
                case 8:// 1 << 3 | 0
                    token = readVar64(buf);
                    break;
                //用户Id
                case 16:// 2 << 3 | 0
                    userId = readVar64(buf);
                    break;
                //relation token
                case 24:// 3 << 3 | 0
                    relationToken = readVar64(buf);
                    break;
                //error,userChange,userOffline
                case 35:// 4 << 3 | 3
                    type = readString(buf);
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
        //channel token
        //tag size 8
        size += computeVar64Size(1, token);
        //用户Id
        //tag size 16
        size += computeVar64Size(1, userId);
        //relation token
        //tag size 24
        size += computeVar64Size(1, relationToken);
        //error,userChange,userOffline
        if (type != null) {
             //tag size 35
             size += computeStringSize(1, type);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get channel token
     *
     * @return
     */
    public long getToken() {
        return token;
    }

    /**
     * set channel token
     */
    public CSBreakUserGatewayMessage setToken(long token) {
        this.token = token;
        return this;
    }

    /**
     * get 用户Id
     *
     * @return
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set 用户Id
     */
    public CSBreakUserGatewayMessage setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * get relation token
     *
     * @return
     */
    public long getRelationToken() {
        return relationToken;
    }

    /**
     * set relation token
     */
    public CSBreakUserGatewayMessage setRelationToken(long relationToken) {
        this.relationToken = relationToken;
        return this;
    }

    /**
     * get error,userChange,userOffline
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * set error,userChange,userOffline
     */
    public CSBreakUserGatewayMessage setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 111;
    }

    @Override
    public String toString() {
        return "CSBreakUserGatewayMessage[111]{"
                + "token=" + token
                + ",userId=" + userId
                + ",relationToken=" + relationToken
                + ",type=" + type
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 13
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSBreakUserGatewayMessage").append("[111]").append("{");
        //channel token
        sb.append("\n");
        sb.append(indent).append("token         = ").append(token);
        //用户Id
        sb.append("\n");
        sb.append(indent).append("userId        = ").append(userId);
        //relation token
        sb.append("\n");
        sb.append(indent).append("relationToken = ").append(relationToken);
        //error,userChange,userOffline
        sb.append("\n");
        sb.append(indent).append("type          = ").append(type);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}