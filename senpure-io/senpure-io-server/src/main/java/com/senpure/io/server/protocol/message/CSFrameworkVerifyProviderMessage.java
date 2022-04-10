package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 向网关表示自己可以提供框架内部验证功能
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSFrameworkVerifyProviderMessage extends CompressMessage {

    public static final int MESSAGE_ID = 105;
    //服务名
    private String serverName;
    //服务实例唯一标识
    private String serverKey;
    //服务类型
    private String serverType;
    //服务扩展字段
    private String serverOption;

    public void copy(CSFrameworkVerifyProviderMessage source) {
        this.serverName = source.getServerName();
        this.serverKey = source.getServerKey();
        this.serverType = source.getServerType();
        this.serverOption = source.getServerOption();
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
            writeString(buf, 35, serverType);
        }
        //服务扩展字段
        if (serverOption != null) {
            writeString(buf, 43, serverOption);
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
                case 35:// 4 << 3 | 3
                    serverType = readString(buf);
                    break;
                //服务扩展字段
                case 43:// 5 << 3 | 3
                    serverOption = readString(buf);
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
             //tag size 35
             size += computeStringSize(1, serverType);
        }
        //服务扩展字段
        if (serverOption != null) {
             //tag size 43
             size += computeStringSize(1, serverOption);
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
    public CSFrameworkVerifyProviderMessage setServerName(String serverName) {
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
    public CSFrameworkVerifyProviderMessage setServerKey(String serverKey) {
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
    public CSFrameworkVerifyProviderMessage setServerType(String serverType) {
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
    public CSFrameworkVerifyProviderMessage setServerOption(String serverOption) {
        this.serverOption = serverOption;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 105;
    }

    @Override
    public String toString() {
        return "CSFrameworkVerifyProviderMessage[105]{"
                + "serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",serverType=" + serverType
                + ",serverOption=" + serverOption
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 12
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSFrameworkVerifyProviderMessage").append("[105]").append("{");
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
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}