package com.senpure.io.server.protocol.bean;

import com.senpure.io.protocol.CompressBean;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class IdName extends CompressBean {
    //消息id
    private int id;
    //有意义的字符串
    private String messageName;

    public void copy(IdName source) {
        this.id = source.getId();
        this.messageName = source.getMessageName();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //消息id
        writeVar32(buf, 8, id);
        //有意义的字符串
        if (messageName != null) {
            writeString(buf, 19, messageName);
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
                //消息id
                case 8:// 1 << 3 | 0
                    id = readVar32(buf);
                    break;
                //有意义的字符串
                case 19:// 2 << 3 | 3
                    messageName = readString(buf);
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
        //消息id
        //tag size 8
        size += computeVar32Size(1, id);
        //有意义的字符串
        if (messageName != null) {
             //tag size 19
             size += computeStringSize(1, messageName);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get 消息id
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * set 消息id
     */
    public IdName setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * get 有意义的字符串
     *
     * @return
     */
    public String getMessageName() {
        return messageName;
    }

    /**
     * set 有意义的字符串
     */
    public IdName setMessageName(String messageName) {
        this.messageName = messageName;
        return this;
    }

    @Override
    public String toString() {
        return "IdName{"
                + "id=" + id
                + ",messageName=" + messageName
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 11
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("IdName").append("{");
        //消息id
        sb.append("\n");
        sb.append(indent).append("id          = ").append(id);
        //有意义的字符串
        sb.append("\n");
        sb.append(indent).append("messageName = ").append(messageName);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}