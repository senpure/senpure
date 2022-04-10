package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class SCRelationUserGatewayMessage extends CompressMessage {

    public static final int MESSAGE_ID = 110;
    //channel token
    private long token;
    //userId
    private long userId;
    //relation token
    private long relationToken;

    public void copy(SCRelationUserGatewayMessage source) {
        this.token = source.getToken();
        this.userId = source.getUserId();
        this.relationToken = source.getRelationToken();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //channel token
        writeVar64(buf, 8, token);
        //userId
        writeVar64(buf, 16, userId);
        //relation token
        writeVar64(buf, 24, relationToken);
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
                //userId
                case 16:// 2 << 3 | 0
                    userId = readVar64(buf);
                    break;
                //relation token
                case 24:// 3 << 3 | 0
                    relationToken = readVar64(buf);
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
        //userId
        //tag size 16
        size += computeVar64Size(1, userId);
        //relation token
        //tag size 24
        size += computeVar64Size(1, relationToken);
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
    public SCRelationUserGatewayMessage setToken(long token) {
        this.token = token;
        return this;
    }

    /**
     * get userId
     *
     * @return
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set userId
     */
    public SCRelationUserGatewayMessage setUserId(long userId) {
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
    public SCRelationUserGatewayMessage setRelationToken(long relationToken) {
        this.relationToken = relationToken;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 110;
    }

    @Override
    public String toString() {
        return "SCRelationUserGatewayMessage[110]{"
                + "token=" + token
                + ",userId=" + userId
                + ",relationToken=" + relationToken
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 13
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCRelationUserGatewayMessage").append("[110]").append("{");
        //channel token
        sb.append("\n");
        sb.append(indent).append("token         = ").append(token);
        //userId
        sb.append("\n");
        sb.append(indent).append("userId        = ").append(userId);
        //relation token
        sb.append("\n");
        sb.append(indent).append("relationToken = ").append(relationToken);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}