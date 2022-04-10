package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 向网关请求消息转发
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSMessageForwardMessage extends CompressMessage {

    public static final int MESSAGE_ID = 120;
    //服务名
    private String serverName;
    //服务实例唯一标识
    private String serverKey;
    //服务类型
    private String serverType;
    //服务扩展字段
    private String serverOption;
    //messageId
    private int id;
    //message data
    private byte [] data;

    public void copy(CSMessageForwardMessage source) {
        this.serverName = source.getServerName();
        this.serverKey = source.getServerKey();
        this.serverType = source.getServerType();
        this.serverOption = source.getServerOption();
        this.id = source.getId();
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
        //服务名
        if (serverName != null) {
            writeString(buf, 11, serverName);
        }
        //服务实例唯一标识
        if (serverKey != null) {
            writeString(buf, 19, serverKey);
        }
        //服务类型
        if (serverType != null) {
            writeString(buf, 27, serverType);
        }
        //服务扩展字段
        if (serverOption != null) {
            writeString(buf, 35, serverOption);
        }
        //messageId
        writeVar32(buf, 40, id);
        //message data
        if (data != null) {
            writeBytes(buf, 51, data);
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
                //服务名
                case 11:// 1 << 3 | 3
                    serverName = readString(buf);
                    break;
                //服务实例唯一标识
                case 19:// 2 << 3 | 3
                    serverKey = readString(buf);
                    break;
                //服务类型
                case 27:// 3 << 3 | 3
                    serverType = readString(buf);
                    break;
                //服务扩展字段
                case 35:// 4 << 3 | 3
                    serverOption = readString(buf);
                    break;
                //messageId
                case 40:// 5 << 3 | 0
                    id = readVar32(buf);
                    break;
                //message data
                case 51:// 6 << 3 | 3
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
        //服务名
        if (serverName != null) {
             //tag size 11
             size += computeStringSize(1, serverName);
        }
        //服务实例唯一标识
        if (serverKey != null) {
             //tag size 19
             size += computeStringSize(1, serverKey);
        }
        //服务类型
        if (serverType != null) {
             //tag size 27
             size += computeStringSize(1, serverType);
        }
        //服务扩展字段
        if (serverOption != null) {
             //tag size 35
             size += computeStringSize(1, serverOption);
        }
        //messageId
        //tag size 40
        size += computeVar32Size(1, id);
        //message data
        if (data != null) {
             //tag size 51
             size += computeBytesSize(1, data);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get 服务名
     *
     * @return
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * set 服务名
     */
    public CSMessageForwardMessage setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    /**
     * get 服务实例唯一标识
     *
     * @return
     */
    public String getServerKey() {
        return serverKey;
    }

    /**
     * set 服务实例唯一标识
     */
    public CSMessageForwardMessage setServerKey(String serverKey) {
        this.serverKey = serverKey;
        return this;
    }

    /**
     * get 服务类型
     *
     * @return
     */
    public String getServerType() {
        return serverType;
    }

    /**
     * set 服务类型
     */
    public CSMessageForwardMessage setServerType(String serverType) {
        this.serverType = serverType;
        return this;
    }

    /**
     * get 服务扩展字段
     *
     * @return
     */
    public String getServerOption() {
        return serverOption;
    }

    /**
     * set 服务扩展字段
     */
    public CSMessageForwardMessage setServerOption(String serverOption) {
        this.serverOption = serverOption;
        return this;
    }

    /**
     * get messageId
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * set messageId
     */
    public CSMessageForwardMessage setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * get message data
     *
     * @return
     */
    public byte [] getData() {
        return data;
    }

    /**
     * set message data
     */
    public CSMessageForwardMessage setData(byte [] data) {
        this.data = data;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 120;
    }

    @Override
    public String toString() {
        return "CSMessageForwardMessage[120]{"
                + "serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",serverType=" + serverType
                + ",serverOption=" + serverOption
                + ",id=" + id
                + ",data=" + bytesToString(data)
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 12
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSMessageForwardMessage").append("[120]").append("{");
        //服务名
        sb.append("\n");
        sb.append(indent).append("serverName   = ").append(serverName);
        //服务实例唯一标识
        sb.append("\n");
        sb.append(indent).append("serverKey    = ").append(serverKey);
        //服务类型
        sb.append("\n");
        sb.append(indent).append("serverType   = ").append(serverType);
        //服务扩展字段
        sb.append("\n");
        sb.append(indent).append("serverOption = ").append(serverOption);
        //messageId
        sb.append("\n");
        sb.append(indent).append("id           = ").append(id);
        //message data
        sb.append("\n");
        sb.append(indent).append("data         = ").append(bytesToString(data));
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}