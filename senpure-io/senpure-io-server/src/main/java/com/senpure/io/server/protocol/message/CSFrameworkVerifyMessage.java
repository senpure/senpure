package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 框架内部验证
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSFrameworkVerifyMessage extends CompressMessage {

    public static final int MESSAGE_ID = 101;
    //服务名
    private String serverName;
    //服务实例唯一标识
    private String serverKey;
    //服务类型
    private String serverType;
    //服务扩展字段
    private String serverOption;
    //ip
    private String ip;
    //账号名
    private String userName;
    //账号类型
    private String userType;
    //账号密码
    private String password;
    //认证token
    private String token;

    public void copy(CSFrameworkVerifyMessage source) {
        this.serverName = source.getServerName();
        this.serverKey = source.getServerKey();
        this.serverType = source.getServerType();
        this.serverOption = source.getServerOption();
        this.ip = source.getIp();
        this.userName = source.getUserName();
        this.userType = source.getUserType();
        this.password = source.getPassword();
        this.token = source.getToken();
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
        //ip
        if (ip != null) {
            writeString(buf, 43, ip);
        }
        //账号名
        if (userName != null) {
            writeString(buf, 51, userName);
        }
        //账号类型
        if (userType != null) {
            writeString(buf, 59, userType);
        }
        //账号密码
        if (password != null) {
            writeString(buf, 67, password);
        }
        //认证token
        if (token != null) {
            writeString(buf, 75, token);
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
                //ip
                case 43:// 5 << 3 | 3
                    ip = readString(buf);
                    break;
                //账号名
                case 51:// 6 << 3 | 3
                    userName = readString(buf);
                    break;
                //账号类型
                case 59:// 7 << 3 | 3
                    userType = readString(buf);
                    break;
                //账号密码
                case 67:// 8 << 3 | 3
                    password = readString(buf);
                    break;
                //认证token
                case 75:// 9 << 3 | 3
                    token = readString(buf);
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
        //ip
        if (ip != null) {
             //tag size 43
             size += computeStringSize(1, ip);
        }
        //账号名
        if (userName != null) {
             //tag size 51
             size += computeStringSize(1, userName);
        }
        //账号类型
        if (userType != null) {
             //tag size 59
             size += computeStringSize(1, userType);
        }
        //账号密码
        if (password != null) {
             //tag size 67
             size += computeStringSize(1, password);
        }
        //认证token
        if (token != null) {
             //tag size 75
             size += computeStringSize(1, token);
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
    public CSFrameworkVerifyMessage setServerName(String serverName) {
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
    public CSFrameworkVerifyMessage setServerKey(String serverKey) {
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
    public CSFrameworkVerifyMessage setServerType(String serverType) {
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
    public CSFrameworkVerifyMessage setServerOption(String serverOption) {
        this.serverOption = serverOption;
        return this;
    }

    /**
     * get ip
     *
     * @return
     */
    public String getIp() {
        return ip;
    }

    /**
     * set ip
     */
    public CSFrameworkVerifyMessage setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * get 账号名
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * set 账号名
     */
    public CSFrameworkVerifyMessage setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * get 账号类型
     *
     * @return
     */
    public String getUserType() {
        return userType;
    }

    /**
     * set 账号类型
     */
    public CSFrameworkVerifyMessage setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    /**
     * get 账号密码
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * set 账号密码
     */
    public CSFrameworkVerifyMessage setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * get 认证token
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * set 认证token
     */
    public CSFrameworkVerifyMessage setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 101;
    }

    @Override
    public String toString() {
        return "CSFrameworkVerifyMessage[101]{"
                + "serverName=" + serverName
                + ",serverKey=" + serverKey
                + ",serverType=" + serverType
                + ",serverOption=" + serverOption
                + ",ip=" + ip
                + ",userName=" + userName
                + ",userType=" + userType
                + ",password=" + password
                + ",token=" + token
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 12
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSFrameworkVerifyMessage").append("[101]").append("{");
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
        //ip
        sb.append("\n");
        sb.append(indent).append("ip           = ").append(ip);
        //账号名
        sb.append("\n");
        sb.append(indent).append("userName     = ").append(userName);
        //账号类型
        sb.append("\n");
        sb.append(indent).append("userType     = ").append(userType);
        //账号密码
        sb.append("\n");
        sb.append(indent).append("password     = ").append(password);
        //认证token
        sb.append("\n");
        sb.append(indent).append("token        = ").append(token);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}